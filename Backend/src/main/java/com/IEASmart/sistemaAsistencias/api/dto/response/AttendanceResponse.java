package com.IEASmart.sistemaAsistencias.api.dto.response;

import com.IEASmart.sistemaAsistencias.api.dto.request.AttendanceRequest;

public class AttendanceResponse {
    private String dni;
    private String studentName;
    private String attendanceType;
    private String date;

    public AttendanceResponse() {}

    public AttendanceResponse(String dni, String studentName, String attendanceType, String date) {
        this.dni = dni;
        this.studentName = studentName;
        this.attendanceType = attendanceType;
        this.date = date;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
