package com.IEASmart.sistemaAsistencias.api.dto.response;

import java.util.List;

public class DashboardResponse {
    StatisticsToday statisticsToday;
    List<WeekSumary> weekSumary;
    List<StudentsTopLate> studentsTopLate;
    long totalStudents;

    public DashboardResponse() {}

    public StatisticsToday getStatisticsToday() {
        return statisticsToday;
    }

    public void setStatisticsToday(StatisticsToday statisticsToday) {
        this.statisticsToday = statisticsToday;
    }

    public List<WeekSumary> getWeekSumary() {
        return weekSumary;
    }

    public void setWeekSumary(List<WeekSumary> weekSumary) {
        this.weekSumary = weekSumary;
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public List<StudentsTopLate> getStudentsTopLate() {
        return studentsTopLate;
    }

    public void setStudentsTopLate(List<StudentsTopLate> studentsTopLate) {
        this.studentsTopLate = studentsTopLate;
    }
}
