package com.IEASmart.sistemaAsistencias.domain.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedOperationException extends BusinessException {
    public UnauthorizedOperationException(String message) {
        super(message, HttpStatus.FORBIDDEN, "UNAUTHORIZED_OPERATION");
    }

    public static UnauthorizedOperationException insufficientRole(String requiredRole) {
        return new UnauthorizedOperationException(
                String.format("Se requiere rol: %s", requiredRole)
        );
    }
}
