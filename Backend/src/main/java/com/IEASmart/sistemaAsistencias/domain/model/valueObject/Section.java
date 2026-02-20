package com.IEASmart.sistemaAsistencias.domain.model.valueObject;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Section {
    A,
    B,
    C,
    D,
    E,
    F;

    @JsonCreator
    public static Section from(String value) {
        if(value == null) return null;
        try {
            return Section.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid section: " + value);
        }
    }
}
