package com.vitral.auth.infraestructure.entry_points;

import com.vitral.auth.domain.model.Usuario;
import com.vitral.auth.domain.usecase.UsuarioUseCase;
import com.vitral.auth.infraestructure.driver_adapters.jpa_repository.UsuarioData;
import com.vitral.auth.infraestructure.entry_points.dto.AuthRequest;
import com.vitral.auth.infraestructure.entry_points.dto.AuthResponse;
import com.vitral.auth.infraestructure.entry_points.dto.RefreshTokenRequest;
import com.vitral.auth.infraestructure.entry_points.dto.TenantAssignmentRequest;
import com.vitral.auth.infraestructure.mapper.UsuarioMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioUseCase usuarioUseCase;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioController controller;

    @Test
    void guardadusuario_debeRetornarOkConUsuarioGuardado() {
        UsuarioData usuarioData = crearUsuarioData();
        Usuario usuario = crearUsuario();
        when(usuarioMapper.toUsuario(usuarioData)).thenReturn(usuario);
        when(usuarioUseCase.guardarUsuario(usuario)).thenReturn(usuario);

        ResponseEntity<Usuario> response = controller.guardarUsuario(usuarioData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario, response.getBody());
    }

    @Test
    void actualizarUsuario_debeRetornarNotFoundSiNoExiste() {
        UsuarioData usuarioData = crearUsuarioData();
        Usuario usuario = crearUsuario();
        when(usuarioMapper.toUsuario(usuarioData)).thenReturn(usuario);
        when(usuarioUseCase.buscarUsuarioPorCedula("123")).thenReturn(null);

        ResponseEntity<Usuario> response = controller.actualizarUsuario(usuarioData);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(usuarioUseCase, never()).actualizarUsuario(any());
    }

    @Test
    void actualizarUsuario_debeRetornarNotFoundSiUsuarioExistenteNoTieneCedula() {
        UsuarioData usuarioData = crearUsuarioData();
        Usuario usuario = crearUsuario();
        Usuario usuarioSinCedula = crearUsuario();
        usuarioSinCedula.setCedula(null);
        when(usuarioMapper.toUsuario(usuarioData)).thenReturn(usuario);
        when(usuarioUseCase.buscarUsuarioPorCedula("123")).thenReturn(usuarioSinCedula);

        ResponseEntity<Usuario> response = controller.actualizarUsuario(usuarioData);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(usuarioUseCase, never()).actualizarUsuario(any());
    }

    @Test
    void actualizarUsuario_debeRetornarOkSiExiste() {
        UsuarioData usuarioData = crearUsuarioData();
        Usuario usuario = crearUsuario();
        when(usuarioMapper.toUsuario(usuarioData)).thenReturn(usuario);
        when(usuarioUseCase.buscarUsuarioPorCedula("123")).thenReturn(usuario);
        when(usuarioUseCase.actualizarUsuario(usuario)).thenReturn(usuario);

        ResponseEntity<Usuario> response = controller.actualizarUsuario(usuarioData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario, response.getBody());
    }

    @Test
    void obtenerUsuarioPorCedula_debeRetornarNotFoundSiNoExiste() {
        when(usuarioUseCase.buscarUsuarioPorCedula("123")).thenReturn(null);

        ResponseEntity<Usuario> response = controller.obtenerUsuarioPorCedula("123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void obtenerUsuarioPorCedula_debeRetornarNotFoundSiUsuarioNoTieneCedula() {
        Usuario usuario = crearUsuario();
        usuario.setCedula(null);
        when(usuarioUseCase.buscarUsuarioPorCedula("123")).thenReturn(usuario);

        ResponseEntity<Usuario> response = controller.obtenerUsuarioPorCedula("123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void obtenerUsuarioPorCedula_debeRetornarOkSiExiste() {
        Usuario usuario = crearUsuario();
        when(usuarioUseCase.buscarUsuarioPorCedula("123")).thenReturn(usuario);

        ResponseEntity<Usuario> response = controller.obtenerUsuarioPorCedula("123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario, response.getBody());
    }

    @Test
    void eliminarUsuario_debeRetornarNotFoundSiNoExiste() {
        when(usuarioUseCase.buscarUsuarioPorCedula("123")).thenReturn(null);

        ResponseEntity<String> response = controller.eliminarUsuario("123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Usuario no encontrado", response.getBody());
        verify(usuarioUseCase, never()).eliminarUsuarioPorCedula("123");
    }

    @Test
    void eliminarUsuario_debeRetornarNotFoundSiUsuarioNoTieneCedula() {
        Usuario usuario = crearUsuario();
        usuario.setCedula(null);
        when(usuarioUseCase.buscarUsuarioPorCedula("123")).thenReturn(usuario);

        ResponseEntity<String> response = controller.eliminarUsuario("123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Usuario no encontrado", response.getBody());
        verify(usuarioUseCase, never()).eliminarUsuarioPorCedula("123");
    }

    @Test
    void eliminarUsuario_debeRetornarOkSiExiste() {
        Usuario usuario = crearUsuario();
        when(usuarioUseCase.buscarUsuarioPorCedula("123")).thenReturn(usuario);

        ResponseEntity<String> response = controller.eliminarUsuario("123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario eliminado correctamente", response.getBody());
        verify(usuarioUseCase).eliminarUsuarioPorCedula("123");
    }

    @Test
    void login_debeRetornarOkConRespuestaDelUseCase() {
        AuthRequest request = new AuthRequest();
        request.setCorreo("ana@mail.com");
        request.setPassword("1234");

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken("token-access")
                .refreshToken("token-refresh")
                .expiresInSeconds(3600)
                .tenantId("tenant-123")
                .build();

        when(usuarioUseCase.login(request)).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = controller.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    void login_debeRetornarBadRequestSiUseCaseFallaValidacion() {
        AuthRequest request = new AuthRequest();
        request.setCorreo("ana@mail.com");
        request.setPassword("1234");

        when(usuarioUseCase.login(request))
                .thenThrow(new IllegalArgumentException("Campos obligatorios"));

        ResponseEntity<AuthResponse> response = controller.login(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void refresh_debeRetornarOkConRespuestaDelUseCase() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("refresh-token-123");

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken("nuevo-token-access")
                .refreshToken("nuevo-token-refresh")
                .expiresInSeconds(3600)
                .build();

        when(usuarioUseCase.refreshToken("refresh-token-123")).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = controller.refresh(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    void logout_debeRetornarOk() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("refresh-token-123");

        ResponseEntity<String> response = controller.logout(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Sesion cerrada correctamente", response.getBody());
        verify(usuarioUseCase).logout("refresh-token-123");
    }

    @Test
    void assignTenant_debeRetornarOkConUsuarioActualizado() {
        Usuario usuario = crearUsuario();
        TenantAssignmentRequest request = new TenantAssignmentRequest();
        request.setTenantId("nuevo-tenant");

        when(usuarioUseCase.asignarTenant("123", "nuevo-tenant")).thenReturn(usuario);

        ResponseEntity<Usuario> response = controller.assignTenant("123", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario, response.getBody());
    }

    @Test
    void assignTenant_debeRetornarNotFoundSiNoExiste() {
        TenantAssignmentRequest request = new TenantAssignmentRequest();
        request.setTenantId("nuevo-tenant");

        when(usuarioUseCase.asignarTenant("123", "nuevo-tenant")).thenReturn(null);

        ResponseEntity<Usuario> response = controller.assignTenant("123", request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private Usuario crearUsuario() {
        return new Usuario("123", "Ana", "ana@mail.com", "1234", "3001234567");
    }

    private UsuarioData crearUsuarioData() {
        return new UsuarioData("123", "Ana", "ana@mail.com", "1234", "3001234567");
    }
}
