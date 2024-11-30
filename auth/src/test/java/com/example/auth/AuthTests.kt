package com.example.auth

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.web.util.matcher.AnyRequestMatcher
import org.springframework.util.ReflectionUtils
import kotlin.test.assertEquals


@SpringBootTest
class AuthTests {
    @Autowired
    lateinit var http: HttpSecurity

    @Autowired
    lateinit var ctx: ApplicationContext

    // getOrApply is singleton
    @Test
    fun test0() {
        http.
            securityMatcher(AnyRequestMatcher.INSTANCE)

        http.csrf {

        }

        val configurer = AuthorizeHttpRequestsConfigurer<HttpSecurity>(ctx)
        val m = ReflectionUtils.findMethod(http.javaClass, "getOrApply", SecurityConfigurerAdapter::class.java)!!
        ReflectionUtils.makeAccessible(m)
        val ret = m.invoke(http, configurer)
        assertEquals(ret, configurer)
        assertEquals(m.invoke(http, AuthorizeHttpRequestsConfigurer(ctx)), configurer)
    }
}