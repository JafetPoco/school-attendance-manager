package com.IEASmart.sistemaAsistencias.domain.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends BusinessException {
    public ConflictException(String message, String code) {
        super(message, HttpStatus.CONFLICT, code);
    }
}
