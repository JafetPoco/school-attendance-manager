package com.IEASmart.sistemaAsistencias.api.dto.request;

public record AttendanceFilter(
        String date,
        String name,
        Long classId,
        String attendanceType
) {}
