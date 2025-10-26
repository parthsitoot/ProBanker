package com.banking.proBanker.Config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    public static final String[] PUBLIC_URLS = {
            "/api/users/register",
            "/api/users/login",
            "/api/auth/password-reset/verify-otp",
            "/api/auth/password-reset/send-otp",
            "/api/auth/password-reset",
            "/api/users/generate-otp",
            "/api/users/verify-otp",
            "swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/actuator/**"
    };


}
