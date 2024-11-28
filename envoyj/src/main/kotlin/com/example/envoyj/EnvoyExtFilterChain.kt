package com.example.envoyj

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.slf4j.LoggerFactory

class EnvoyExtFilterChain(val filters: List<EnvoyExtFilter>): FilterChain {

    var i: Int = 0

    override fun doFilter(request: ServletRequest, response: ServletResponse?) {
        val cur = i
        if(i >= filters.size) return
        i += 1
        filters[cur].doFilter(request, response, this)
    }
}