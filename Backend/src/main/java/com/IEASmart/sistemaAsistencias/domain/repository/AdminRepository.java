package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Admin;

import java.util.List;

public interface AdminRepository {
    public List<Admin> findAll();
    public Admin findById(Long id);
    public Admin save(Admin admin);
}
