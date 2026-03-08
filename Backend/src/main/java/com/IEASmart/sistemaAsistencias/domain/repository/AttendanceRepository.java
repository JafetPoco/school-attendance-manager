package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Attendance;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository {
    boolean existsByStudentAndDate(String student, LocalDate date);
    Attendance save(Attendance attendance);
    List<Attendance> findAllBySchool(School school);
}
