package com.example.envoyj

import io.envoyproxy.envoy.service.ext_proc.v3.ExternalProcessorGrpc
import io.envoyproxy.envoy.service.ext_proc.v3.ProcessingRequest
import io.envoyproxy.envoy.service.ext_proc.v3.ProcessingResponse
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ExtProcService(val factory: FilterChainFactory) : ExternalProcessorGrpc.ExternalProcessorImplBase() {
    companion object {
        @JvmStatic
        val log = LoggerFactory.getLogger(ExtProcService::class.java)
    }

    override fun process(responseObserver: StreamObserver<ProcessingResponse>): StreamObserver<ProcessingRequest> {
        return Observer(responseObserver, this.factory)
    }

    class Observer(val client: StreamObserver<ProcessingResponse>, val factory: FilterChainFactory) :
        StreamObserver<ProcessingRequest> {
        override fun onNext(value: ProcessingRequest?) {
            if (value == null) return
            val res = ProcessingResponse.newBuilder()
            val chain = factory.create()
            factory.apply(chain, value, res)
            val processed = res.build()
            client.onNext(processed)
        }

        override fun onError(t: Throwable?) {
            log.error("grpc error")
            client.onCompleted()
        }

        override fun onCompleted() {
        }
    }
}