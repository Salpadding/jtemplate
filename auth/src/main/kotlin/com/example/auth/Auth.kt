package com.example.auth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.firewall.FirewalledRequest
import org.springframework.security.web.firewall.HttpFirewall
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class Auth(val ctx: ApplicationContext): ApplicationListener<ApplicationReadyEvent>{
    companion object {
        @JvmStatic
        val log = LoggerFactory.getLogger(Auth::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            DisableSSLVerification.main(args)
            SpringApplication.run(Auth::class.java, *args)
        }
    }

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        try {
            val settings = ctx.getBean(AuthorizationServerSettings::class.java)
            log.info("settings = {}", settings)
        } catch (e: Exception) {

        }

    }

    @Profile("mix", "client")
    @RestController
    class HelloController {
        @GetMapping("/hello")
        fun hello(): String {
            return "hello"
        }
        @GetMapping("/public/hello")
        fun pubHello(): String {
            return "hello"
        }
    }

    @Profile("mix")
    @Configuration
    class MixConfiguration {
        @Bean
        fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
            return http
                .cors { it.disable() }
                .headers { it.disable() }
                .csrf { it.disable() }
                .oauth2Login { }
                .formLogin { }
                .authorizeHttpRequests {
                    it.requestMatchers({ x -> x.requestURI.startsWith("/public") }).permitAll().anyRequest()
                        .authenticated()
                }
                .build()

        }
    }


    @Profile("client")
    @Configuration
    class ClientConfiguration {
        @Bean
        fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
            return http
                .cors { it.disable() }
                .headers { it.disable() }
                .csrf { it.disable() }
                .oauth2Login { }
                .authorizeHttpRequests {
                    it.requestMatchers({ x -> x.requestURI.startsWith("/public") }).permitAll().anyRequest()
                        .authenticated()
                }
                .build()

        }
    }


    @Profile("mix")
    @Bean
    fun firewall(): HttpFirewall {
        return object : HttpFirewall {
            override fun getFirewalledRequest(request: HttpServletRequest?): FirewalledRequest {
                return object : FirewalledRequest(request) {
                    override fun reset() {

                    }
                    override fun getScheme(): String {
                        return "https"
                    }
                }
            }

            override fun getFirewalledResponse(response: HttpServletResponse?): HttpServletResponse {
                return response!!
            }
        }
    }
}