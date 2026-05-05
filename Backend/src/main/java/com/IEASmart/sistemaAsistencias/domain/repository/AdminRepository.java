package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Admin;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;

import java.util.List;
import java.util.Optional;

public interface AdminRepository {
    List<Admin> findAllBySchool(School school);
    Optional<Admin> findById(Long id);
    Optional<Admin> findByEmail(String email);
    Boolean existsByEmail(String email);
    Admin save(Admin admin);
}
