package com.example.envoyj

import io.envoyproxy.envoy.service.ext_proc.v3.CommonResponse
import io.envoyproxy.envoy.service.ext_proc.v3.ExternalProcessorGrpc
import io.envoyproxy.envoy.service.ext_proc.v3.HeadersResponse
import io.envoyproxy.envoy.service.ext_proc.v3.ProcessingRequest
import io.envoyproxy.envoy.service.ext_proc.v3.ProcessingResponse
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ExtProcService : ExternalProcessorGrpc.ExternalProcessorImplBase() {
    companion object {
        @JvmStatic
        val log = LoggerFactory.getLogger(ExtProcService::class.java)
    }


    override fun process(responseObserver: StreamObserver<ProcessingResponse>): StreamObserver<ProcessingRequest> {
        return Observer(responseObserver)
    }

    class Observer(val client: StreamObserver<ProcessingResponse>) : StreamObserver<ProcessingRequest> {
        override fun onNext(value: ProcessingRequest?) {
            if (value == null) return
            val res = when (value.requestCase) {
                ProcessingRequest.RequestCase.REQUEST_HEADERS -> {
                    val headers = value.requestHeaders
                    headers.headers.headersList.forEach {
                        println("header ${it.key} = ${it.value}")
                    }
                    val bd = ProcessingResponse.newBuilder()
                    bd.requestHeaders =
                        HeadersResponse.newBuilder().setResponse(CommonResponse.newBuilder().build()).build()
                    bd.build()
                }

                else -> {
                    val bd = ProcessingResponse.newBuilder()
                    bd.responseHeaders =
                        HeadersResponse.newBuilder().setResponse(CommonResponse.newBuilder().build()).build()
                    bd.build()
                }
            }
            client.onNext(res)
        }

        override fun onError(t: Throwable?) {
            t?.let { log.error("", t) }
        }

        override fun onCompleted() {
        }

    }
}