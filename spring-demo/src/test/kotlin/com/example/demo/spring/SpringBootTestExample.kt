package com.example.demo.spring

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [SpringBootTestExample.Config::class])
class SpringBootTestExample {
    class Config {

    }

    @Test
    fun test() {

    }
}