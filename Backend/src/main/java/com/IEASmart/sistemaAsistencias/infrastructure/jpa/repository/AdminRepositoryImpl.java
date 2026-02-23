package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Admin;
import com.IEASmart.sistemaAsistencias.domain.repository.AdminRepository;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.AdminEntity;
import com.IEASmart.sistemaAsistencias.infrastructure.mapper.AdminMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AdminRepositoryImpl implements AdminRepository {
    private final AdminJpaRepository jpaRepository;
    private final AdminMapper mapper;

    public AdminRepositoryImpl(AdminJpaRepository jpaRepository, AdminMapper adminMapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = adminMapper;
    }

    @Override
    public List<Admin> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Admin> findById(Long id){
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Admin> findByEmail(String email){
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Boolean existsByEmail(String email){
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public Admin save(Admin admin){
        AdminEntity entity = mapper.toEntity(admin);
        AdminEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
