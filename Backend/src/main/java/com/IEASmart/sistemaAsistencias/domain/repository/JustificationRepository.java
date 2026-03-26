package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Justification;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.JustificationStatus;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;

import java.util.List;
import java.util.Optional;

public interface JustificationRepository {
    Optional<Justification> findByAttendanceId(Long attendanceId);
    List<Justification> findAllByStatus(JustificationStatus status, School school);
    Justification save(Justification justification);
}
