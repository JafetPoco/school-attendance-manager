package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.JustificationStatus;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.JustificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JustificationJpaRepository extends JpaRepository<JustificationEntity, Long> {
    Optional<JustificationEntity> findByAttendance_Id(Long attendanceId);
    List<JustificationEntity> findAllByStatusAndAttendance_Student_School(JustificationStatus status, School school);
}
