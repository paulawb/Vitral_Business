package com.vitral.auth.infraestructure.encrypter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EncrypterGatewayImplTest {

    @Test
    void encrypt_debeGenerarValorDiferenteAlOriginal() {
        EncrypterGatewayImpl encrypter = new EncrypterGatewayImpl();

        String encrypted = encrypter.encrypt("1234");

        assertNotEquals("1234", encrypted);
    }

    @Test
    void matches_debeValidarPasswordCorrectaEIncorrecta() {
        EncrypterGatewayImpl encrypter = new EncrypterGatewayImpl();
        String encrypted = encrypter.encrypt("1234");

        assertTrue(encrypter.matches("1234", encrypted));
        assertFalse(encrypter.matches("9999", encrypted));
    }
}
