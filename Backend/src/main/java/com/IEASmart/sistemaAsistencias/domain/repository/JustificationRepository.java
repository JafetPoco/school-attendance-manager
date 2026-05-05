package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Justification;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.JustificationStatus;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JustificationRepository {
    Optional<Justification> findById(Long id);
    Optional<Justification> findByAttendanceId(String attendanceId);
    List<Justification> findAllByStatus(JustificationStatus status, School school);
    Page<Justification> findAllByFilter(School school, JustificationStatus justificationStatus, LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<Justification> findAllByFilter(School school, JustificationStatus justificationStatus, LocalDate date, String name, Pageable pageable);
    Justification save(Justification justification);
    long countByStatus(JustificationStatus status, School school);
}
