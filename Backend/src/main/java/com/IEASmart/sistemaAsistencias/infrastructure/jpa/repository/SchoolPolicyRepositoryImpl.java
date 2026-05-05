package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.SchoolPolicy;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.repository.SchoolPolicyRepository;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.SchoolPolicyEntity;
import com.IEASmart.sistemaAsistencias.infrastructure.mapper.SchoolPolicyMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SchoolPolicyRepositoryImpl implements SchoolPolicyRepository {
    private final SchoolPolicyJpaRepository jpaRepository;
    private final SchoolPolicyMapper mapper;

    public SchoolPolicyRepositoryImpl(SchoolPolicyJpaRepository jpaRepository, SchoolPolicyMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public SchoolPolicy save(SchoolPolicy schoolPolicy) {
        SchoolPolicyEntity entity = mapper.toEntity(schoolPolicy);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<SchoolPolicy> getBySchool(School school){
        return jpaRepository.findBySchool(school)
                .map(mapper::toDomain);
    }
}
