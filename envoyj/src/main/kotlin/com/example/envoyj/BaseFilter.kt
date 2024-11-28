package com.example.envoyj

import io.envoyproxy.envoy.service.ext_proc.v3.CommonResponse
import io.envoyproxy.envoy.service.ext_proc.v3.HeaderMutation
import io.envoyproxy.envoy.service.ext_proc.v3.HeadersResponse
import io.envoyproxy.envoy.service.ext_proc.v3.ProcessingRequest
import io.envoyproxy.envoy.service.ext_proc.v3.ProcessingResponse
import org.springframework.stereotype.Service

@Service
class BaseFilter : AbstractEnvoyExtFilter() {
    override fun onRequestHeaders(
        request: ProcessingRequest,
        response: ProcessingResponse.Builder,
        next: ExtFilterChain
    ) {
        next.doFilter(
            request,
            response.setRequestHeaders(
                HeadersResponse.newBuilder()
                    .setResponse(CommonResponse.newBuilder().setHeaderMutation(HeaderMutation.newBuilder()))
            )
        )
    }

    override fun onResponseHeaders(
        request: ProcessingRequest,
        response: ProcessingResponse.Builder,
        chain: ExtFilterChain
    ) {
        chain.doFilter(
            request,
            response.setResponseHeaders(
                HeadersResponse.newBuilder()
                    .setResponse(CommonResponse.newBuilder().setHeaderMutation(HeaderMutation.newBuilder()))
            )
        )
    }
}