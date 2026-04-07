package com.IEASmart.sistemaAsistencias.api.dto.response;

public class AttendanceResponse {
    private String dni;
    private String studentName;
    private String studentFirstLastName;
    private String studentSecondLastName;
    private String attendanceType;
    private String date;
    private Long idAttendance;

    public AttendanceResponse() {}

    public AttendanceResponse(String dni, String studentName, String studentFirstLastName, String studentSecondLastName, String attendanceType, String date, Long idAttendance) {
        this.dni = dni;
        this.studentName = studentName;
        this.studentFirstLastName = studentFirstLastName;
        this.studentSecondLastName = studentSecondLastName;
        this.attendanceType = attendanceType;
        this.date = date;
        this.idAttendance = idAttendance;
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

    public String getStudentFirstLastName() {
        return studentFirstLastName;
    }

    public void setStudentFirstLastName(String studentFirstLastName) {
        this.studentFirstLastName = studentFirstLastName;
    }

    public String getStudentSecondLastName() {
        return studentSecondLastName;
    }

    public void setStudentSecondLastName(String studentSecondLastName) {
        this.studentSecondLastName = studentSecondLastName;
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

    public Long getIdAttendance() {
        return idAttendance;
    }

    public void setIdAttendance(Long idAttendance) {
        this.idAttendance = idAttendance;
    }
}
