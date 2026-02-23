package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.domain.model.SuperAdmin;

import java.util.Optional;

public interface SuperAdminRepository {
    Boolean existsByEmail(String email);
    Optional<SuperAdmin> findByEmail(String email);
    SuperAdmin save(SuperAdmin superAdmin);
}
