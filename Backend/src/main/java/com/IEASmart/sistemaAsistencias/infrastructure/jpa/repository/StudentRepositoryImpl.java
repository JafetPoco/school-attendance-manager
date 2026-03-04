package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Student;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Grade;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Level;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Section;
import com.IEASmart.sistemaAsistencias.domain.repository.StudentRepository;
import com.IEASmart.sistemaAsistencias.infrastructure.mapper.StudentMapper;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.StudentEntity;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.specification.StudentSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

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
    public List<Student> findAllByFilters(School school, String name, Level level, Grade grade, Section section) {
        Specification<StudentEntity> spec = Specification
                .where(StudentSpecifications.hasSchool(school))
                .and(StudentSpecifications.hasName(name))
                .and(StudentSpecifications.hasLevel(level))
                .and(StudentSpecifications.hasGrade(grade))
                .and(StudentSpecifications.hasSection(section));

        return jpaRepository.findAll(spec).stream().map(studentMapper::toDomain).toList();
    }
}
