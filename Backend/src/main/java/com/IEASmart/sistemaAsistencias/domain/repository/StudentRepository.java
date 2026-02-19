package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    Optional<Student> findById(String dni);
    List<Student> getAllStudents();
    Student save(Student student);
}
