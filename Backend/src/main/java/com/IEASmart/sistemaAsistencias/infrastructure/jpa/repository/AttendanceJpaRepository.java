package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.AttendanceType;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Section;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceJpaRepository extends JpaRepository<AttendanceEntity, Long>, JpaSpecificationExecutor<AttendanceEntity> {
    Optional<AttendanceEntity> findByStudent_DniAndDate(String student, LocalDate date);
    List<AttendanceEntity> findAllByStudent_SchoolAndStudent_SectionAndDateBetweenOrderByStudent_FirstLastNameAsc(School school, Section section, LocalDate startDate, LocalDate endDate);
    long countByStudent_DniAndAttendanceTypeAndDateBetween(String studentDni, AttendanceType type, LocalDate startDate, LocalDate endDate);
    List<AttendanceEntity> findAllByStudent_DniAndDateBetweenOrderByDateAsc(String studentDni, LocalDate startDate, LocalDate endDate);
}
