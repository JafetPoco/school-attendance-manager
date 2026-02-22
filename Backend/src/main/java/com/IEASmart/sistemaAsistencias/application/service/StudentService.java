package com.IEASmart.sistemaAsistencias.application.service;

import com.IEASmart.sistemaAsistencias.api.dto.response.StudentResponse;
import com.IEASmart.sistemaAsistencias.api.mapper.StudentApiMapper;
import com.IEASmart.sistemaAsistencias.domain.model.Student;
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

    public List<StudentResponse> getAllStudents() {
        List<Student> students = studentRepository.getAllStudents();
        return students.stream()
                .map(student -> {
                    StudentResponse response = studentApiMapper.toResponse(student);
                    parentRepository.findByAlumnoId(student.getDni())
                            .ifPresent(parent -> response.setParentName(parent.getNames()));
                    return response;
                })
                .toList();
    }

    public Optional<StudentResponse> getStudentById(String studentId) {
        return studentRepository.findById(studentId).map(studentApiMapper::toResponse);
    }
}
