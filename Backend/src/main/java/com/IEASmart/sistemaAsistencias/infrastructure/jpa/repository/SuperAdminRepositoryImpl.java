package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.SuperAdmin;
import com.IEASmart.sistemaAsistencias.domain.repository.SuperAdminRepository;
import com.IEASmart.sistemaAsistencias.infrastructure.mapper.SuperAdminMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SuperAdminRepositoryImpl implements SuperAdminRepository {
    private final SuperAdminJpaRepository jpaRepository;
    private final SuperAdminMapper mapper;

    public SuperAdminRepositoryImpl(SuperAdminJpaRepository jpaRepository, SuperAdminMapper superAdminMapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = superAdminMapper;
    }

    @Override
    public Boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public Optional<SuperAdmin> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public SuperAdmin save(SuperAdmin superAdmin) {
        if (superAdmin == null) return null;
        var entity = mapper.toEntity(superAdmin);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
