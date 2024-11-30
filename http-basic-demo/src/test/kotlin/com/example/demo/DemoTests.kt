package com.example.demo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.security.config.annotation.ObjectPostProcessor
import kotlin.test.assertNotNull

@SpringBootTest
class DemoTests {

    class Inner {
        @Autowired
        lateinit var ctx: ApplicationContext
    }

    @Autowired
    lateinit var  objectPostProcessor: ObjectPostProcessor<Any>

    @Autowired
    lateinit var ctx: ApplicationContext

    @Test
    fun testPostProcessor() {
        val inner = Inner()
        objectPostProcessor.postProcess(inner)
        assertNotNull(inner.ctx)
    }
}