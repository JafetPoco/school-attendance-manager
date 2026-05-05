package com.IEASmart.sistemaAsistencias.domain.model.valueObject;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Section {
    BENJAMIN,
    NOE,
    MOISES,
    JACOB,
    ENOC,
    JOSE,
    GEDEON,
    JOSUE,
    ELIAS,
    ELISEO,
    DANIEL,
    ESTEBAN,
    MATEO,
    SALOMON,
    DAVID,
    JONATAN;

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
