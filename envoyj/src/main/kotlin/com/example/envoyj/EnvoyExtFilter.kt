package com.example.envoyj

import jakarta.servlet.Filter

interface EnvoyExtFilter: Filter {
    companion object {
        val ORDER_MAP: Map<Class<*>, Int> = mapOf(
            BaseFilter::class.java to 0,
            CookieCompressor::class.java to 1
        )
    }
}