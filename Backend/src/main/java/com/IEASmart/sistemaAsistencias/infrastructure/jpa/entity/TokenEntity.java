package com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
public class TokenEntity {
    @Id
    private String token;

    @Column(name = "attendance_id")
    private Long attendanceId;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "used")
    private boolean used;

    public TokenEntity() {}

    public TokenEntity(String token, Long attendanceId, LocalDateTime expiryDate, LocalDateTime createdAt, boolean used) {
        this.token = token;
        this.attendanceId = attendanceId;
        this.expiryDate = expiryDate;
        this.createdAt = createdAt;
        this.used = used;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Long attendanceId) {
        this.attendanceId = attendanceId;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
