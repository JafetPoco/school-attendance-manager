package com.IEASmart.sistemaAsistencias.domain.model.valueObject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum School {
    GJS("Gral. José de San Martín"),
    EFF("Eduardo Francisco Forga");

    private final String fullName;

    School (String fullName) {
        this.fullName = fullName;
    }

    @JsonValue
    public String getFullName() {
        return fullName;
    }


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
