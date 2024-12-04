package com.example.demo.spring

import org.junit.jupiter.api.Test
import org.springframework.context.support.GenericApplicationContext

class BasicTestKt {

    @Test
    fun test0() {
        val ctx = GenericApplicationContext()
        val classFile = ctx.getResource("classpath:${this.javaClass.name.replace(".", "/")}.class")
        println(classFile.file.name)
        ctx.refresh()
    }
}