package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Student;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    Optional<Student> findById(String dni, School school);
    List<Student> getAllStudents(School school);
    Student save(Student student);
}
