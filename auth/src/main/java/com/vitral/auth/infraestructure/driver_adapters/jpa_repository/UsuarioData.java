package com.vitral.auth.infraestructure.driver_adapters.jpa_repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "usuarios")
@Data
public class UsuarioData {

    @Id
    @NotBlank(message = "Cedula es obligatoria")
    @Size(max = 30, message = "Cedula debe tener maximo 30 caracteres")
    @Column(unique = true, nullable = false, length = 30)
    private String cedula;

    @Column(nullable = false, updatable = false, length = 36, unique = true)
    private String uuid;

    @Size(max = 100, message = "Nombres debe tener maximo 100 caracteres")
    private String nombres;
    
    @Size(max = 100, message = "Apellidos debe tener maximo 100 caracteres")
    private String apellidos;

    @NotBlank(message = "Correo es obligatorio")
    @Email(message = "Formato de correo invalido")
    @Size(max = 120, message = "Correo debe tener maximo 120 caracteres")
    @Column(length = 120, nullable = false, unique = true)
    private String correo;

    @NotBlank(message = "Password es obligatorio")
    private String password;

    @Size(max = 20, message = "Telefono debe tener maximo 20 caracteres")
    @Column(length = 20)
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
    
    // Deprecated: kept for database compatibility, will be removed by migration
    @Column(name = "tipo_negocio")
    private String tipoNegocio;

     public UsuarioData(String cedula, String nombres, String correo, String password, String telefono) {
         this.cedula = cedula;
         this.nombres = nombres;
         this.correo = correo;
         this.password = password;
         this.telefono = telefono;
     }

     @PrePersist
     void onCreate() {
         if (uuid == null || uuid.isBlank()) {
             uuid = UUID.randomUUID().toString();
         }
         if (fechaActualizacion == null) {
             fechaActualizacion = LocalDateTime.now();
         }
         if (activo == null) {
             activo = true;
         }
         if (estado == null || estado.isBlank()) {
             estado = "PENDING";
         }
     }

     @PreUpdate
     void onUpdate() {
         fechaActualizacion = LocalDateTime.now();
     }
 }
