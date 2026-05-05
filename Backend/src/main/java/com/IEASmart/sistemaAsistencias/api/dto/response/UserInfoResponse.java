package com.IEASmart.sistemaAsistencias.api.dto.response;

public class UserInfoResponse {
    private String dni;
    private String name;
    private String firstLastName;
    private String secondLastName;
    private String email;
    private String role;

    public UserInfoResponse(String dni, String name, String firstLastName, String secondLastName, String email, String role) {
        this.dni = dni;
        this.name = name;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.email = email;
        this.role = role;
    }

    public UserInfoResponse() {}

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public void setFirstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
