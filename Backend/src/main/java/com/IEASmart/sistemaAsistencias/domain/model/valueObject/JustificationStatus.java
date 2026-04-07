package com.IEASmart.sistemaAsistencias.domain.model.valueObject;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum JustificationStatus {
    PENDIENTE,
    ACEPTADA,
    RECHAZADA;

    @JsonCreator
    public static JustificationStatus from(String value) {
        if(value == null) return null;
        try {
            return JustificationStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid level: " + value);
        }
    }
}
