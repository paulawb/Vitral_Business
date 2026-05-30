package com.vitral.auth.infraestructure.entry_points.dto;

import com.vitral.auth.domain.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private long expiresInSeconds;
    private String tenantId;
    private Usuario usuario;
}
