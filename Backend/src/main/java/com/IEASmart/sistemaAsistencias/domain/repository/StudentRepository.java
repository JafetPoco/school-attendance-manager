package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Student;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Level;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Grade;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Section;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    Optional<Student> findById(String dni, School school);
    List<Student> getAllStudents(School school);
    Student save(Student student);
    List<Student> findAllByFilters(School school, String name, Level level, Grade grade, Section section);
}
