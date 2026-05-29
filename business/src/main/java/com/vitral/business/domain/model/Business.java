package com.vitral.business.domain.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Business {
    private String tenantId;
    
    @NotBlank(message = "El slug es requerido")
    private String slug;
    
    @NotBlank(message = "El nombre es requerido")
    private String nombre;
    
    private String descripcion;
    private String logoUrl;
    private String primaryColor;
    private String secondaryColor;
    private String businessType;
    private String vertical;
    private String phone;
    
    @Email(message = "El correo electrónico no tiene un formato válido")
    private String email;
    
    private String whatsappNumber;
    private String instagramUrl;
    private String facebookUrl;
    private String tiktokUrl;
    private String timezone;
    private Boolean active;
    private List<Schedule> schedules;
}
