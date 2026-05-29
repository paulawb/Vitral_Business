package com.ecommerce.crud.infraestructure.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String mensaje;
    private Map<String, String> errores;

    public ErrorResponse(int status, String mensaje) {
        this.status = status;
        this.mensaje = mensaje;
        this.errores = null;
    }
}
