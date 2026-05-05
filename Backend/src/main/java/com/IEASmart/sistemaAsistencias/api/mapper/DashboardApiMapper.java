package com.IEASmart.sistemaAsistencias.api.mapper;

import com.IEASmart.sistemaAsistencias.api.dto.response.StudentsTopLate;
import com.IEASmart.sistemaAsistencias.api.dto.response.WeekSumary;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.projection.TopLateInfo;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.projection.WeekAttendanceStats;
import org.springframework.stereotype.Component;

@Component
public class DashboardApiMapper {
    public WeekSumary toResponseWeekSumary(WeekAttendanceStats stats) {
        WeekSumary response = new WeekSumary();
        response.setDay(stats.getDay().getDayOfWeek().getValue());
        response.setAttendances(stats.getAttendances());
        response.setAbsences(stats.getAbsences());
        response.setLate(stats.getLate());
        return response;
    }

    public StudentsTopLate toResponseStudentsTopLate(TopLateInfo data) {
        StudentsTopLate response = new StudentsTopLate();
        response.setFullName(data.getName() + " " + data.getFirstLastName() + " " + data.getSecondLastName());
        response.setGrade(data.getGrade() + " - " + data.getSection());
        response.setTotalLate(data.getLateCount());
        return response;
    }
}
