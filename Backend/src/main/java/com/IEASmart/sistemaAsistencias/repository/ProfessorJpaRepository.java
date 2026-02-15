package com.IEASmart.sistemaAsistencias.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorJpaRepository extends JpaRepository<ProfessorEntity, Long> {
    public Boolean existsByEmail(String email);
}
