package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Attendance;

import java.time.LocalDate;

public interface AttendanceRepository {
    boolean existsByStudentAndDate(String student, LocalDate date);
    Attendance save(Attendance attendance);
}
