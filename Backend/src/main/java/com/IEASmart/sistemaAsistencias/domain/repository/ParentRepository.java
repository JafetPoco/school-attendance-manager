package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Parent;

import java.util.List;
import java.util.Optional;

public interface ParentRepository {
    Optional<Parent> findById(Long id);
    Optional<Parent> findByAlumnoId(String alumnoId);
    List<Parent> getAll();
    Parent save(Parent parent);
}
