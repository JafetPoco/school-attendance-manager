package com.IEASmart.sistemaAsistencias.api.dto.response;

import java.time.LocalTime;

public class SchoolPolicyResponse {
    private int justificationExpirationDays;
    private LocalTime lateAttendaceTime;

    public SchoolPolicyResponse() {}

    public SchoolPolicyResponse(int justificationExpirationDays, LocalTime lateAttendaceTime) {
        this.justificationExpirationDays = justificationExpirationDays;
        this.lateAttendaceTime = lateAttendaceTime;
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
}
