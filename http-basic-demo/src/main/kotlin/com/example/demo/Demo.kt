package com.example.demo

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.SecurityBuilder
import org.springframework.security.config.annotation.SecurityConfigurer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.security.web.firewall.FirewalledRequest
import org.springframework.security.web.firewall.HttpFirewall
import org.springframework.security.web.util.matcher.AnyRequestMatcher
import org.springframework.util.ReflectionUtils

@SpringBootApplication
@EnableWebSecurity(debug = true)
class Demo {
    // disable firewall
    @Bean
    fun firewall(): HttpFirewall {
        return object : HttpFirewall {
            override fun getFirewalledRequest(request: HttpServletRequest?): FirewalledRequest {
                return object : FirewalledRequest(request) {
                    override fun reset() {

                    }
                }
            }

            override fun getFirewalledResponse(response: HttpServletResponse?): HttpServletResponse {
                return response!!
            }
        }
    }

    @Bean
    // session scope security context
    // use spring session if persistence is required
    fun securityContextRepo(): SecurityContextRepository {
        return HttpSessionSecurityContextRepository()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val clazz = SecurityContextRepository::class.java
        http.setSharedObject(clazz, securityContextRepo())

        http
            .securityMatcher(AnyRequestMatcher.INSTANCE)
            // cors is controlled by external proxy
            .cors { it.disable() }
            // csrf is controlled by external proxy
            .csrf { it.disable() }
            // headers are controlled by external proxy
            .headers { it.disable() }
            // multiple session is permitted
            .sessionManagement {
                it.disable()
            }
            // http basic authentication
            .httpBasic {  }
            // reserve session when logout, since logout filter will save empty context
            .logout { it.invalidateHttpSession(false) }
            // replace default per-request scoped repository
            .formLogin { }
            .authorizeHttpRequests {
                it.requestMatchers({ req ->
                    req.requestURI.startsWith("/private")
                }).authenticated().anyRequest().permitAll()
            }

        return http.build()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Demo::class.java, *args)
        }
    }
}