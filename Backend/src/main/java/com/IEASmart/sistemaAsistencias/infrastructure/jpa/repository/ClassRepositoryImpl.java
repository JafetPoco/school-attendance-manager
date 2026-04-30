package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Class;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.repository.ClassRepository;
import com.IEASmart.sistemaAsistencias.infrastructure.mapper.ClassMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class ClassRepositoryImpl implements ClassRepository {
    private final ClassJpaRepository classJpaRepository;
    private final ClassMapper mapper;

    public ClassRepositoryImpl(ClassJpaRepository classJpaRepository, ClassMapper mapper) {
        this.classJpaRepository = classJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Class> findAllBySchool(School school) {
        if (Objects.isNull(school)) {
            return List.of();
        }

        return classJpaRepository.findAllBySchool(school)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Class save(Class newClass) {
        return mapper.toDomain(classJpaRepository.save(mapper.toEntity(newClass)));
    }
}
