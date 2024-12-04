package com.example.mvc.demo

import org.slf4j.LoggerFactory
import org.springframework.boot.BootstrapContext
import org.springframework.boot.BootstrapRegistry
import org.springframework.boot.ConfigurableBootstrapContext
import org.springframework.boot.DefaultBootstrapContext
import org.springframework.boot.SpringApplication
import org.springframework.boot.env.EnvironmentPostProcessor
import org.springframework.boot.logging.DeferredLogFactory
import org.springframework.boot.logging.DeferredLogs
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.core.io.support.SpringFactoriesLoader

class Main {
    companion object {
        @JvmStatic
        val log = LoggerFactory.getLogger(Main::class.java)
        @JvmStatic
        fun main(args: Array<String>) {
            val ctx = AnnotationConfigApplicationContext()
            log.info("ctx = {}", ctx)
            log.info("env = {}", ctx.environment)

            // process environment
            val loader = SpringFactoriesLoader.forDefaultResourceLocation(Main.javaClass.classLoader)

            // pass arguments to class declared in spring.factories
            var argumentResolver = SpringFactoriesLoader.ArgumentResolver.of(DeferredLogFactory::class.java, DeferredLogs());
            val bootCtx = DefaultBootstrapContext()
            argumentResolver = argumentResolver.and(ConfigurableBootstrapContext::class.java, bootCtx)
            argumentResolver = argumentResolver.and(BootstrapContext::class.java, bootCtx)
            argumentResolver = argumentResolver.and(BootstrapRegistry::class.java, bootCtx)

            // search EnvironmentPostProcessor implementations under spring.factories
            val processors = loader.load(EnvironmentPostProcessor::class.java, argumentResolver)
            val app = SpringApplication(Main::class.java)

            // trigger processors
            processors.forEach {
                it.postProcessEnvironment(ctx.environment, app)
            }
            log.info("ok")
        }
    }
}