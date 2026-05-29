package com.vitral.auth.infraestructure.driver_adapters.jpa_repository;

import com.vitral.auth.domain.model.Usuario;
import com.vitral.auth.infraestructure.mapper.UsuarioMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioDataGatewayImplTest {

    @Mock
    private UsuarioDataJpaRepository repository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioDataGatewayImpl gateway;

    @Test
    void guardarUsuario_debeMapearYGuardar() {
        Usuario usuario = crearUsuario();
        UsuarioData usuarioData = crearUsuarioData();
        when(usuarioMapper.toUsuarioData(usuario)).thenReturn(usuarioData);
        when(repository.save(usuarioData)).thenReturn(usuarioData);
        when(usuarioMapper.toUsuario(usuarioData)).thenReturn(usuario);

        Usuario resultado = gateway.guardarUsuario(usuario);

        assertEquals(usuario, resultado);
        verify(repository).save(usuarioData);
    }

    @Test
    void actualizarUsuario_debeRetornarNullSiNoExiste() {
        Usuario usuario = crearUsuario();
        when(repository.existsById("123")).thenReturn(false);

        Usuario resultado = gateway.actualizarUsuario(usuario);

        assertNull(resultado);
        verify(repository, never()).save(any());
    }

    @Test
    void actualizarUsuario_debeGuardarSiExiste() {
        Usuario usuario = crearUsuario();
        UsuarioData usuarioData = crearUsuarioData();
        when(repository.existsById("123")).thenReturn(true);
        when(usuarioMapper.toUsuarioData(usuario)).thenReturn(usuarioData);
        when(repository.save(usuarioData)).thenReturn(usuarioData);
        when(usuarioMapper.toUsuario(usuarioData)).thenReturn(usuario);

        Usuario resultado = gateway.actualizarUsuario(usuario);

        assertEquals(usuario, resultado);
        verify(repository).save(usuarioData);
    }

    @Test
    void buscarUsuarioPorCedula_debeMapearSiExiste() {
        UsuarioData usuarioData = crearUsuarioData();
        Usuario usuario = crearUsuario();
        when(repository.findById("123")).thenReturn(Optional.of(usuarioData));
        when(usuarioMapper.toUsuario(usuarioData)).thenReturn(usuario);

        Usuario resultado = gateway.buscarUsuarioPorCedula("123");

        assertEquals(usuario, resultado);
    }

    @Test
    void buscarUsuarioPorCedula_debeRetornarNullSiNoExiste() {
        when(repository.findById("123")).thenReturn(Optional.empty());

        Usuario resultado = gateway.buscarUsuarioPorCedula("123");

        assertNull(resultado);
    }

    @Test
    void eliminarUsuarioPorCedula_debeInvocarDeleteById() {
        gateway.eliminarUsuarioPorCedula("123");

        verify(repository).deleteById("123");
    }

    @Test
    void buscarUsuarioPorCorreo_debeRetornarNullSiNoExiste() {
        when(repository.findByCorreo("ana@mail.com")).thenReturn(null);

        Usuario resultado = gateway.buscarUsuarioPorCorreo("ana@mail.com");

        assertNull(resultado);
    }

    @Test
    void buscarUsuarioPorCorreo_debeMapearSiExiste() {
        UsuarioData usuarioData = crearUsuarioData();
        Usuario usuario = crearUsuario();
        when(repository.findByCorreo("ana@mail.com")).thenReturn(usuarioData);
        when(usuarioMapper.toUsuario(usuarioData)).thenReturn(usuario);

        Usuario resultado = gateway.buscarUsuarioPorCorreo("ana@mail.com");

        assertEquals(usuario, resultado);
    }

    private Usuario crearUsuario() {
        return new Usuario("123", "Ana", "ana@mail.com", "enc-1234", 25, "3001234567", "USER", "barberia");
    }

    private UsuarioData crearUsuarioData() {
        return new UsuarioData("123", "Ana", "ana@mail.com", "enc-1234", 25, "3001234567", "USER", "barberia");
    }
}
