package com.IEASmart.sistemaAsistencias.domain.model.valueObject;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Grade {
    PRIMERO,
    SEGUNDO,
    TERCERO,
    CUARTO,
    QUINTO,
    SEXTO;

    @JsonCreator
    public static Grade from(String value) {
        if(value == null) return null;
        try {
            return Grade.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid grade: " + value);
        }
    }
}
