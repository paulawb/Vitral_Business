package com.ecommerce.crud.infraestructure.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void manejarProductoNoEncontrado_debeRetornarNotFound() {
        ResponseEntity<ErrorResponse> response = handler.manejarProductoNoEncontrado(
                new ProductoNoEncontradoException("no encontrado")
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("no encontrado", response.getBody().getMensaje());
    }

    @Test
    void manejarValidacion_debeRetornarBadRequest() {
        ResponseEntity<ErrorResponse> response = handler.manejarValidacion(
                new ValidacionException("dato invalido")
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("dato invalido", response.getBody().getMensaje());
    }

    @Test
    void manejarJsonInvalido_debeRetornarBadRequest() {
        ResponseEntity<ErrorResponse> response = handler.manejarJsonInvalido(
                new HttpMessageNotReadableException("json invalido", mock(HttpInputMessage.class))
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Formato de datos invalido en la peticion", response.getBody().getMensaje());
    }

    @Test
    void manejarTipoInvalido_debeRetornarBadRequest() {
        MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
                "abc", Long.class, "id", null, new IllegalArgumentException("error")
        );

        ResponseEntity<ErrorResponse> response = handler.manejarTipoInvalido(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Tipo de dato invalido para el parametro: id", response.getBody().getMensaje());
    }

    @Test
    void manejarErrorGeneral_debeRetornarInternalServerError() {
        ResponseEntity<ErrorResponse> response = handler.manejarErrorGeneral(new RuntimeException("error"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Ocurrio un error interno", response.getBody().getMensaje());
    }
}
