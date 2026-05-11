package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Parent;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ParentRepository {
    Optional<Parent> findById(Long id, School school);
    Optional<Parent> findByAlumnoId(String alumnoId);
    List<Parent> getAll(School school);
    Optional<Parent> findByPhoneNumber(String phoneNumber, School school);
    Parent save(Parent parent);
    List<Parent> saveAll(List<Parent> parents);
    void delete(Parent parent);
    Map<String, Parent> findByPhoneNumberIn(Set<String> phones, School school);

}
