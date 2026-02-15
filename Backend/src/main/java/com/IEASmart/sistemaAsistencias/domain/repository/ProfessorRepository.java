package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Professor;

import java.util.List;

public interface ProfessorRepository {
    public List<Professor> findAll();
    public boolean existsByEmail(String email);
    public Professor save(Professor professor);
    public Professor findById(Long id);
}
