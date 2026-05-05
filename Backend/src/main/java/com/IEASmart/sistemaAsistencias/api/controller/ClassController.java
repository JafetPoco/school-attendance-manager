package com.IEASmart.sistemaAsistencias.api.controller;

import com.IEASmart.sistemaAsistencias.api.dto.request.ClassRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.ClassFullInfoResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.ClassResponse;
import com.IEASmart.sistemaAsistencias.application.service.AuthorizationService;
import com.IEASmart.sistemaAsistencias.application.service.ClassService;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClassController {
    private final AuthorizationService authorizationService;
    private final ClassService classService;

    public ClassController(AuthorizationService authorizationService, ClassService classService) {
        this.authorizationService = authorizationService;
        this.classService = classService;
    }

    @GetMapping("/public")
    public ResponseEntity<List<ClassResponse>> getAllClasses() {
        School school = authorizationService.getUserSchool();
        List<ClassResponse> classes = classService.getAllClasses(school);
        return ResponseEntity.ok(classes);
    }

    @PostMapping()
    public ResponseEntity<ClassResponse> createClasses(@RequestBody ClassRequest request) {
        if(!authorizationService.isAdmin()){
            return ResponseEntity.status(403).build();
        }
        School school = authorizationService.getUserSchool();
        ClassResponse response = classService.createClass(request, school);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<List<ClassFullInfoResponse>> getAllClassesFullInfo() {
        if(!authorizationService.isAdmin()){
            return ResponseEntity.status(403).build();
        }
        School school = authorizationService.getUserSchool();
        List<ClassFullInfoResponse> classes = classService.getAllClassesFullInfo(school);
        return ResponseEntity.ok(classes);
    }

    @PatchMapping("/{classId}")
    public ResponseEntity<ClassFullInfoResponse> updateClass(@PathVariable Long classId, @RequestBody ClassRequest request) {
        if(!authorizationService.isAdmin()){
            return ResponseEntity.status(403).build();
        }
        School school = authorizationService.getUserSchool();
        ClassFullInfoResponse response = classService.updateClass(classId, request, school);
        return ResponseEntity.ok(response);
    }
}
