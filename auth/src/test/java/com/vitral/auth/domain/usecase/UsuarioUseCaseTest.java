package com.vitral.auth.domain.usecase;

import com.vitral.auth.domain.model.Usuario;
import com.vitral.auth.domain.model.gateway.EncrypterGateway;
import com.vitral.auth.domain.model.gateway.UsuarioGateway;
import com.vitral.auth.infraestructure.entry_points.dto.AuthRequest;
import com.vitral.auth.infraestructure.entry_points.dto.AuthResponse;
import com.vitral.auth.infraestructure.entry_points.dto.RefreshTokenRequest;
import com.vitral.auth.infraestructure.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    private static final String CEDULA_VALIDA = "123";
    private static final String CORREO_VALIDO = "ana@mail.com";
    private static final String PASSWORD_VALIDO = "1234";
    private static final String PASSWORD_ENCRIPTADO = "enc-1234";
    private static final String TEXTO_EN_BLANCO = "   ";
    private static final String REFRESH_TOKEN = "refresh-token-123";

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private EncrypterGateway encrypterGateway;

    @InjectMocks
    private UsuarioUseCase usuarioUseCase;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .cedula(CEDULA_VALIDA)
                .nombres("Ana")
                .apellidos("Perez")
                .correo(CORREO_VALIDO)
                .password(PASSWORD_VALIDO)
                .telefono("3001234567")
                .rol("TENANT_ADMIN")
                .tenantId("UNASSIGNED")
                .build();
    }

    @Test
    void guardarUsuario_debeEncriptarPasswordYGuardarUsuario() {
        when(encrypterGateway.encrypt(PASSWORD_VALIDO)).thenReturn(PASSWORD_ENCRIPTADO);
        when(usuarioGateway.guardarUsuario(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = usuarioUseCase.guardarUsuario(usuario);

        assertEquals(PASSWORD_ENCRIPTADO, resultado.getPassword());
        verify(encrypterGateway).encrypt(PASSWORD_VALIDO);
        verify(usuarioGateway).guardarUsuario(usuario);
    }

    @Test
    void guardarUsuario_debeFallarSiCorreoEsNull() {
        usuario.setCorreo(null);

        NullPointerException error = assertThrows(
                NullPointerException.class,
                () -> usuarioUseCase.guardarUsuario(usuario)
        );

        assertEquals("usuario o contrasena son incorrectas - guardarUsuario", error.getMessage());
        verifyNoInteractions(usuarioGateway, encrypterGateway);
    }

    @Test
    void guardarUsuario_debeFallarSiCorreoEsEnBlanco() {
        usuario.setCorreo(TEXTO_EN_BLANCO);

        NullPointerException error = assertThrows(
                NullPointerException.class,
                () -> usuarioUseCase.guardarUsuario(usuario)
        );

        assertEquals("usuario o contrasena son incorrectas - guardarUsuario", error.getMessage());
        verifyNoInteractions(usuarioGateway, encrypterGateway);
    }

    @Test
    void guardarUsuario_debeFallarSiPasswordEsNull() {
        usuario.setPassword(null);

        NullPointerException error = assertThrows(
                NullPointerException.class,
                () -> usuarioUseCase.guardarUsuario(usuario)
        );

        assertEquals("usuario o contrasena son incorrectas - guardarUsuario", error.getMessage());
        verifyNoInteractions(usuarioGateway, encrypterGateway);
    }

    @Test
    void guardarUsuario_debeFallarSiPasswordEsEnBlanco() {
        usuario.setPassword(TEXTO_EN_BLANCO);

        NullPointerException error = assertThrows(
                NullPointerException.class,
                () -> usuarioUseCase.guardarUsuario(usuario)
        );

        assertEquals("usuario o contrasena son incorrectas - guardarUsuario", error.getMessage());
        verifyNoInteractions(usuarioGateway, encrypterGateway);
    }

    @Test
    void actualizarUsuario_debeEncriptarPasswordYActualizarUsuario() {
        when(encrypterGateway.encrypt(PASSWORD_VALIDO)).thenReturn(PASSWORD_ENCRIPTADO);
        when(usuarioGateway.actualizarUsuario(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = usuarioUseCase.actualizarUsuario(usuario);

        assertEquals(PASSWORD_ENCRIPTADO, resultado.getPassword());
        verify(encrypterGateway).encrypt(PASSWORD_VALIDO);
        verify(usuarioGateway).actualizarUsuario(usuario);
    }

    @Test
    void actualizarUsuario_debeFallarSiCedulaEsNull() {
        usuario.setCedula(null);

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioUseCase.actualizarUsuario(usuario)
        );

        assertEquals("cedula es incorrecta - actualizarUsuario", error.getMessage());
        verifyNoInteractions(usuarioGateway, encrypterGateway);
    }

    @Test
    void actualizarUsuario_debeFallarSiCedulaEsVacia() {
        usuario.setCedula(TEXTO_EN_BLANCO);

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioUseCase.actualizarUsuario(usuario)
        );

        assertEquals("cedula es incorrecta - actualizarUsuario", error.getMessage());
        verifyNoInteractions(usuarioGateway, encrypterGateway);
    }

    @Test
    void buscarUsuarioPorCedula_debeRetornarUsuarioCuandoExiste() {
        when(usuarioGateway.buscarUsuarioPorCedula(CEDULA_VALIDA)).thenReturn(usuario);

        Usuario resultado = usuarioUseCase.buscarUsuarioPorCedula(CEDULA_VALIDA);

        assertNotNull(resultado);
        assertEquals(CEDULA_VALIDA, resultado.getCedula());
        verify(usuarioGateway).buscarUsuarioPorCedula(CEDULA_VALIDA);
    }

    @Test
    void buscarUsuarioPorCedula_debeRetornarUsuarioVacioSiGatewayFalla() {
        when(usuarioGateway.buscarUsuarioPorCedula(CEDULA_VALIDA)).thenThrow(new RuntimeException("error de busqueda"));

        Usuario resultado = usuarioUseCase.buscarUsuarioPorCedula(CEDULA_VALIDA);

        assertNotNull(resultado);
        assertNull(resultado.getCedula());
    }

    @Test
    void eliminarUsuarioPorCedula_debeEliminarUsuarioCuandoExiste() {
        assertDoesNotThrow(() -> usuarioUseCase.eliminarUsuarioPorCedula(CEDULA_VALIDA));

        verify(usuarioGateway).eliminarUsuarioPorCedula(CEDULA_VALIDA);
    }

    @Test
    void eliminarUsuarioPorCedula_noDebePropagarErrorSiGatewayFalla() {
        doThrow(new RuntimeException("error al eliminar")).when(usuarioGateway).eliminarUsuarioPorCedula(CEDULA_VALIDA);

        assertDoesNotThrow(() -> usuarioUseCase.eliminarUsuarioPorCedula(CEDULA_VALIDA));

        verify(usuarioGateway).eliminarUsuarioPorCedula(CEDULA_VALIDA);
    }

    @Test
    void login_debeRetornarAuthResponseCuandoLasCredencialesSonValidas() {
        usuario.setPassword(PASSWORD_ENCRIPTADO);
        when(usuarioGateway.buscarUsuarioPorCorreo(CORREO_VALIDO)).thenReturn(usuario);
        when(encrypterGateway.matches(PASSWORD_VALIDO, PASSWORD_ENCRIPTADO)).thenReturn(true);
        when(usuarioGateway.actualizarUsuario(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthRequest request = new AuthRequest();
        request.setCorreo(CORREO_VALIDO);
        request.setPassword(PASSWORD_VALIDO);

        AuthResponse respuesta = usuarioUseCase.login(request);

        assertNotNull(respuesta);
        assertNotNull(respuesta.getAccessToken());
        assertNotNull(respuesta.getRefreshToken());
        assertEquals(CORREO_VALIDO, JwtUtil.validateToken(respuesta.getAccessToken()));
        verify(usuarioGateway).buscarUsuarioPorCorreo(CORREO_VALIDO);
        verify(encrypterGateway).matches(PASSWORD_VALIDO, PASSWORD_ENCRIPTADO);
    }

    @Test
    void login_debeRetornarErrorSiUsuarioNoExiste() {
        when(usuarioGateway.buscarUsuarioPorCorreo(CORREO_VALIDO)).thenReturn(null);

        AuthRequest request = new AuthRequest();
        request.setCorreo(CORREO_VALIDO);
        request.setPassword(PASSWORD_VALIDO);

        assertThrows(
                IllegalArgumentException.class,
                () -> usuarioUseCase.login(request)
        );
        verify(usuarioGateway).buscarUsuarioPorCorreo(CORREO_VALIDO);
    }

    @Test
    void login_debeFallarSiCorreoEsNull() {
        AuthRequest request = new AuthRequest();
        request.setCorreo(null);
        request.setPassword(PASSWORD_VALIDO);

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioUseCase.login(request)
        );

        assertEquals("Campos obligatorios", error.getMessage());
        verifyNoInteractions(usuarioGateway, encrypterGateway);
    }

    @Test
    void login_debeFallarSiPasswordEsNull() {
        AuthRequest request = new AuthRequest();
        request.setCorreo(CORREO_VALIDO);
        request.setPassword(null);

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioUseCase.login(request)
        );

        assertEquals("Campos obligatorios", error.getMessage());
        verifyNoInteractions(usuarioGateway, encrypterGateway);
    }

    @Test
    void refreshToken_debeRetornarAuthResponseConTokenNuevo() {
        String validRefreshToken = JwtUtil.generateRefreshToken(CORREO_VALIDO, "UNASSIGNED", "TENANT_ADMIN");
        usuario.setRefreshToken(validRefreshToken);
        when(usuarioGateway.buscarUsuarioPorRefreshToken(validRefreshToken)).thenReturn(usuario);
        when(usuarioGateway.actualizarUsuario(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthResponse respuesta = usuarioUseCase.refreshToken(validRefreshToken);

        assertNotNull(respuesta);
        assertNotNull(respuesta.getAccessToken());
        assertNotNull(respuesta.getRefreshToken());
        // Verify the response contains the user data
        assertEquals(CORREO_VALIDO, JwtUtil.validateToken(respuesta.getAccessToken()));
    }

    @Test
    void refreshToken_debeFallarSiRefreshTokenEsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> usuarioUseCase.refreshToken(null)
        );
    }

    @Test
    void refreshToken_debeFallarSiRefreshTokenEsVacio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> usuarioUseCase.refreshToken("")
        );
    }

    @Test
    void refreshToken_debeFallarSiUsuarioNoExiste() {
        when(usuarioGateway.buscarUsuarioPorRefreshToken(REFRESH_TOKEN)).thenReturn(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> usuarioUseCase.refreshToken(REFRESH_TOKEN)
        );
    }

    @Test
    void logout_debeLimpiarRefreshToken() {
        usuario.setRefreshToken(REFRESH_TOKEN);
        when(usuarioGateway.buscarUsuarioPorRefreshToken(REFRESH_TOKEN)).thenReturn(usuario);
        when(usuarioGateway.actualizarUsuario(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(REFRESH_TOKEN);

        assertDoesNotThrow(() -> usuarioUseCase.logout(REFRESH_TOKEN));

        verify(usuarioGateway).buscarUsuarioPorRefreshToken(REFRESH_TOKEN);
        verify(usuarioGateway).actualizarUsuario(any(Usuario.class));
    }

    @Test
    void logout_debeHacerNadaSiRefreshTokenEsNull() {
        assertDoesNotThrow(() -> usuarioUseCase.logout(null));
        verify(usuarioGateway, never()).buscarUsuarioPorRefreshToken(anyString());
    }

    @Test
    void logout_debeHacerNadaSiUsuarioNoExiste() {
        when(usuarioGateway.buscarUsuarioPorRefreshToken(REFRESH_TOKEN)).thenReturn(null);

        assertDoesNotThrow(() -> usuarioUseCase.logout(REFRESH_TOKEN));
        verify(usuarioGateway).buscarUsuarioPorRefreshToken(REFRESH_TOKEN);
        verify(usuarioGateway, never()).actualizarUsuario(any());
    }

    @Test
    void asignarTenant_debeAsignarTenantAlUsuario() {
        when(usuarioGateway.asignarTenant(CEDULA_VALIDA, "nuevo-tenant")).thenReturn(usuario);

        Usuario resultado = usuarioUseCase.asignarTenant(CEDULA_VALIDA, "nuevo-tenant");

        assertNotNull(resultado);
        verify(usuarioGateway).asignarTenant(CEDULA_VALIDA, "nuevo-tenant");
    }

    @Test
    void asignarTenant_debeFallarSiCedulaEsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> usuarioUseCase.asignarTenant(null, "tenant")
        );
    }

    @Test
    void asignarTenant_debeFallarSiTenantEsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> usuarioUseCase.asignarTenant(CEDULA_VALIDA, null)
        );
    }
}
