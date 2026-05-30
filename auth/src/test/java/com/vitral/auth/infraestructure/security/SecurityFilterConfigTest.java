package com.vitral.auth.infraestructure.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SecurityFilterConfigTest {

    @Test
    void jwtFilter_debeCrearBean() {
        SecurityFilterConfig config = new SecurityFilterConfig();

        assertNotNull(config.jwtFilter());
    }
}
