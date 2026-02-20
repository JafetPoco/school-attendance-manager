package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.ProfessorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfessorJpaRepository extends JpaRepository<ProfessorEntity, Long> {
    Boolean existsByEmail(String email);
    Optional<ProfessorEntity> findByEmail(String email);
}
