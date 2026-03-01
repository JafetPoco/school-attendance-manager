package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentJpaRepository extends JpaRepository<StudentEntity, String> {
    List<StudentEntity> findAllBySchool(School school);
    Optional<StudentEntity> findByDniAndSchool(String id, School school);
}
