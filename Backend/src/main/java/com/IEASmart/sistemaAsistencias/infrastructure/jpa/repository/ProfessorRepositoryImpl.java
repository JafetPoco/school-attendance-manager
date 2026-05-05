package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Professor;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.repository.ProfessorRepository;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.ProfessorEntity;
import com.IEASmart.sistemaAsistencias.infrastructure.mapper.ProfessorMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProfessorRepositoryImpl implements ProfessorRepository {
    private final ProfessorJpaRepository jpaRepository;
    private final ProfessorMapper mapper;

    public ProfessorRepositoryImpl(ProfessorJpaRepository jpaRepository, ProfessorMapper professorMapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = professorMapper;
    }

    @Override
    public List<Professor> findAllBySchool(School school){
        return jpaRepository.findAllBySchool(school).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Boolean existsByEmail(String email){
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public Optional<Professor> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Professor> findByEmail(String email){
        return jpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public Professor save(Professor professor) {
        ProfessorEntity entity = mapper.toEntity(professor);
        ProfessorEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
}
