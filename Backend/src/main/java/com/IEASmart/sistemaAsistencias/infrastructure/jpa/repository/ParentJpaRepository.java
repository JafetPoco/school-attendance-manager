package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParentJpaRepository extends JpaRepository<ParentEntity, Long> {
    Optional<ParentEntity> findByParentIdAndSchool(Long id, School school);

    List<ParentEntity> findAllBySchool(School school);
    @Query("SELECT p FROM ParentEntity p JOIN p.children h WHERE h.dni = :alumnoId")
    Optional<ParentEntity> findByAlumnoId(@Param("alumnoId") String alumnoId);
}
