package com.vitral.auth.infraestructure.security;

public class SecurityFilterConfig {

    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }
}
