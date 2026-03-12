package com.IEASmart.sistemaAsistencias.application.dto;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Section;

public record AttendanceMonthlyCriteria(
        int month,
        Section section
) {}
