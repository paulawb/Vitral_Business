package com.vitral.auth.infraestructure.driver_adapters.jpa_repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UsuarioDataTest {

    @Test
    void constructorVacioYSetters_debenAsignarValores() {
        UsuarioData usuarioData = new UsuarioData();

        usuarioData.setCedula("123");
        usuarioData.setNombres("Ana");
        usuarioData.setCorreo("ana@mail.com");
        usuarioData.setPassword("1234");
        usuarioData.setTelefono("3001234567");

        assertEquals("123", usuarioData.getCedula());
        assertEquals("Ana", usuarioData.getNombres());
        assertEquals("ana@mail.com", usuarioData.getCorreo());
        assertEquals("1234", usuarioData.getPassword());
        assertEquals("3001234567", usuarioData.getTelefono());
    }

    @Test
    void constructorCompleto_debeAsignarValores() {
        UsuarioData usuarioData = new UsuarioData("123", "Ana", "ana@mail.com", "1234", "3001234567");

        assertEquals("123", usuarioData.getCedula());
        assertEquals("Ana", usuarioData.getNombres());
        assertEquals("ana@mail.com", usuarioData.getCorreo());
        assertEquals("1234", usuarioData.getPassword());
        assertEquals("3001234567", usuarioData.getTelefono());
    }

    @Test
    void constructorVacio_debeIniciarConNulls() {
        UsuarioData usuarioData = new UsuarioData();

        assertNull(usuarioData.getCedula());
        assertNull(usuarioData.getNombres());
        assertNull(usuarioData.getCorreo());
        assertNull(usuarioData.getPassword());
        assertNull(usuarioData.getTelefono());
    }
}
