package com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "school_policies")
public class SchoolPolicyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "justification_expiration_days", nullable = false)
    private int justificationExpirationDays;

    @Column(name = "late_attendance_time", nullable = false)
    private LocalTime lateAttendaceTime;

    @Column(name = "school", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private School school;

    public SchoolPolicyEntity() {}

    public SchoolPolicyEntity(Long id, int justificationExpirationDays, LocalTime lateAttendaceTime, School school) {
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
