package com.vitral.auth.infraestructure.mapper;

import com.vitral.auth.domain.model.Usuario;
import com.vitral.auth.infraestructure.driver_adapters.jpa_repository.UsuarioData;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toUsuario(UsuarioData usuarioData) {
        if (usuarioData == null) {
            return null;
        }

        return Usuario.builder()
                .uuid(usuarioData.getUuid())
                .cedula(usuarioData.getCedula())
                .nombres(usuarioData.getNombres())
                .apellidos(usuarioData.getApellidos())
                .correo(usuarioData.getCorreo())
                .password(usuarioData.getPassword())
                .edad(usuarioData.getEdad())
                .telefono(usuarioData.getTelefono())
                .fotoPerfil(usuarioData.getFotoPerfil())
                .rol(usuarioData.getRol())
                .estado(usuarioData.getEstado())
                .emailVerificado(usuarioData.getEmailVerificado())
                .telefonoVerificado(usuarioData.getTelefonoVerificado())
                .ultimoLogin(usuarioData.getUltimoLogin())
                .tenantId(usuarioData.getTenantId())
                .providerAuth(usuarioData.getProviderAuth())
                .intentosFallidos(usuarioData.getIntentosFallidos())
                .bloqueadoHasta(usuarioData.getBloqueadoHasta())
                .tokenRecuperacion(usuarioData.getTokenRecuperacion())
                .activo(usuarioData.getActivo())
                .fechaActualizacion(usuarioData.getFechaActualizacion())
                .tipoNegocio(usuarioData.getTipoNegocio())
                .refreshToken(usuarioData.getRefreshToken())
                .build();
    }

    public UsuarioData toUsuarioData(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return UsuarioData.builder()
                .uuid(usuario.getUuid())
                .cedula(usuario.getCedula())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .correo(usuario.getCorreo())
                .password(usuario.getPassword())
                .edad(usuario.getEdad())
                .telefono(usuario.getTelefono())
                .fotoPerfil(usuario.getFotoPerfil())
                .rol(usuario.getRol())
                .estado(usuario.getEstado())
                .emailVerificado(usuario.getEmailVerificado())
                .telefonoVerificado(usuario.getTelefonoVerificado())
                .ultimoLogin(usuario.getUltimoLogin())
                .tenantId(usuario.getTenantId())
                .providerAuth(usuario.getProviderAuth())
                .intentosFallidos(usuario.getIntentosFallidos())
                .bloqueadoHasta(usuario.getBloqueadoHasta())
                .tokenRecuperacion(usuario.getTokenRecuperacion())
                .activo(usuario.getActivo())
                .fechaActualizacion(usuario.getFechaActualizacion())
                .tipoNegocio(resolveTipoNegocio(usuario))
                .refreshToken(usuario.getRefreshToken())
                .build();
    }

    private String resolveTipoNegocio(Usuario usuario) {
        if ("barberia".equalsIgnoreCase(usuario.getTipoNegocio())) {
            return usuario.getRol();
        }
        return usuario.getTipoNegocio();
    }
}
