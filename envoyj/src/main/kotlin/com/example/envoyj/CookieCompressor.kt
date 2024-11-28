package com.example.envoyj

import io.envoyproxy.envoy.config.core.v3.HeaderValueOption.HeaderAppendAction
import io.envoyproxy.envoy.service.ext_proc.v3.ProcessingRequest
import io.envoyproxy.envoy.service.ext_proc.v3.ProcessingResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.net.HttpCookie
import java.security.SecureRandom
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class CookieCompressor : AbstractEnvoyExtFilter() {
    val cache: MutableMap<String, String> = ConcurrentHashMap()

    companion object {
        val log = LoggerFactory.getLogger(CookieCompressor::class.java)
    }

    override fun onRequestHeaders(
        request: ProcessingRequest,
        response: ProcessingResponse.Builder,
        chain: ExtFilterChain
    ) {
        val cookies = request.requestHeaders.headers.headersList.filter { it.key == "cookie" }.
            flatMap { it.rawValue.toStringUtf8().split(";").
            flatMap { HttpCookie.parse(it) } }

        if(cookies.isEmpty()) {
            chain.doFilter(request, response)
            return
        }
        cookies.forEach { if(cache.contains(it.value)) { it.value = cache[it.value] } }
        val joined = cookies.map { it.toString() }.joinToString(";")
        setRequestHeader(response, "cookie", joined, HeaderAppendAction.OVERWRITE_IF_EXISTS_OR_ADD_VALUE, false)
        chain.doFilter(request, response)
    }

    override fun onResponseHeaders(
        request: ProcessingRequest,
        response: ProcessingResponse.Builder,
        next: ExtFilterChain
    ) {
        val setCookie = request.responseHeaders.headers.headersList.filter { it.key == "set-cookie" }
        val cookies: MutableList<HttpCookie> = mutableListOf()
        if (setCookie.isNotEmpty()) {
            setCookie.forEach {
                val parsed = HttpCookie.parse(it.rawValue.toStringUtf8())
                parsed.forEach {
                    // avoid double quotes be appended
                    it.version = 0
                    if (it.value.length > 64) {
                        val uuid = UUID.randomUUID().toString()
                        cache[uuid] = it.value
                        it.value = uuid
                    }
                    cookies.add(it)
                }
            }
        }
        cookies.forEachIndexed { i, c ->
            val s = c.toString()
            log.info("set-cookie: {}", s)
            if (i == 0)
                setResponseHeader(
                    response,
                    "set-cookie",
                    s,
                    HeaderAppendAction.OVERWRITE_IF_EXISTS_OR_ADD_VALUE,
                    false
                )
            else {
                setResponseHeader(
                    response,
                    "set-cookie",
                    s,
                    HeaderAppendAction.APPEND_IF_EXISTS_OR_ADD_VALUE,
                    true
                )
            }
        }
        next.doFilter(request, response)
    }
}