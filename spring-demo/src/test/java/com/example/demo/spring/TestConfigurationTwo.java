package com.example.demo.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfigurationTwo {
    public TestConfigurationTwo() {
        System.out.println("");
    }

    public static class SomeBean {
    }

    @Bean
    public SomeBean someBean() {
        return new SomeBean();
    }
}
