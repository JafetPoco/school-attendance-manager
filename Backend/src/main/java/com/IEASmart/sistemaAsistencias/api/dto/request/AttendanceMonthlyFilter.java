package com.IEASmart.sistemaAsistencias.api.dto.request;

public record AttendanceMonthlyFilter(
        Integer month,
        Long classId
) {}
