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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        return jpaRepository.findByDniAndClassInfo_School(dni, school).map(studentMapper::toDomain);
    }

    @Override
    public List<Student> getAllStudents(School school){
        return jpaRepository.findAllByClassInfo_School(school).stream().map(studentMapper::toDomain).toList();
    }

    @Override
    public Student save(Student student){
        return studentMapper.toDomain(jpaRepository.save(studentMapper.toEntity(student)));
    }

    private Specification<StudentEntity> buildSpecification(School school, StudentCriteria criteria) {
        return Specification
                .where(StudentSpecifications.hasSchool(school))
                .and(StudentSpecifications.hasName(criteria.name()))
                .and(StudentSpecifications.hasLevel(criteria.level()))
                .and(StudentSpecifications.hasGrade(criteria.grade()))
                .and(StudentSpecifications.hasSection(criteria.section()));
    }

    @Override
    public Page<Student> findAllByFilters(School school, StudentCriteria criteria, Pageable pageable) {
        Specification<StudentEntity> spec = buildSpecification(school, criteria);
        Page<StudentEntity> page = jpaRepository.findAll(spec, pageable);
        return page.map(studentMapper::toDomain);
    }

    @Override
    public List<Student> findAllByClassId(Long classId) {
        return jpaRepository.findAllByClassInfo_IdOrderByFirstLastNameAsc(classId).stream()
                .map(studentMapper::toDomain)
                .toList();
    }

    @Override
    public List<String> findAllWithoutAttendanceOnDate(School school, LocalDate date) {
        return jpaRepository.findAllByClassInfo_SchoolAndWithoutAttendanceOnDate(school, date);
    }

    @Override
    public long countStudentsBySchool(School school) {
        return jpaRepository.countByClassInfo_School(school);
    }

    @Override
    public List<Student> findByNameContainingIgnoreCase(String query, School school) {
        StudentCriteria criteria = new StudentCriteria(query, null, null, null);
        Specification<StudentEntity> spec = buildSpecification(school, criteria);
        List<StudentEntity> list = jpaRepository.findAll(spec);
        return list.stream().map(studentMapper::toDomain).toList();
    }

    @Override
    public Set<String> findAllDnisBySchool(School school){
        return jpaRepository.findExistingDnis(school);
    }

    @Override
    public Student getReferenceById(String dni){
        return studentMapper.toDomain(jpaRepository.getReferenceById(dni));
    }
}
