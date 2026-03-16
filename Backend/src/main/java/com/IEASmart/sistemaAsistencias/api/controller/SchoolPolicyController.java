package com.IEASmart.sistemaAsistencias.api.controller;

import com.IEASmart.sistemaAsistencias.api.dto.request.SchoolPolicyRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.SchoolPolicyResponse;
import com.IEASmart.sistemaAsistencias.application.service.AuthorizationService;
import com.IEASmart.sistemaAsistencias.application.service.SchoolPolicyService;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/policies")
public class SchoolPolicyController {
    private final SchoolPolicyService schoolPolicyService;
    private final AuthorizationService authorizationService;

    public SchoolPolicyController(SchoolPolicyService schoolPolicyService, AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
        this.schoolPolicyService = schoolPolicyService;
    }

    @PostMapping()
    public SchoolPolicyResponse createPolicy(@RequestBody SchoolPolicyRequest request) {
        School school = authorizationService.getUserSchool();
        return schoolPolicyService.createSchoolPolicy(request, school);
    }

    @GetMapping()
    public SchoolPolicyResponse getPolicy() {
        School school = authorizationService.getUserSchool();
        return schoolPolicyService.getSchoolPolicy(school);
    }

    @PatchMapping()
    public SchoolPolicyResponse patchPolicy(@RequestBody SchoolPolicyRequest request) {
        School school = authorizationService.getUserSchool();
        return schoolPolicyService.updateSchoolPolicy(request, school);
    }
}
