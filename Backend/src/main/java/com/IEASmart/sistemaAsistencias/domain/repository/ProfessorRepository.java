package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Professor;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;

import java.util.List;
import java.util.Optional;

public interface ProfessorRepository {
    List<Professor> findAllBySchool(School school);
    Boolean existsByEmail(String email);
    Optional<Professor> findById(Long id);
    Optional<Professor> findByEmail(String email);
    Professor save(Professor professor);
}
