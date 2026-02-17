package com.IEASmart.sistemaAsistencias.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfessorJpaRepository extends JpaRepository<ProfessorEntity, Long> {
    Boolean existsByEmail(String email);
    Optional<ProfessorEntity> findByEmail(String email);
}
