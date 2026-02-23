package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.SuperAdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuperAdminJpaRepository extends JpaRepository<SuperAdminEntity, Long> {
    Boolean existsByEmail(String email);
    Optional<SuperAdminEntity> findByEmail(String email);
}
