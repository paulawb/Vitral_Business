package com.vitral.auth.infraestructure.entry_points.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TenantAssignmentRequest {

    @NotBlank
    private String tenantId;
}
