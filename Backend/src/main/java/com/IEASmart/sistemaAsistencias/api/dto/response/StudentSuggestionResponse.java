package com.IEASmart.sistemaAsistencias.api.dto.response;

public class StudentSuggestionResponse {
    private String fullName;
    private String dni;

    public StudentSuggestionResponse() {}

    public StudentSuggestionResponse(String fullName, String dni) {
        this.fullName = fullName;
        this.dni = dni;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}
