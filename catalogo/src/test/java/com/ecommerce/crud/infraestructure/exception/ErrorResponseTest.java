package com.ecommerce.crud.infraestructure.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorResponseTest {

    @Test
    void constructorYSetters_debenAsignarValores() {
        ErrorResponse errorResponse = new ErrorResponse(400, "mensaje");

        assertEquals(400, errorResponse.getStatus());
        assertEquals("mensaje", errorResponse.getMensaje());

        errorResponse.setStatus(404);
        errorResponse.setMensaje("otro");

        assertEquals(404, errorResponse.getStatus());
        assertEquals("otro", errorResponse.getMensaje());
    }
}
