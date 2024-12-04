package com.example.demo.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class BasicTest {
    public BasicTest() {
        System.out.println();
    }
    public static class ConfigFactory implements FactoryBean<Config> {
        public ConfigFactory() {
            System.out.println("");
        }

        @Override
        public Config getObject() throws Exception {
            return new Config();
        }

        @Override
        public Class<?> getObjectType() {
            return Config.class;
        }
    }

    public static class Config {
        public Config() {
            System.out.println();
        }
    }

    @Bean
    public ConfigFactory configFactory() {
        return new ConfigFactory();
    }

    @Test
    public void test() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(BasicTest.class);
        ctx.getBean(Config.class);
    }

    @Test
    public void test1() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(TestConfigurationTwo.class);
    }
}
