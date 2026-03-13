package com.IEASmart.sistemaAsistencias.api.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class InformationAttendanceResponse {
    private StudentResponse student;
    private long totalAttendances;
    private long totalAbsences;
    private long totalLate;
    private long totalExcusedAbsences;
    private long total;
    private Map<LocalDate, String> attendances;

    public InformationAttendanceResponse() {}

    public InformationAttendanceResponse(StudentResponse student, long totalAttendances, long totalAbsences, long totalLate, long totalExcusedAbsences, long total, Map<LocalDate, String> attendances) {
        this.student = student;
        this.totalAttendances = totalAttendances;
        this.totalAbsences = totalAbsences;
        this.totalLate = totalLate;
        this.totalExcusedAbsences = totalExcusedAbsences;
        this.total = total;
        this.attendances = attendances;
    }

    public StudentResponse getStudent() {
        return student;
    }

    public void setStudent(StudentResponse student) {
        this.student = student;
    }

    public long getTotalAttendances() {
        return totalAttendances;
    }

    public void setTotalAttendances(long totalAttendances) {
        this.totalAttendances = totalAttendances;
    }

    public long getTotalAbsences() {
        return totalAbsences;
    }

    public void setTotalAbsences(long totalAbsences) {
        this.totalAbsences = totalAbsences;
    }

    public long getTotalLate() {
        return totalLate;
    }

    public void setTotalLate(long totalLate) {
        this.totalLate = totalLate;
    }

    public long getTotalExcusedAbsences() {
        return totalExcusedAbsences;
    }

    public void setTotalExcusedAbsences(long totalExcusedAbsences) {
        this.totalExcusedAbsences = totalExcusedAbsences;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Map<LocalDate, String> getAttendances() {
        return attendances;
    }

    public void setAttendances(Map<LocalDate, String> attendances) {
        this.attendances = attendances;
    }
}
