package com.vitral.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    private String uuid;
    private String cedula;
    private String nombres;
    private String apellidos;
    private String correo;
    private String password;
    private Integer edad;
    private String telefono;
    private String fotoPerfil;
    private String rol;
    private String estado;
    private Boolean emailVerificado;
    private Boolean telefonoVerificado;
    private LocalDateTime ultimoLogin;
    private String tenantId;
    private String providerAuth;
    private Integer intentosFallidos;
    private LocalDateTime bloqueadoHasta;
    private String tokenRecuperacion;
    private Boolean activo;
    private LocalDateTime fechaActualizacion;
    private String tipoNegocio;
    private String refreshToken;

    public Usuario(String cedula, String nombres, String correo, String password, Integer edad, String telefono, String rol, String tipoNegocio) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.correo = correo;
        this.password = password;
        this.edad = edad;
        this.telefono = telefono;
        this.rol = rol;
        this.tipoNegocio = tipoNegocio;
    }
}
