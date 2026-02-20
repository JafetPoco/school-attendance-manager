package com.IEASmart.sistemaAsistencias.service;

import com.IEASmart.sistemaAsistencias.controller.dto.StudentDto;
import com.IEASmart.sistemaAsistencias.domain.model.Parent;
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

    public StudentService(StudentRepository studentRepository, ParentRepository parentRepository) {
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.getAllStudents();
    }

    public Optional<Student> getStudentById(String studentId) {
        return studentRepository.findById(studentId);
    }

    public void addStudent(StudentDto dto) {
        if(dto == null) throw new IllegalArgumentException("Student data cannot be null");
        if(parentRepository.findById(dto.getParentId()).isPresent()){
            throw new IllegalArgumentException("Parent with ID " + dto.getParentId() + " does not exist");
        }

        Student student = new Student();
        student.setDni(dto.getDni());
        student.setName(dto.getName());
        student.setFirstLastName(dto.getFirstLastName());
        student.setSecondLastName(dto.getSecondLastName());
        student.setLevel(dto.getLevel());
        student.setGrade(dto.getGrade());
        student.setSection(dto.getSection());

        Parent parent = parentRepository.findById(dto.getParentId()).get();
        parent.addChild(student);

        parentRepository.save(parent);
        studentRepository.save(student);
    }
}
