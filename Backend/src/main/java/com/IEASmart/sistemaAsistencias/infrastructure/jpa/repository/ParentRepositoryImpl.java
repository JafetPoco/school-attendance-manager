package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Parent;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.repository.ParentRepository;
import com.IEASmart.sistemaAsistencias.infrastructure.mapper.ParentMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ParentRepositoryImpl implements ParentRepository {
    private final ParentJpaRepository jpaRepository;
    private final ParentMapper mapper;

    public ParentRepositoryImpl(ParentJpaRepository parentJpaRepository, ParentMapper parentMapper) {
        this.mapper = parentMapper;
        this.jpaRepository = parentJpaRepository;
    }

    @Override
    public Optional<Parent> findById(Long id, School school){
        return jpaRepository.findByParentIdAndSchool(id, school).map(mapper::toDomain);
    }

    @Override
    public Optional<Parent> findByAlumnoId(String alumnoId){
        return jpaRepository.findByAlumnoId(alumnoId).map(mapper::toDomain);
    }

    @Override
    public List<Parent> getAll(School school){
        return jpaRepository.findAllBySchool(school).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Parent save(Parent parent){
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(parent)));
    }

}
