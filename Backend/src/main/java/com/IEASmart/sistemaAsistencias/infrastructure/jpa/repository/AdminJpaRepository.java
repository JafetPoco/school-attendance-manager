package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminJpaRepository extends JpaRepository<AdminEntity, Long> {
    Boolean existsByEmail(String email);
    Optional<AdminEntity> findByEmail(String email);
}
