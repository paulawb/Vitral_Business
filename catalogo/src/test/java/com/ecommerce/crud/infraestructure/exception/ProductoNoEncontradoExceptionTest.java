package com.ecommerce.crud.infraestructure.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductoNoEncontradoExceptionTest {

    @Test
    void constructor_debeGuardarMensaje() {
        ProductoNoEncontradoException exception = new ProductoNoEncontradoException("no encontrado");

        assertEquals("no encontrado", exception.getMessage());
    }
}
