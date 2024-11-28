package com.example.auth

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Auth {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Auth::class.java, *args);
        }
    }
}