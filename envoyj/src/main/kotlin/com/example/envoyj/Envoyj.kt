package com.example.envoyj

import org.springframework.boot.SpringApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan
class Envoyj(val ctx: ApplicationContext) {
    init {
        context = ctx
    }

    companion object {
        @JvmStatic
        var context: ApplicationContext? = null

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Envoyj::class.java)
            context?.getBean(GrpcServer::class.java)?.server?.awaitTermination()
        }
    }
}