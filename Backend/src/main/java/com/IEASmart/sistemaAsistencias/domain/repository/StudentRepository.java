package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.application.dto.StudentCriteria;
import com.IEASmart.sistemaAsistencias.domain.model.Student;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StudentRepository {
    Optional<Student> findById(String dni, School school);
    List<Student> getAllStudents(School school);
    Student save(Student student);
    Page<Student> findAllByFilters(School school, StudentCriteria criteria, Pageable pageable);
    List<Student> findAllByClassId(Long classId);
    List<Student> findAllWithoutAttendanceOnDate(School school, LocalDate date);
    long countStudentsBySchool(School school);
    List<Student> findByNameContainingIgnoreCase(String query, School school);
    Set<String> findAllDnisBySchool(School school);
}
