package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Student;
import com.IEASmart.sistemaAsistencias.domain.repository.StudentRepository;
import com.IEASmart.sistemaAsistencias.infrastructure.mapper.StudentMapper;
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
    public Optional<Student> findById(String dni){
        return jpaRepository.findById(dni).map(studentMapper::toDomain);
    }

    @Override
    public List<Student> getAllStudents(){
        return jpaRepository.findAll().stream().map(studentMapper::toDomain).toList();
    }

    @Override
    public Student save(Student student){
        return studentMapper.toDomain(jpaRepository.save(studentMapper.toEntity(student)));
    }
}
