package com.vitral.auth.infraestructure.entry_points;

import com.vitral.auth.domain.model.EmailMessage;
import com.vitral.auth.domain.model.Usuario;
import com.vitral.auth.domain.model.gateway.EmailGateway;
import com.vitral.auth.domain.usecase.UsuarioUseCase;
import com.vitral.auth.infraestructure.driver_adapters.jpa_repository.UsuarioData;
import com.vitral.auth.infraestructure.entry_points.dto.AuthRequest;
import com.vitral.auth.infraestructure.entry_points.dto.AuthResponse;
import com.vitral.auth.infraestructure.entry_points.dto.RefreshTokenRequest;
import com.vitral.auth.infraestructure.entry_points.dto.TenantAssignmentRequest;
import com.vitral.auth.infraestructure.mapper.UsuarioMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;
    private final UsuarioMapper usuarioMapper;
    private final EmailGateway emailGateway;

    @PostMapping({"/api/vitral/usuarios/guardar", "/api/v1/auth/register"})
    public ResponseEntity<Usuario> guardarUsuario(@Valid @RequestBody UsuarioData usuarioData) {
        try {
            Usuario usuarioValidadoGuardado = usuarioUseCase.guardarUsuario(usuarioMapper.toUsuario(usuarioData));
            return new ResponseEntity<>(usuarioValidadoGuardado, HttpStatus.OK);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Normalmente ocurre cuando hay constraints UNIQUE en BD (cedula/correo).
            // En vez de devolver un error genérico, lo convertimos en "ya existe".
            throw new IllegalStateException("El usuario (cedula/correo) ya existe");
        }
    }

    @PutMapping({"/api/vitral/usuarios/actualizar", "/api/v1/auth/users"})
    public ResponseEntity<Usuario> actualizarUsuario(@RequestBody UsuarioData usuarioData) {
        Usuario usuario = usuarioMapper.toUsuario(usuarioData);
        Usuario usuarioExistente = usuarioUseCase.buscarUsuarioPorCedula(usuario.getCedula());

        if (usuarioExistente == null || usuarioExistente.getCedula() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Usuario usuarioActualizado = usuarioUseCase.actualizarUsuario(usuario);
        return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
    }

    @GetMapping({"/api/vitral/usuarios/buscar/{cedula}", "/api/v1/auth/users/{cedula}"})
    public ResponseEntity<Usuario> obtenerUsuarioPorCedula(@PathVariable String cedula) {
        Usuario usuario = usuarioUseCase.buscarUsuarioPorCedula(cedula);

        if (usuario == null || usuario.getCedula() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @DeleteMapping({"/api/vitral/usuarios/eliminar/{cedula}", "/api/v1/auth/users/{cedula}"})
    public ResponseEntity<String> eliminarUsuario(@PathVariable String cedula) {
        Usuario usuario = usuarioUseCase.buscarUsuarioPorCedula(cedula);

        if (usuario == null || usuario.getCedula() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        usuarioUseCase.eliminarUsuarioPorCedula(cedula);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }

    @PostMapping({"/api/vitral/usuarios/login", "/api/v1/auth/login"})
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest data) {
        try {
            AuthResponse authResponse = usuarioUseCase.login(data);

            String tenantId = authResponse.getTenantId();
            String fecha = java.time.LocalDateTime.now().toString();

            EmailMessage email = new EmailMessage(
                    data.getCorreo(),
                    "ConfirmaciÃƒÂ³n de inicio de sesiÃƒÂ³n",
                    "Hola, tu inicio de sesiÃƒÂ³n fue exitoso.\nTenant: " + tenantId + "\nFecha: " + fecha
            );

            emailGateway.sendEmail(email);

            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/api/v1/auth/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest data) {
        return new ResponseEntity<>(usuarioUseCase.refreshToken(data.getRefreshToken()), HttpStatus.OK);
    }

    @PostMapping("/api/v1/auth/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest data) {
        usuarioUseCase.logout(data.getRefreshToken());
        return ResponseEntity.ok("Sesion cerrada correctamente");
    }

    @PostMapping("/api/v1/auth/users/{cedula}/tenant")
    public ResponseEntity<Usuario> assignTenant(@PathVariable String cedula, @Valid @RequestBody TenantAssignmentRequest request) {
        Usuario usuario = usuarioUseCase.asignarTenant(cedula, request.getTenantId());
        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/api/v1/auth/notifications/email")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailMessage email) {
        emailGateway.sendEmail(email);
        return ResponseEntity.ok("Correo enviado correctamente");
    }
}
