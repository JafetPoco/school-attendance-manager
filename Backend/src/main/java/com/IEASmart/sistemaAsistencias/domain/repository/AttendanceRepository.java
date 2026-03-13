package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.application.dto.AttendanceCriteria;
import com.IEASmart.sistemaAsistencias.domain.model.Attendance;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.AttendanceType;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository {
    boolean existsByStudentAndDate(String student, LocalDate date);
    Attendance save(Attendance attendance);
    List<Attendance> saveAll(List<Attendance> attendances);
    Page<Attendance> findAllByFilter(School school, AttendanceCriteria criteria, Pageable pageable);
    List<Attendance> findByStudentSchoolAndSectionAndDateBetween(School school, Section section, LocalDate startDate, LocalDate endDate);
    long countByStudentDniAndAttendanceTypeAndDateBetween(String dni, AttendanceType type, LocalDate startDate, LocalDate endDate);
    List<Attendance> findByStudentAndDateBetween(String dni, LocalDate startDate, LocalDate endDate);
}
