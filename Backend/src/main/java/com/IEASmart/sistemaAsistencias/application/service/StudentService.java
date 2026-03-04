package com.IEASmart.sistemaAsistencias.application.service;

import com.IEASmart.sistemaAsistencias.api.dto.response.StudentResponse;
import com.IEASmart.sistemaAsistencias.api.mapper.StudentApiMapper;
import com.IEASmart.sistemaAsistencias.domain.model.Student;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Level;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Grade;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Section;
import com.IEASmart.sistemaAsistencias.domain.repository.ParentRepository;
import com.IEASmart.sistemaAsistencias.domain.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final StudentApiMapper studentApiMapper;

    public StudentService(StudentRepository studentRepository, ParentRepository parentRepository, StudentApiMapper studentApiMapper) {
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.studentApiMapper = studentApiMapper;
    }

    public List<StudentResponse> getAllStudents(School school, String name, String level, String grade, String section) {
        Level levelEnum = Level.from(level);
        Grade gradeEnum = Grade.from(grade);
        Section sectionEnum = Section.from(section);

        List<Student> students = studentRepository.findAllByFilters(school, name, levelEnum, gradeEnum, sectionEnum);
        return students.stream().map(studentApiMapper::toResponse).toList();
    }

    public Optional<StudentResponse> getStudentById(String studentId, School school) {
        return studentRepository.findById(studentId, school).map(studentApiMapper::toResponse);
    }
}
