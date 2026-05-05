package com.IEASmart.sistemaAsistencias.application.dto;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Grade;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Level;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Section;

public record StudentCriteria(
        String name,
        Level level,
        Grade grade,
        String section
) {}
