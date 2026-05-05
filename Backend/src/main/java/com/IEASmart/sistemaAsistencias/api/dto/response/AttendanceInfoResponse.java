package com.IEASmart.sistemaAsistencias.api.dto.response;

import java.time.LocalDate;

public class AttendanceInfoResponse {
    String id;
    String fullName;
    LocalDate date;
    String grade;

    public AttendanceInfoResponse() {}

    public AttendanceInfoResponse(String id, String fullName, LocalDate date, String grade) {
        this.id = id;
        this.fullName = fullName;
        this.date = date;
        this.grade = grade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
