package com.IEASmart.sistemaAsistencias.api.dto.response;

import java.util.Map;

public class MonthlyAttendanceResponse {
    private StudentResponse student;
    private Map<Integer, String> dailyAttendance; // Día del mes -> Estado
    private Map<String, Object> summary; // Resumen del mes

    public MonthlyAttendanceResponse(StudentResponse student, Map<Integer, String> dailyAttendance, Map<String, Object> summary) {
        this.student = student;
        this.dailyAttendance = dailyAttendance;
        this.summary = summary;
    }

    public StudentResponse getStudent() {
        return student;
    }

    public void setStudent(StudentResponse student) {
        this.student = student;
    }

    public Map<Integer, String> getDailyAttendance() {
        return dailyAttendance;
    }

    public void setDailyAttendance(Map<Integer, String> dailyAttendance) {
        this.dailyAttendance = dailyAttendance;
    }

    public Map<String, Object> getSummary() {
        return summary;
    }

    public void setSummary(Map<String, Object> summary) {
        this.summary = summary;
    }
}
