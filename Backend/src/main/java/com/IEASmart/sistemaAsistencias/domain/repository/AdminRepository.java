package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Admin;

import java.util.List;
import java.util.Optional;

public interface AdminRepository {
    List<Admin> findAll();
    Optional<Admin> findById(Long id);
    Optional<Admin> findByEmail(String email);
    Boolean existsByEmail(String email);
    Admin save(Admin admin);
}
