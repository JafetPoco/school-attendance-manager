package com.IEASmart.sistemaAsistencias.api.dto.response;

public class StudentsTopLate {
    String fullName;
    String grade;
    int totalLate;

    public StudentsTopLate() {}

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getTotalLate() {
        return totalLate;
    }

    public void setTotalLate(int totalLate) {
        this.totalLate = totalLate;
    }
}
