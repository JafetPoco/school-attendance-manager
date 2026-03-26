package com.IEASmart.sistemaAsistencias.domain.model;

import java.time.LocalDateTime;

public class Token {
    private String token;
    private Long attendanceId;
    private LocalDateTime expiryDate;
    private LocalDateTime createdAt;
    private boolean used;

    public Token(String token, Long attendanceId, LocalDateTime expiryDate, LocalDateTime createdAt, boolean used) {
        this.token = token;
        this.attendanceId = attendanceId;
        this.expiryDate = expiryDate;
        this.createdAt = createdAt;
        this.used = used;
    }

    public Token() {}

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
