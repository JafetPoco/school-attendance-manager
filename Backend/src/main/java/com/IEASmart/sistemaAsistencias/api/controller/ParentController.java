package com.IEASmart.sistemaAsistencias.api.controller;

import com.IEASmart.sistemaAsistencias.api.dto.request.ParentWithChildRequest;
import com.IEASmart.sistemaAsistencias.api.dto.request.StudentRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.ImportResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.ParentResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.ParentWithChildResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.StudentResponse;
import com.IEASmart.sistemaAsistencias.application.service.AuthorizationService;
import com.IEASmart.sistemaAsistencias.application.service.ParentService;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/parents")
public class ParentController {
    private final ParentService parentService;
    private final AuthorizationService authorizationService;

    public ParentController(ParentService parentService,
                            AuthorizationService authorizationService) {
        this.parentService = parentService;
        this.authorizationService = authorizationService;
    }

    @PostMapping("/{parentId}/student")
    public ResponseEntity<StudentResponse> addChild(
            @PathVariable Long parentId,
            @RequestBody StudentRequest request) {

        School school = authorizationService.getUserSchool();
        StudentResponse response = parentService.addChildToParent(parentId, request, school);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/with-children")
    public ResponseEntity<ParentResponse> createParentWithChildren(@RequestBody ParentWithChildRequest request) {
        School school = authorizationService.getUserSchool();
        ParentResponse response = parentService.addParentWithChildren(request, school);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{parentId}/students/batch")
    public ResponseEntity<List<StudentResponse>> addManyChildToParent(
            @PathVariable Long parentId,
            @RequestBody List<StudentRequest> requests) {
        School school = authorizationService.getUserSchool();
        List<StudentResponse> responses = parentService.addChildrenToParent(parentId, requests, school);

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/import")
    public ResponseEntity<ImportResponse> importExcel(@RequestParam("file") MultipartFile file) {
        School school = authorizationService.getUserSchool();
        parentService.importFromExcel(file, school);
        ImportResponse response = new ImportResponse("Exito", "Archivo importado correctamente");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/template")
    public ResponseEntity<Resource> descargarPlantillaExcel() {
        try {
            // Cargar el archivo desde resources
            Resource resource = new ClassPathResource("templates/studentsTemplate.xlsx");

            // Verificar que existe
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"studentsTemplate.xlsx\"")
                    .contentType(MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ParentResponse>> getAllParents() {
        School school = authorizationService.getUserSchool();
        List<ParentResponse> parents = parentService.getAllParents(school);
        return ResponseEntity.ok(parents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParentWithChildResponse> getParentById(@PathVariable Long id) {
        School school = authorizationService.getUserSchool();
        return parentService.getParentById(id, school)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{parentId}/students")
    public ResponseEntity<List<StudentResponse>> getChildren(@PathVariable Long parentId) {
        School school = authorizationService.getUserSchool();
        List<StudentResponse> responses = parentService.getChildrenOfParent(parentId, school);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentById(@PathVariable String id) {
        School school = authorizationService.getUserSchool();
        parentService.removeChildByDni(id, school);
        return ResponseEntity.noContent().build();
    }
}
