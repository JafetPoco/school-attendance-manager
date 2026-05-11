package com.IEASmart.sistemaAsistencias.application.dto;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Grade;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Level;

public record ImportRowData(
        int rowNum,
        String dni,
        Level level,
        Grade grade,
        String section,
        String firstLast,
        String secondLast,
        String name,
        String parentName,
        String parentPhone
) {}
