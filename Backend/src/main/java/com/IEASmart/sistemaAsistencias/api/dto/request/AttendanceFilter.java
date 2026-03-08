package com.IEASmart.sistemaAsistencias.api.dto.request;

public record AttendanceFilter(
        String date,
        String name,
        String section,
        String attendanceType
) {}
