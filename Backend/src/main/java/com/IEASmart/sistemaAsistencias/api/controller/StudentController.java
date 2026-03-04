package com.IEASmart.sistemaAsistencias.api.controller;

import com.IEASmart.sistemaAsistencias.api.dto.request.StudentFilter;
import com.IEASmart.sistemaAsistencias.api.dto.response.PageResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.StudentResponse;
import com.IEASmart.sistemaAsistencias.application.service.AuthorizationService;
import com.IEASmart.sistemaAsistencias.application.service.StudentService;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public PageResponse<StudentResponse> getAllStudents(
            StudentFilter filter,
            Pageable pageable
    ) {
        School school = authorizationService.getUserSchool();
        return studentService.getAllStudents(school, filter, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable String id) {
        School school = authorizationService.getUserSchool();
        Optional<StudentResponse> studentOpt = studentService.getStudentById(id, school);
        return studentOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
