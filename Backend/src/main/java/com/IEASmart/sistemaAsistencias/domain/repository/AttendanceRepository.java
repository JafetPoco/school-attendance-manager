package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.application.dto.AttendanceCriteria;
import com.IEASmart.sistemaAsistencias.domain.model.Attendance;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository {
    boolean existsByStudentAndDate(String student, LocalDate date);
    Attendance save(Attendance attendance);
    List<Attendance> findAllBySchool(School school);
    Page<Attendance> findAllByFilter(School school, AttendanceCriteria criteria, Pageable pageable);
    List<Attendance> findByStudentSchoolAndDateBetween(School school,LocalDate startDate,LocalDate endDate);
}
