package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface StudentJpaRepository extends JpaRepository<StudentEntity, String>, JpaSpecificationExecutor<StudentEntity> {
    List<StudentEntity> findAllBySchool(School school);
    Optional<StudentEntity> findByDniAndSchool(String id, School school);
}
