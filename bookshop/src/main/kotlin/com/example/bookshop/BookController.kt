package com.example.bookshop

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BookController(val req: HttpServletRequest, val res: HttpServletResponse) {
    @GetMapping("/books")
    fun books(): Any {
        req.headerNames.toList().forEach {
            println("${it} = ${req.getHeader(it)}")
        }
        res.addHeader("x-svc-now", "1")
        res.addHeader("x-svc-now", "2")
        res.addHeader("x-user", "java")
        res.addHeader("x-user", "java")
        res.addHeader("x-user", "java")
        res.addHeader("x-user", "java")
        res.addHeader("x-user", "java")
        res.addHeader("x-user", "java")
        return arrayOf("1", "2", "3")
    }
}