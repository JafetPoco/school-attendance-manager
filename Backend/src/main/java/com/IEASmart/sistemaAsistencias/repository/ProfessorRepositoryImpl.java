package com.IEASmart.sistemaAsistencias.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Professor;
import com.IEASmart.sistemaAsistencias.domain.repository.ProfessorRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public boolean existsByEmail(String email){
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public Professor save(Professor professor){
        ProfessorEntity entity = toEntity(professor);
        ProfessorEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Professor findById(Long id){
        return jpaRepository.findById(id)
                .map(this::toDomain)
                .orElse(null);
    }

    private Professor toDomain(ProfessorEntity entity) {
        return new Professor(
                entity.getUserId(),
                entity.getNames(),
                entity.getFirstLastName(),
                entity.getSecondLastName(),
                entity.getEmail()
        );
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
