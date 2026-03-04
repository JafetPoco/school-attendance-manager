package com.IEASmart.sistemaAsistencias.api.dto.request;

public record StudentFilter(
        String name,
        String level,
        String grade,
        String section
) {}
