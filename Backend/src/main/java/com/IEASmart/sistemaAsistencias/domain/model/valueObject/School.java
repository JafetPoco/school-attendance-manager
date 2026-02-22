package com.IEASmart.sistemaAsistencias.domain.model.valueObject;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum School {
    GJS,
    EFF;

    @JsonCreator
    public static School from(String value) {
        if(value == null) return null;
        try {
            return School.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid section: " + value);
        }
    }
}
