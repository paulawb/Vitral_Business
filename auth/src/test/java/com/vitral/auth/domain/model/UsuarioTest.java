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
        usuario.setEdad(25);
        usuario.setTelefono("3001234567");
        usuario.setRol("USER");
        usuario.setTipoNegocio("Barberia");

        assertEquals("123", usuario.getCedula());
        assertEquals("Ana", usuario.getNombres());
        assertEquals("ana@mail.com", usuario.getCorreo());
        assertEquals("1234", usuario.getPassword());
        assertEquals(25, usuario.getEdad());
        assertEquals("3001234567", usuario.getTelefono());
        assertEquals("USER", usuario.getRol());
        assertEquals("Barberia", usuario.getTipoNegocio());
    }

    @Test
    void constructorCompleto_debeAsignarValores() {

        Usuario usuario = new Usuario(
                "123",
                "Ana",
                "ana@mail.com",
                "1234",
                25,
                "3001234567",
                "USER",
                "Barberia"
        );

        assertEquals("123", usuario.getCedula());
        assertEquals("Ana", usuario.getNombres());
        assertEquals("ana@mail.com", usuario.getCorreo());
        assertEquals("1234", usuario.getPassword());
        assertEquals(25, usuario.getEdad());
        assertEquals("3001234567", usuario.getTelefono());
        assertEquals("USER", usuario.getRol());
        assertEquals("Barberia", usuario.getTipoNegocio());
    }

    @Test
    void constructorVacio_debeIniciarConNulls() {

        Usuario usuario = new Usuario();

        assertNull(usuario.getCedula());
        assertNull(usuario.getNombres());
        assertNull(usuario.getCorreo());
        assertNull(usuario.getPassword());
        assertNull(usuario.getEdad());
        assertNull(usuario.getTelefono());
        assertNull(usuario.getRol());
        assertNull(usuario.getTipoNegocio());
    }
}