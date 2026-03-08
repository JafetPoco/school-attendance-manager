package com.IEASmart.sistemaAsistencias.domain.model.valueObject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AttendanceType {
    PRESENTE("Presente"),
    AUSENTE("Ausente"),
    TARDE("Tarde"),
    JUSTIFICADO("Justificado");

    private final String displayName;

    AttendanceType(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getFullName() {
        return displayName;
    }


    @JsonCreator
    public static AttendanceType from(String value) {
        if(value == null) return null;
        try {
            return AttendanceType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid section: " + value);
        }
    }
}
