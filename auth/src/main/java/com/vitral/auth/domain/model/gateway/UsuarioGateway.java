package com.vitral.auth.domain.model.gateway;

import com.vitral.auth.domain.model.Usuario;

public interface UsuarioGateway {

    Usuario guardarUsuario(Usuario usuario);

    Usuario actualizarUsuario(Usuario usuario);

    Usuario buscarUsuarioPorCedula(String cedula);

    void eliminarUsuarioPorCedula(String cedula);

    Usuario buscarUsuarioPorCorreo(String correo);

    Usuario buscarUsuarioPorRefreshToken(String refreshToken);

    Usuario asignarTenant(String cedula, String tenantId);
}
