package com.example.envoyj

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Envoyj {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Envoyj::class.java)
        }
    }
}