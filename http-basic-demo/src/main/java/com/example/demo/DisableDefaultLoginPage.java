package com.example.demo;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;

public class DisableDefaultLoginPage {
    public static void disable(HttpSecurity http) {
        http.removeConfigurer(DefaultLoginPageConfigurer.class);
    }
}
