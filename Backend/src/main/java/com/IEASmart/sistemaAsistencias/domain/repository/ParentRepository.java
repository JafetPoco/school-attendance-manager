package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Parent;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;

import java.util.List;
import java.util.Optional;

public interface ParentRepository {
    Optional<Parent> findById(Long id, School school);
    Optional<Parent> findByAlumnoId(String alumnoId);
    List<Parent> getAll(School school);
    Parent save(Parent parent);
}
