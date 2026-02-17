package com.IEASmart.sistemaAsistencias.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Admin;
import com.IEASmart.sistemaAsistencias.domain.repository.AdminRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AdminRepositoryImpl implements AdminRepository {
    private final AdminJpaRepository jpaRepository;

    public AdminRepositoryImpl(AdminJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Admin> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Admin> findById(Long id){
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Admin> findByEmail(String email){
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Boolean existsByEmail(String email){
        return jpaRepository.existsByEmail(email);
    }

    private Admin toDomain(AdminEntity entity) {
        if (entity == null) return null;
        return new Admin(
                entity.getUserId(),
                entity.getNames(),
                entity.getFirstLastName(),
                entity.getSecondLastName(),
                entity.getEmail(),
                null
        );
    }

    @Override
    public Admin save(Admin admin){
        AdminEntity entity = toEntity(admin);
        AdminEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    private AdminEntity toEntity(Admin admin) {
        if (admin == null) return null;
        AdminEntity entity = new AdminEntity(
                admin.getNames(),
                admin.getFirstLastName(),
                admin.getSecondLastName(),
                admin.getEmail()
        );
        return entity;
    }
}
