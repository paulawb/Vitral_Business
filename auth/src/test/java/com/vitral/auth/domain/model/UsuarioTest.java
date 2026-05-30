package com.vitral.auth.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UsuarioTest {

    @Test
    void constructorVacioYSetters_debenAsignarValores() {

        Usuario usuario = new Usuario();

        usuario.setCedula("123");
        usuario.setNombres("Ana");
        usuario.setCorreo("ana@mail.com");
        usuario.setPassword("1234");
        usuario.setTelefono("3001234567");

        assertEquals("123", usuario.getCedula());
        assertEquals("Ana", usuario.getNombres());
        assertEquals("ana@mail.com", usuario.getCorreo());
        assertEquals("1234", usuario.getPassword());
        assertEquals("3001234567", usuario.getTelefono());
    }

    @Test
    void constructorCompleto_debeAsignarValores() {

        Usuario usuario = new Usuario(
                "123",
                "Ana",
                "ana@mail.com",
                "1234",
                "3001234567"
        );

        assertEquals("123", usuario.getCedula());
        assertEquals("Ana", usuario.getNombres());
        assertEquals("ana@mail.com", usuario.getCorreo());
        assertEquals("1234", usuario.getPassword());
        assertEquals("3001234567", usuario.getTelefono());
    }

    @Test
    void constructorVacio_debeIniciarConNulls() {

        Usuario usuario = new Usuario();

        assertNull(usuario.getCedula());
        assertNull(usuario.getNombres());
        assertNull(usuario.getCorreo());
        assertNull(usuario.getPassword());
        assertNull(usuario.getTelefono());
    }
}