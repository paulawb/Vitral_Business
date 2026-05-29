package com.vitral.business.infraestructure.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private int status;
    private String message;
    private Map<String, String> errors;
    
    public ApiError(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
