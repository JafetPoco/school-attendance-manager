package com.IEASmart.sistemaAsistencias.domain.model;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.AttendanceType;

import java.time.LocalDate;
import java.time.LocalTime;

public class Attendance {
    private Long id;
    private LocalTime time;
    private LocalDate date;
    private AttendanceType attendanceType;
    private Student student;

    public Attendance() {}

    public Attendance(Long id, LocalTime time, LocalDate date, AttendanceType attendanceType, Student student) {
        this.id = id;
        this.time = time;
        this.date = date;
        this.attendanceType = attendanceType;
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public AttendanceType getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(AttendanceType attendanceType) {
        this.attendanceType = attendanceType;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
