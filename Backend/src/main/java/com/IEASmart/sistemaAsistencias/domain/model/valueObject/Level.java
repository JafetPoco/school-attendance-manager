package com.IEASmart.sistemaAsistencias.domain.model.valueObject;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Level {
    PRIMARIA,
    SECUNDARIA;

    @JsonCreator
    public static Level from(String value) {
        if(value == null) return null;
        try {
            return Level.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid level: " + value);
        }
    }
}