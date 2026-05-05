package com.IEASmart.sistemaAsistencias.infrastructure.mapper;

import com.IEASmart.sistemaAsistencias.domain.model.Class;
import com.IEASmart.sistemaAsistencias.domain.model.Student;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.ClassEntity;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.StudentEntity;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
    private final ClassMapper classMapper;

    public StudentMapper(ClassMapper classMapper) {
        this.classMapper = classMapper;
    }

    public Student toDomain(StudentEntity entity) {
        if (entity == null) {
            return null;
        }

        Student student = new Student();
        student.setDni(entity.getDni());
        student.setName(entity.getName());
        student.setFirstLastName(entity.getFirstLastName());
        student.setSecondLastName(entity.getSecondLastName());

        Class classSchool = classMapper.toDomain(entity.getClassInfo());
        student.setClassSchool(classSchool);

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

        ClassEntity classEntity = classMapper.toEntity(student.getClassSchool());
        entity.setClassInfo(classEntity);

        return entity;
    }
}
