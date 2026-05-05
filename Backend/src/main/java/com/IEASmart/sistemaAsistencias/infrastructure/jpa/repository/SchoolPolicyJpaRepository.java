package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.SchoolPolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolPolicyJpaRepository extends JpaRepository<SchoolPolicyEntity, Long> {
    Optional<SchoolPolicyEntity> findBySchool(School school);
}
