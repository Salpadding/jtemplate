package com.example.envoyj

import com.google.protobuf.BoolValue
import com.google.protobuf.ByteString
import io.envoyproxy.envoy.config.core.v3.HeaderValue
import io.envoyproxy.envoy.config.core.v3.HeaderValueOption
import io.envoyproxy.envoy.service.ext_proc.v3.HeadersResponse
import io.envoyproxy.envoy.service.ext_proc.v3.ProcessingRequest
import io.envoyproxy.envoy.service.ext_proc.v3.ProcessingResponse
import io.envoyproxy.envoy.service.ext_proc.v3.ProcessingResponse.Builder
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.springframework.core.Ordered

abstract class AbstractEnvoyExtFilter : EnvoyExtFilter, Ordered {
    interface ExtFilterChain {
        fun doFilter(request: ProcessingRequest, response: ProcessingResponse.Builder)
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse?, chain: FilterChain) {
        val req = (request as EnvoyExtProcRequest).req
        val res = (request as EnvoyExtProcRequest).res
        val next: ExtFilterChain = object : ExtFilterChain {
            override fun doFilter(rq: ProcessingRequest, rs: ProcessingResponse.Builder) {
                val newReq = EnvoyExtProcRequest(rq, rs)
                chain.doFilter(newReq, response)
            }
        }

        when (req.requestCase) {
            ProcessingRequest.RequestCase.REQUEST_HEADERS -> {
                onRequestHeaders(req, res, next)
            }

            ProcessingRequest.RequestCase.RESPONSE_HEADERS -> {
                onResponseHeaders(req, res, next)
            }

            else -> {

            }
        }
    }

    abstract fun onRequestHeaders(
        request: ProcessingRequest,
        response: ProcessingResponse.Builder,
        next: ExtFilterChain
    )

    abstract fun onResponseHeaders(
        request: ProcessingRequest,
        response: ProcessingResponse.Builder,
        next: ExtFilterChain
    )

    override fun getOrder(): Int {
        return EnvoyExtFilter.ORDER_MAP.getOrDefault(this.javaClass, 0)
    }

    fun setRequestHeader(response: Builder, key: String, value: String, action: Int, append: Boolean) {
        setHeader(response.requestHeadersBuilder, key, value, action, append)
    }

    fun setResponseHeader(response: Builder, key: String, value: String, action: Int, append: Boolean) {
        setHeader(response.responseHeadersBuilder, key, value, action, append)
    }

    private fun setHeader(builder: HeadersResponse.Builder, key: String, value: String, action: Int, append: Boolean) {
        val mut = builder.responseBuilder.headerMutationBuilder
        mut.addSetHeaders(
            HeaderValueOption.newBuilder().setAppend(BoolValue.of(append)).setHeader(
                HeaderValue.newBuilder().setKeyBytes(
                    ByteString.copyFromUtf8(key)
                ).setRawValue(ByteString.copyFromUtf8(value))
            ).setAppendAction(HeaderValueOption.HeaderAppendAction.forNumber(action))
        )
    }
}