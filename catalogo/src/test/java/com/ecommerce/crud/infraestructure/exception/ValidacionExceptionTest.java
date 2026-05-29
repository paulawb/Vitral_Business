package com.ecommerce.crud.infraestructure.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidacionExceptionTest {

    @Test
    void constructor_debeGuardarMensaje() {
        ValidacionException exception = new ValidacionException("invalido");

        assertEquals("invalido", exception.getMessage());
    }
}
