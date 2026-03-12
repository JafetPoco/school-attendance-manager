package com.IEASmart.sistemaAsistencias.api.dto.request;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.AttendanceType;

public class AttendanceRequest {
    private String dni;
    private AttendanceType attendanceType;

    public AttendanceRequest() {}

    public AttendanceRequest(String dni, AttendanceType attendanceType) {
        this.dni = dni;
        this.attendanceType = attendanceType;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public AttendanceType getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(AttendanceType attendanceType) {
        this.attendanceType = attendanceType;
    }
}
