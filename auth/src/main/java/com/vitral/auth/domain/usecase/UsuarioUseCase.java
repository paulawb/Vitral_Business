package com.vitral.auth.domain.usecase;

import com.vitral.auth.domain.model.Usuario;
import com.vitral.auth.domain.model.gateway.EncrypterGateway;
import com.vitral.auth.domain.model.gateway.UsuarioGateway;
import com.vitral.auth.infraestructure.entry_points.dto.AuthRequest;
import com.vitral.auth.infraestructure.entry_points.dto.AuthResponse;
import com.vitral.auth.infraestructure.security.InputSanitizer;
import com.vitral.auth.infraestructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioGateway usuarioGateway;
    private final EncrypterGateway encrypterGateway;

    public Usuario guardarUsuario(Usuario usuario) {
        if (esCampoVacio(usuario.getCorreo()) || esCampoVacio(usuario.getPassword())) {
            throw new NullPointerException("usuario o contrasena son incorrectas - guardarUsuario");
        }
        usuario.setUuid(valorPorDefecto(usuario.getUuid(), UUID.randomUUID().toString()));
        usuario.setCorreo(InputSanitizer.clean(usuario.getCorreo()));
        usuario.setNombres(InputSanitizer.clean(usuario.getNombres()));
        usuario.setApellidos(InputSanitizer.clean(usuario.getApellidos()));
        usuario.setTelefono(InputSanitizer.clean(usuario.getTelefono()));
        usuario.setRol(valorPorDefecto(usuario.getRol(), "TENANT_ADMIN"));
        usuario.setEstado(valorPorDefecto(usuario.getEstado(), "PENDING"));
        usuario.setProviderAuth(valorPorDefecto(usuario.getProviderAuth(), "LOCAL"));
        usuario.setTenantId(normalizarTenantId(usuario.getTenantId()));
        usuario.setEmailVerificado(valorBooleano(usuario.getEmailVerificado(), false));
        usuario.setTelefonoVerificado(valorBooleano(usuario.getTelefonoVerificado(), false));
        usuario.setActivo(valorBooleano(usuario.getActivo(), true));
        usuario.setIntentosFallidos(valorEntero(usuario.getIntentosFallidos(), 0));
        usuario.setFechaActualizacion(LocalDateTime.now());
        usuario.setPassword(encrypterGateway.encrypt(usuario.getPassword()));
        return usuarioGateway.guardarUsuario(usuario);
    }

    public Usuario actualizarUsuario(Usuario usuario) {
        if (esCampoVacio(usuario.getCedula())) {
            throw new IllegalArgumentException("cedula es incorrecta - actualizarUsuario");
        }
        if (esCampoVacio(usuario.getCorreo()) || esCampoVacio(usuario.getPassword())) {
            throw new NullPointerException("usuario o contrasena son incorrectas - actualizarUsuario");
        }
        usuario.setCorreo(InputSanitizer.clean(usuario.getCorreo()));
        usuario.setNombres(InputSanitizer.clean(usuario.getNombres()));
        usuario.setApellidos(InputSanitizer.clean(usuario.getApellidos()));
        usuario.setTelefono(InputSanitizer.clean(usuario.getTelefono()));
        usuario.setFechaActualizacion(LocalDateTime.now());
        usuario.setPassword(encrypterGateway.encrypt(usuario.getPassword()));
        return usuarioGateway.actualizarUsuario(usuario);
    }

    public Usuario buscarUsuarioPorCedula(String cedula) {
        try {
            return usuarioGateway.buscarUsuarioPorCedula(cedula);
        } catch (Exception error) {
            return Usuario.builder().build();
        }
    }

    public void eliminarUsuarioPorCedula(String cedula) {
        try {
            usuarioGateway.eliminarUsuarioPorCedula(cedula);
        } catch (Exception error) {
            // Compatibilidad con el comportamiento legado: no propagar la excepcion.
        }
    }

    public AuthResponse login(AuthRequest request) {
        String correo = InputSanitizer.clean(request.getCorreo());
        String password = request.getPassword();
        if (esCampoVacio(correo) || esCampoVacio(password)) {
            throw new IllegalArgumentException("Campos obligatorios");
        }

        Usuario usuario = usuarioGateway.buscarUsuarioPorCorreo(correo);
        if (usuario == null) {
            throw new IllegalArgumentException("Credenciales invalidas");
        }

        if (usuario.getBloqueadoHasta() != null && usuario.getBloqueadoHasta().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Usuario bloqueado temporalmente");
        }

        if (!encrypterGateway.matches(password, usuario.getPassword())) {
            int intentos = valorEntero(usuario.getIntentosFallidos(), 0) + 1;
            usuario.setIntentosFallidos(intentos);
            if (intentos >= 5) {
                usuario.setBloqueadoHasta(LocalDateTime.now().plusMinutes(15));
            }
            usuario.setFechaActualizacion(LocalDateTime.now());
            usuarioGateway.actualizarUsuario(usuario);
            throw new IllegalArgumentException("Credenciales invalidas");
        }

        usuario.setIntentosFallidos(0);
        usuario.setBloqueadoHasta(null);
        usuario.setUltimoLogin(LocalDateTime.now());
        usuario.setFechaActualizacion(LocalDateTime.now());
        usuario.setRefreshToken(JwtUtil.generateRefreshToken(usuario.getCorreo(), usuario.getTenantId(), usuario.getRol()));
        Usuario actualizado = usuarioGateway.actualizarUsuario(usuario);

        return AuthResponse.builder()
                .accessToken(JwtUtil.generateToken(actualizado.getCorreo(), actualizado.getTenantId(), actualizado.getRol()))
                .refreshToken(actualizado.getRefreshToken())
                .expiresInSeconds(JwtUtil.getExpirationTimeSeconds())
                .rol(actualizado.getRol())
                .tenantId(actualizado.getTenantId())
                .usuario(actualizado)
                .build();
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (esCampoVacio(refreshToken)) {
            throw new IllegalArgumentException("Refresh token obligatorio");
        }

        Usuario usuario = usuarioGateway.buscarUsuarioPorRefreshToken(refreshToken);
        if (usuario == null || JwtUtil.validateToken(refreshToken) == null) {
            throw new IllegalArgumentException("Refresh token invalido");
        }

        String nuevoRefreshToken = JwtUtil.generateRefreshToken(usuario.getCorreo(), usuario.getTenantId(), usuario.getRol());
        usuario.setRefreshToken(nuevoRefreshToken);
        usuario.setFechaActualizacion(LocalDateTime.now());
        Usuario actualizado = usuarioGateway.actualizarUsuario(usuario);

        return AuthResponse.builder()
                .accessToken(JwtUtil.generateToken(actualizado.getCorreo(), actualizado.getTenantId(), actualizado.getRol()))
                .refreshToken(actualizado.getRefreshToken())
                .expiresInSeconds(JwtUtil.getExpirationTimeSeconds())
                .rol(actualizado.getRol())
                .tenantId(actualizado.getTenantId())
                .usuario(actualizado)
                .build();
    }

    public void logout(String refreshToken) {
        if (esCampoVacio(refreshToken)) {
            return;
        }
        Usuario usuario = usuarioGateway.buscarUsuarioPorRefreshToken(refreshToken);
        if (usuario != null) {
            usuario.setRefreshToken(null);
            usuario.setFechaActualizacion(LocalDateTime.now());
            usuarioGateway.actualizarUsuario(usuario);
        }
    }

    public Usuario asignarTenant(String cedula, String tenantId) {
        if (esCampoVacio(cedula) || esCampoVacio(tenantId)) {
            throw new IllegalArgumentException("Cedula y tenant son obligatorios");
        }
        return usuarioGateway.asignarTenant(cedula, tenantId);
    }

    private boolean esCampoVacio(String valor) {
        return valor == null || valor.isBlank();
    }

    private String valorPorDefecto(String valor, String defecto) {
        return esCampoVacio(valor) ? defecto : valor;
    }

    private Boolean valorBooleano(Boolean valor, boolean defecto) {
        return valor == null ? defecto : valor;
    }

    private Integer valorEntero(Integer valor, int defecto) {
        return valor == null ? defecto : valor;
    }

    private String normalizarTenantId(String tenantId) {
        if (esCampoVacio(tenantId)) {
            return "UNASSIGNED";
        }
        return tenantId.trim();
    }
}
