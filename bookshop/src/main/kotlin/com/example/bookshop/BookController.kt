package com.example.bookshop

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BookController(val req: HttpServletRequest) {
    @GetMapping("/books")
    fun books(): Any {
        req.headerNames.toList().forEach {
            println("${it} = ${req.getHeader(it)}")
        }
        return arrayOf("1", "2", "3")
    }
}