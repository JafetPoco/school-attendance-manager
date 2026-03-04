package com.IEASmart.sistemaAsistencias.api.controller;

import com.IEASmart.sistemaAsistencias.api.dto.request.StudentRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.StudentResponse;
import com.IEASmart.sistemaAsistencias.application.service.AuthorizationService;
import com.IEASmart.sistemaAsistencias.application.service.StudentService;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Level;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;
    private final AuthorizationService authorizationService;

    public StudentController(StudentService studentService,
                             AuthorizationService authorizationService) {
        this.studentService = studentService;
        this.authorizationService = authorizationService;
    }

    @GetMapping
    public List<StudentResponse> getAllStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) String section
            ) {
        School school = authorizationService.getUserSchool();
        return studentService.getAllStudents(school, name, level, grade, section);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable String id) {
        School school = authorizationService.getUserSchool();
        Optional<StudentResponse> studentOpt = studentService.getStudentById(id, school);
        return studentOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
