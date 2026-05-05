package com.IEASmart.sistemaAsistencias.api.dto.request;

public class SchoolPolicyRequest {
    private Integer justificationExpirationDays;
    private String lateAttendaceTime;

    public SchoolPolicyRequest(Integer justificationExpirationDays, String lateAttendaceTime) {
        this.justificationExpirationDays = justificationExpirationDays;
        this.lateAttendaceTime = lateAttendaceTime;
    }

    public Integer getJustificationExpirationDays() {
        return justificationExpirationDays;
    }

    public void setJustificationExpirationDays(Integer justificationExpirationDays) {
        this.justificationExpirationDays = justificationExpirationDays;
    }

    public String getLateAttendaceTime() {
        return lateAttendaceTime;
    }

    public void setLateAttendaceTime(String lateAttendaceTime) {
        this.lateAttendaceTime = lateAttendaceTime;
    }
}
