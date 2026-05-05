package com.IEASmart.sistemaAsistencias.domain.exception;

import org.springframework.http.HttpStatus;

public class InvalidArgumentException extends BusinessException {
    public InvalidArgumentException(String message, String code) {
        super(message, HttpStatus.BAD_REQUEST, code);
    }

    public static InvalidArgumentException required(String fieldName) {
        return new InvalidArgumentException("El campo '" + fieldName + "' es requerido.", "REQUIRED_FIELD");
    }

}
