package com.example.envoyj

import io.envoyproxy.envoy.service.ext_proc.v3.ProcessingRequest
import io.envoyproxy.envoy.service.ext_proc.v3.ProcessingResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FilterChainFactory(@Autowired(required = false) val filters: List<EnvoyExtFilter>) {
    companion object {
        val log = LoggerFactory.getLogger(EnvoyExtFilterChain::class.java)
    }

    init {
        log.info("filters = {}", filters.map { it.javaClass.name })
    }

    fun create(): EnvoyExtFilterChain {
        return EnvoyExtFilterChain(
            filters
        )
    }

    fun apply(chain: EnvoyExtFilterChain, req: ProcessingRequest, res: ProcessingResponse.Builder) {
        val servletReq = EnvoyExtProcRequest(req, res)
        chain.doFilter(servletReq, null)
    }
}