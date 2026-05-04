package com.nexusretail.security;

public class SecurityConstants {

    public static final String[] WHITELIST = {
            // Authentication & OAuth
            "/api/v1/auth/login",

            // Configuration - Public dropdown options
            "/api/v1/configuration/code-values/dropdown/**",

            // Swagger & API Docs
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-resources",
            "/api-docs",
            "/api-docs/**"
    };

    private SecurityConstants(){

    }
}
