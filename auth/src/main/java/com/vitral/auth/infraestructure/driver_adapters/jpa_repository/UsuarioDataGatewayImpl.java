package com.vitral.auth.infraestructure.driver_adapters.jpa_repository;

import com.vitral.auth.domain.model.Usuario;
import com.vitral.auth.domain.model.gateway.UsuarioGateway;
import com.vitral.auth.infraestructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UsuarioDataGatewayImpl implements UsuarioGateway {

    private final UsuarioDataJpaRepository repository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        UsuarioData usuarioData = usuarioMapper.toUsuarioData(usuario);
        return usuarioMapper.toUsuario(repository.save(usuarioData));
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        if (!repository.existsById(usuario.getCedula())) {
            return null;
        }

        UsuarioData usuarioData = usuarioMapper.toUsuarioData(usuario);
        return usuarioMapper.toUsuario(repository.save(usuarioData));
    }

    @Override
    public Usuario buscarUsuarioPorCedula(String cedula) {
        return repository.findById(cedula)
                .map(usuarioMapper::toUsuario)
                .orElse(null);
    }

    @Override
    public void eliminarUsuarioPorCedula(String cedula) {
        repository.deleteById(cedula);
    }

    @Override
    public Usuario buscarUsuarioPorCorreo(String correo) {
        UsuarioData data = repository.findByCorreo(correo);

        if (data == null) {
            return null;
        }

        return usuarioMapper.toUsuario(data);
    }

    @Override
    public Usuario buscarUsuarioPorRefreshToken(String refreshToken) {
        return repository.findByRefreshToken(refreshToken)
                .map(usuarioMapper::toUsuario)
                .orElse(null);
    }

    @Override
    public Usuario asignarTenant(String cedula, String tenantId) {
        UsuarioData usuarioData = repository.findById(cedula).orElse(null);
        if (usuarioData == null) {
            return null;
        }
        usuarioData.setTenantId(tenantId);
        return usuarioMapper.toUsuario(repository.save(usuarioData));
    }
}
