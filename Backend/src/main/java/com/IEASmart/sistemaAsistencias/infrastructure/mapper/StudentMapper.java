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

        Student student = new Student();
        student.setDni(entity.getDni());
        student.setName(entity.getName());
        student.setFirstLastName(entity.getFirstLastName());
        student.setSecondLastName(entity.getSecondLastName());
        student.setLevel(entity.getLevel());
        student.setGrade(entity.getGrade());
        student.setSection(entity.getSection());
        student.setSchool(entity.getSchool());
        return student;
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
        entity.setSchool(student.getSchool());
        return entity;
    }
}
