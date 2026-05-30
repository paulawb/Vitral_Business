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
    private String telefono;
    private String fotoPerfil;
    private String estado;
    private Boolean emailVerificado;
    private LocalDateTime ultimoLogin;
    private String tenantId;
    private String providerAuth;
    private Integer intentosFallidos;
    private LocalDateTime bloqueadoHasta;
    private String tokenRecuperacion;
    private Boolean activo;
    private LocalDateTime fechaActualizacion;
    private String refreshToken;

    public Usuario(String cedula, String nombres, String correo, String password, String telefono) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.correo = correo;
        this.password = password;
        this.telefono = telefono;
    }
}
