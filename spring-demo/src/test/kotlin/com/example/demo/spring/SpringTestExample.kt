package com.example.demo.spring

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [SpringTestExample.TestConfig::class])
class SpringTestExample {
    private lateinit var ctx: ApplicationContext

    @MockBean
    private lateinit var foo: Foo


    interface Foo {
        fun bar(): String
    }

    @Configuration
    class TestConfig {
        @Bean
        fun foo(): Foo {
            return object: Foo {
                override fun bar(): String {
                    return "bar"
                }
            }
        }
    }

    @Autowired
    fun setCtx(ctx: ApplicationContext) {
        this.ctx = ctx
    }

    @Test
    fun test1() {
        println(this.foo.bar())
        Mockito.`when`(this.foo.bar()).thenReturn("barbar")
        println(this.foo.bar())
        Mockito.`when`(this.foo.bar()).thenReturn("barbarbar")
        println(this.foo.bar())
    }


    @Test
    fun test2() {
        println(this.foo.bar())
    }

    @Test
    fun test3() {
        this.ctx.getBean(TestConfig::class.java)
        this.ctx.getBean(TestConfig::class.java)
        this.ctx.getBean(TestConfig::class.java)
    }
}