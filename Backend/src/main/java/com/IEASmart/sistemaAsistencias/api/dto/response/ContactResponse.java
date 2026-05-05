package com.IEASmart.sistemaAsistencias.api.dto.response;

public class ContactResponse {
    String number;
    String parentName;
    String studentName;
    String token;

    public ContactResponse() {}

    public ContactResponse(String number, String parentName, String studentName, String token) {
        this.number = number;
        this.parentName = parentName;
        this.studentName = studentName;
        this.token = token;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
