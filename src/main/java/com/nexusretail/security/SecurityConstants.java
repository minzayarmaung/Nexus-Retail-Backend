package com.nexusretail.security;

public class SecurityConstants {

    public static final String[] WHITELIST = {
            "/nexusretail/api/v1/auth/login",
            "/nexusretail/api/v1/configuration/code-values/dropdown/**",
            "/nexusretail/api/v1/configuration/**",

            // Swagger / OpenAPI v3
            "/v3/api-docs/**",
            "/v3/api-docs",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/api-docs/**",
            "/api-docs"
    };

    private SecurityConstants(){

    }
}
