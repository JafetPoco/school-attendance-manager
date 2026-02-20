package com.IEASmart.sistemaAsistencias.controller;

import com.IEASmart.sistemaAsistencias.controller.dto.StudentDto;
import com.IEASmart.sistemaAsistencias.domain.model.Student;
import com.IEASmart.sistemaAsistencias.service.StudentService;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable String id) {
        Optional<Student> studentOpt = studentService.getStudentById(id);
        return studentOpt.orElse(null);
    }

    @PostMapping
    public void addStudent(@RequestBody StudentDto dto) {
        studentService.addStudent(dto);
    }
}
