package com.IEASmart.sistemaAsistencias.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Professor;
import com.IEASmart.sistemaAsistencias.domain.repository.ProfessorRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProfessorRepositoryImpl implements ProfessorRepository {
    private final ProfessorJpaRepository jpaRepository;

    public ProfessorRepositoryImpl(ProfessorJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Professor> findAll(){
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Boolean existsByEmail(String email){
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public Optional<Professor> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Optional<Professor> findByEmail(String email){
        return jpaRepository.findByEmail(email)
                .map(this::toDomain);
    }

    private Professor toDomain(ProfessorEntity entity) {
        return new Professor(
                entity.getUserId(),
                entity.getNames(),
                entity.getFirstLastName(),
                entity.getSecondLastName(),
                entity.getEmail(),
                null
        );
    }

    @Override
    public Professor save(Professor professor) {
        ProfessorEntity entity = toEntity(professor);
        ProfessorEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    private ProfessorEntity toEntity(Professor professor) {
        ProfessorEntity entity = new ProfessorEntity(
                professor.getNames(),
                professor.getFirstLastName(),
                professor.getSecondLastName(),
                professor.getEmail()
        );

        return entity;
    }
}
