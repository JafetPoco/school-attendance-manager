package com.IEASmart.sistemaAsistencias.infrastructure.mapper;

import com.IEASmart.sistemaAsistencias.domain.model.Student;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.StudentEntity;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
    public Student toDomain(StudentEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Student(
                entity.getDni(),
                entity.getName(),
                entity.getFirstLastName(),
                entity.getSecondLastName(),
                entity.getLevel(),
                entity.getGrade(),
                entity.getSection()
        );
    }

    public StudentEntity toEntity(Student student) {
        if (student == null) {
            return null;
        }
        StudentEntity entity = new StudentEntity();
        entity.setDni(student.getDni());
        entity.setName(student.getName());
        entity.setFirstLastName(student.getFirstLastName());
        entity.setSecondLastName(student.getSecondLastName());
        entity.setLevel(student.getLevel());
        entity.setGrade(student.getGrade());
        entity.setSection(student.getSection());
        return entity;
    }
}
