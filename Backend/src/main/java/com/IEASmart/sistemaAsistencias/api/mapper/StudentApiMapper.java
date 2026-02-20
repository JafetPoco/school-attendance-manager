package com.IEASmart.sistemaAsistencias.api.mapper;

import com.IEASmart.sistemaAsistencias.api.dto.request.StudentRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.StudentResponse;
import com.IEASmart.sistemaAsistencias.domain.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentApiMapper {
    public static StudentResponse toResponse(Student student){
        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setDni(student.getDni());
        studentResponse.setName(student.getName());
        studentResponse.setFirstLastName(student.getFirstLastName());
        studentResponse.setSecondLastName(student.getSecondLastName());
        studentResponse.setLevel(student.getLevel());
        studentResponse.setGrade(student.getGrade());
        studentResponse.setSection(student.getSection());
        return studentResponse;
    }

    public static Student toDomain(StudentRequest studentRequest){
        Student student = new Student();
        student.setDni(studentRequest.getDni());
        student.setName(studentRequest.getName());
        student.setFirstLastName(studentRequest.getFirstLastName());
        student.setSecondLastName(studentRequest.getSecondLastName());
        student.setLevel(studentRequest.getLevel());
        student.setGrade(studentRequest.getGrade());
        student.setSection(studentRequest.getSection());
        return student;
    }
}
