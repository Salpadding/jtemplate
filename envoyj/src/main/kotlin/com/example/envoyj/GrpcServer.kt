package com.example.envoyj

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class GrpcServer(val extProcService: ExtProcService, @Value("\${grpc.port}") port: Int) {

    companion object {
        val log = LoggerFactory.getLogger(GrpcServer::class.java)
    }

    val server: io.grpc.Server = io.grpc.ServerBuilder.forPort(port)
        .addService(extProcService).build()

    init {
        log.info("server start at port {}", port)
        this.server.start()
    }
}