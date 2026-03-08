package com.IEASmart.sistemaAsistencias.application.dto;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.AttendanceType;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Section;

import java.time.LocalDate;

public record AttendanceCriteria(
        LocalDate date,
        String name,
        Section section,
        AttendanceType attendanceType
) {
}
