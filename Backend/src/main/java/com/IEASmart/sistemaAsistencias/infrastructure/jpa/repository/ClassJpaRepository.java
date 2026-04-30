package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassJpaRepository extends JpaRepository<ClassEntity, Long> {
    List<ClassEntity> findAllBySchool(School school);
}
