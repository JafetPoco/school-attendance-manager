package com.IEASmart.sistemaAsistencias.api.dto.response;

import java.util.List;
import java.util.Map;

public class MonthlyAttendanceResponse {
    private String dni;
    private String name;
    private String lastName;
    private String section;
    private Map<Integer, String> dailyAttendance; // Día del mes -> Estado

    public MonthlyAttendanceResponse() {}

    public MonthlyAttendanceResponse(String dni, String name, String lastName, String section, Map<Integer, String> dailyAttendance) {
        this.dni = dni;
        this.name = name;
        this.lastName = lastName;
        this.section = section;
        this.dailyAttendance = dailyAttendance;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Map<Integer, String> getDailyAttendance() {
        return dailyAttendance;
    }

    public void setDailyAttendance(Map<Integer, String> dailyAttendance) {
        this.dailyAttendance = dailyAttendance;
    }
}
