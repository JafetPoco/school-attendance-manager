package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.JustificationStatus;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.JustificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface JustificationJpaRepository extends JpaRepository<JustificationEntity, Long>, JpaSpecificationExecutor<JustificationEntity> {
    Optional<JustificationEntity> findByAttendance_Id(Long attendanceId);
    List<JustificationEntity> findAllByStatusAndAttendance_Student_School(JustificationStatus status, School school);
    long countByStatusAndAttendance_Student_School(JustificationStatus status, School school);
}