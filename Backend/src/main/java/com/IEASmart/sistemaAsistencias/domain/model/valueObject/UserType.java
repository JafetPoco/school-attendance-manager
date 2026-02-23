package com.IEASmart.sistemaAsistencias.domain.model.valueObject;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserType {
    PROFESSOR,
    ADMIN,
    SUPER_ADMIN;

    @JsonCreator
    public static UserType from(String value) {
        if(value == null) return null;
        try {
            return UserType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid section: " + value);
        }
    }
}
