package com.vitral.auth.infraestructure.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ApiError {

    private String message;
    private int status;
    private String path;
    private LocalDateTime timestamp;
}
