package com.IEASmart.sistemaAsistencias.api.dto.response;

public class AttendanceTodayDashboard {
    private long totalAttendances;
    private long totalPresences;
    private long totalAbsences;
    private long totalPendingJustifications;
    private long totalLate;

    public AttendanceTodayDashboard() {}

    public AttendanceTodayDashboard(long totalAttendances, long totalPresences, long totalAbsences, long totalPendingJustifications, long totalLate) {
        this.totalAttendances = totalAttendances;
        this.totalPresences = totalPresences;
        this.totalAbsences = totalAbsences;
        this.totalPendingJustifications = totalPendingJustifications;
        this.totalLate = totalLate;
    }

    public long getTotalAttendances() {
        return totalAttendances;
    }

    public void setTotalAttendances(long totalAttendances) {
        this.totalAttendances = totalAttendances;
    }

    public long getTotalPresences() {
        return totalPresences;
    }

    public void setTotalPresences(long totalPresences) {
        this.totalPresences = totalPresences;
    }

    public long getTotalAbsences() {
        return totalAbsences;
    }

    public void setTotalAbsences(long totalAbsences) {
        this.totalAbsences = totalAbsences;
    }

    public long getTotalPendingJustifications() {
        return totalPendingJustifications;
    }

    public void setTotalPendingJustifications(long totalPendingJustifications) {
        this.totalPendingJustifications = totalPendingJustifications;
    }

    public long getTotalLate() {
        return totalLate;
    }

    public void setTotalLate(long totalLate) {
        this.totalLate = totalLate;
    }
}
