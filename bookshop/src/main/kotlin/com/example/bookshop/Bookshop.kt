package com.example.bookshop

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Bookshop {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Bookshop::class.java, *args);
        }
    }
}