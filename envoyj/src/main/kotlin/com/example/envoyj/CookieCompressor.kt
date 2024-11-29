package com.example.envoyj

import io.envoyproxy.envoy.config.core.v3.HeaderValueOption.HeaderAppendAction
import io.envoyproxy.envoy.service.ext_proc.v3.ProcessingRequest
import io.envoyproxy.envoy.service.ext_proc.v3.ProcessingResponse
import io.netty.handler.codec.http.cookie.ClientCookieDecoder
import io.netty.handler.codec.http.cookie.ClientCookieEncoder
import io.netty.handler.codec.http.cookie.ServerCookieDecoder
import io.netty.handler.codec.http.cookie.ServerCookieEncoder
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.net.HttpCookie
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
        val serverParser = ServerCookieDecoder.LAX
        val cookies = request.requestHeaders.headers.headersList.filter { it.key == "cookie" }.
            flatMap { serverParser.decodeAll(it.rawValue.toStringUtf8()) }

        if(cookies.isEmpty()) {
            chain.doFilter(request, response)
            return
        }
        cookies.forEach { c ->
            cache[c.value()]?.let { c.setValue(it) }
        }
        val joined = ClientCookieEncoder.LAX.encode(cookies)
        log.info("joined cookie = {}", joined)
        setRequestHeader(response, "cookie", joined, HeaderAppendAction.OVERWRITE_IF_EXISTS_OR_ADD_VALUE, false)
        chain.doFilter(request, response)
    }

    override fun onResponseHeaders(
        request: ProcessingRequest,
        response: ProcessingResponse.Builder,
        next: ExtFilterChain
    ) {
        val setCookie = request.responseHeaders.headers.headersList.filter { it.key == "set-cookie" }
        val cookies: MutableList<Any> = mutableListOf()

        setCookie.forEach {
            if(it.rawValue.size() < 64) {
                cookies.add(it.rawValue.toStringUtf8())
                return@forEach
            }
            val decoded = ClientCookieDecoder.LAX.decode(it.rawValue.toStringUtf8())
            if(decoded.value().length < 64) {
                cookies.add(it.rawValue.toStringUtf8())
                return@forEach
            }
            val uuid = UUID.randomUUID().toString()
            val prev = decoded.value()
            decoded.setValue(uuid)
            cache[uuid] = prev
            cookies.add(ServerCookieEncoder.LAX.encode(decoded))
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
        log.info("do next")
        next.doFilter(request, response)
    }
}