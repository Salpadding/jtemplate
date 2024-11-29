package com.example.envoyj

import io.envoyproxy.envoy.service.ext_proc.v3.ProcessingRequest
import io.envoyproxy.envoy.service.ext_proc.v3.ProcessingResponse
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse

class EnvoyExtFilterChain(val filters: List<EnvoyExtFilter>): FilterChain, AbstractEnvoyExtFilter.ExtFilterChain {
    private var requestHeaders: Map<String, List<String>>? = null
    private var responseHeaders: Map<String, List<String>>? = null
    var req: ProcessingRequest? = null
    var i: Int = 0

    override fun doFilter(request: ServletRequest, response: ServletResponse?) {
        if(this.req == null) {
            this.req = (request as EnvoyExtProcRequest).req
        }
        val cur = i
        if(i >= filters.size) return
        i += 1
        filters[cur].doFilter(request, response, this)
    }

    override fun doFilter(request: ProcessingRequest, response: ProcessingResponse.Builder) {
        val newReq = EnvoyExtProcRequest(request, response)
        this.doFilter(newReq, null)
    }

    override fun getRequestHeaders(): Map<String, List<String>> {
        val h = requestHeaders
        if(h != null) {
            return h
        }
        val headers: MutableMap<String, List<String>> = mutableMapOf()
        this.req?.requestHeaders?.headers?.headersList?.forEach {
            headers.putIfAbsent(it.key, mutableListOf())
            (headers[it.key] as MutableList<String>).add(it.rawValue.toStringUtf8())
        }
        this.requestHeaders = headers
        return headers
    }


    override fun getAuthority(): String {
        return getRequestHeaders()[":authority"]?.firstOrNull() ?: ""
    }
}