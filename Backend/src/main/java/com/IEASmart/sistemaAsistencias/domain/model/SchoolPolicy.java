package com.IEASmart.sistemaAsistencias.domain.model;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;

import java.time.LocalTime;

public class SchoolPolicy {
    private Long id;
    private int justificationExpirationDays;
    private LocalTime lateAttendaceTime;
    private School school;

    public SchoolPolicy() {}

    public SchoolPolicy(Long id, int justificationExpirationDays, LocalTime lateAttendaceTime, School school) {
        this.id = id;
        this.justificationExpirationDays = justificationExpirationDays;
        this.lateAttendaceTime = lateAttendaceTime;
        this.school = school;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getJustificationExpirationDays() {
        return justificationExpirationDays;
    }

    public void setJustificationExpirationDays(int justificationExpirationDays) {
        this.justificationExpirationDays = justificationExpirationDays;
    }

    public LocalTime getLateAttendaceTime() {
        return lateAttendaceTime;
    }

    public void setLateAttendaceTime(LocalTime lateAttendaceTime) {
        this.lateAttendaceTime = lateAttendaceTime;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}
