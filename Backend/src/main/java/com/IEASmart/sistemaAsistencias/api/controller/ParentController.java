package com.IEASmart.sistemaAsistencias.api.controller;

import com.IEASmart.sistemaAsistencias.api.dto.request.ParentWithChildRequest;
import com.IEASmart.sistemaAsistencias.api.dto.request.StudentRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.ParentResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.ParentWithChildResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.StudentResponse;
import com.IEASmart.sistemaAsistencias.application.service.ParentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parents")
public class ParentController {
    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @GetMapping
    public ResponseEntity<List<ParentResponse>> getAllParents() {
        List<ParentResponse> parents = parentService.getAllParents();
        return ResponseEntity.ok(parents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParentWithChildResponse> getParentById(@PathVariable Long id) {
        return parentService.getParentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{parentId}/students")
    public ResponseEntity<StudentResponse> addChild(
            @PathVariable Long parentId,
            @RequestBody StudentRequest request) {

        StudentResponse response = parentService.addChildToParent(parentId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/with-children")
    public ResponseEntity<ParentResponse> createParentWithChild(@RequestBody ParentWithChildRequest request) {
        ParentResponse response = parentService.addParentWithChild(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{parentId}/students/batch")
    public ResponseEntity<List<StudentResponse>> addManyChildToParent(
            @PathVariable Long parentId,
            @RequestBody List<StudentRequest> requests) {

        List<StudentResponse> responses = parentService.addChildrenToParent(parentId, requests);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{parentId}/students")
    public ResponseEntity<List<StudentResponse>> obtenerHijos(@PathVariable Long parentId) {
        List<StudentResponse> responses = parentService.getChildrenOfParent(parentId);
        return ResponseEntity.ok(responses);
    }

}
