package com.vitral.auth.infraestructure.security;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class JwtUtilTest {

    @Test
    void constructorPrivado_debePoderInstanciarsePorCobertura() throws Exception {
        Constructor<JwtUtil> constructor = JwtUtil.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        JwtUtil instancia = constructor.newInstance();

        assertNotNull(instancia);
    }

    @Test
    void generateTokenYValidateToken_debenRetornarCorreoOriginal() {
        String token = JwtUtil.generateToken("ana@mail.com");

        assertNotNull(token);
        assertEquals("ana@mail.com", JwtUtil.validateToken(token));
    }

    @Test
    void validateToken_debeRetornarNullSiTokenEsInvalido() {
        assertNull(JwtUtil.validateToken("token-invalido"));
    }
}
