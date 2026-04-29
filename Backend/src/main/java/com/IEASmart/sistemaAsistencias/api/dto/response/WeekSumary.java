package com.IEASmart.sistemaAsistencias.api.dto.response;

public class WeekSumary {
    private int day;
    private int attendances;
    private int absences;
    private int late;

    public WeekSumary() {}

    public WeekSumary(int day, int attendances, int absences, int late) {
        this.day = day;
        this.attendances = attendances;
        this.absences = absences;
        this.late = late;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getAttendances() {
        return attendances;
    }

    public void setAttendances(int attendances) {
        this.attendances = attendances;
    }

    public int getAbsences() {
        return absences;
    }

    public void setAbsences(int absences) {
        this.absences = absences;
    }

    public int getLate() {
        return late;
    }

    public void setLate(int late) {
        this.late = late;
    }
}
