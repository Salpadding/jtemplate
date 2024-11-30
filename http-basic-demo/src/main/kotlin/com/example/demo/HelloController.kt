package com.example.demo

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController(val repo: SecurityContextRepository, val req: HttpServletRequest) {
    @GetMapping("/hello")
    fun hello(): String {
        return "hello"
    }

    @GetMapping("/public/login")
    fun login(): String {
        return "please login"
    }

    @GetMapping("/private/hello")
    fun privateHello(): String {
        return "hello"
    }

    @PostMapping("/hello")
    fun createHello(): Any {
        return mapOf("ok" to true)
    }

    @GetMapping("/session")
    fun session(): Any {
        return repo.loadDeferredContext(req).get().authentication
    }
}