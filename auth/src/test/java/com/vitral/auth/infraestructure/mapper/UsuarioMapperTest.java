package com.vitral.auth.infraestructure.mapper;

import com.vitral.auth.domain.model.Usuario;
import com.vitral.auth.infraestructure.driver_adapters.jpa_repository.UsuarioData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsuarioMapperTest {

    private final UsuarioMapper mapper = new UsuarioMapper();

    @Test
    void toUsuario_debeMapearTodosLosCampos() {
        UsuarioData usuarioData = new UsuarioData("123", "Ana", "ana@mail.com", "1234", "3001234567");

        Usuario usuario = mapper.toUsuario(usuarioData);

        assertEquals("123", usuario.getCedula());
        assertEquals("Ana", usuario.getNombres());
        assertEquals("ana@mail.com", usuario.getCorreo());
        assertEquals("1234", usuario.getPassword());
        assertEquals("3001234567", usuario.getTelefono());
    }

    @Test
    void toUsuarioData_debeMapearTodosLosCampos() {
        Usuario usuario = new Usuario("123", "Ana", "ana@mail.com", "1234", "3001234567");

        UsuarioData usuarioData = mapper.toUsuarioData(usuario);

        assertEquals("123", usuarioData.getCedula());
        assertEquals("Ana", usuarioData.getNombres());
        assertEquals("ana@mail.com", usuarioData.getCorreo());
        assertEquals("1234", usuarioData.getPassword());
        assertEquals("3001234567", usuarioData.getTelefono());
    }
}
