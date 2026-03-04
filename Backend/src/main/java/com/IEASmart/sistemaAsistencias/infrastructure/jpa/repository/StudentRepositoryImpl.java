package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.application.dto.StudentCriteria;
import com.IEASmart.sistemaAsistencias.domain.model.Student;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.repository.StudentRepository;
import com.IEASmart.sistemaAsistencias.infrastructure.mapper.StudentMapper;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.StudentEntity;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.specification.StudentSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepositoryImpl implements StudentRepository {
    private final StudentJpaRepository jpaRepository;
    private final StudentMapper studentMapper;

    public StudentRepositoryImpl(StudentJpaRepository studentJpaRepository, StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
        this.jpaRepository = studentJpaRepository;
    }

    @Override
    public Optional<Student> findById(String dni, School school){
        return jpaRepository.findByDniAndSchool(dni, school).map(studentMapper::toDomain);
    }

    @Override
    public List<Student> getAllStudents(School school){
        return jpaRepository.findAllBySchool(school).stream().map(studentMapper::toDomain).toList();
    }

    @Override
    public Student save(Student student){
        return studentMapper.toDomain(jpaRepository.save(studentMapper.toEntity(student)));
    }

    @Override
    public Page<Student> findAllByFilters(School school, StudentCriteria criteria, Pageable pageable) {
        Specification<StudentEntity> spec = Specification
                .where(StudentSpecifications.hasSchool(school))
                .and(StudentSpecifications.hasName(criteria.name()))
                .and(StudentSpecifications.hasLevel(criteria.level()))
                .and(StudentSpecifications.hasGrade(criteria.grade()))
                .and(StudentSpecifications.hasSection(criteria.section()));

        Page<StudentEntity> page = jpaRepository.findAll(spec, pageable);
        return page.map(studentMapper::toDomain);
    }
}
