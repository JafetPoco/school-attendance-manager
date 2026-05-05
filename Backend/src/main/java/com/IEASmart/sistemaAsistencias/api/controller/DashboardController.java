package com.IEASmart.sistemaAsistencias.api.controller;

import com.IEASmart.sistemaAsistencias.api.dto.response.DashboardResponse;
import com.IEASmart.sistemaAsistencias.application.service.AuthorizationService;
import com.IEASmart.sistemaAsistencias.application.service.DashboardService;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final AuthorizationService authorizationService;
    private final DashboardService dashboardService;

    public DashboardController(AuthorizationService authorizationService, DashboardService dashboardService) {
        this.authorizationService = authorizationService;
        this.dashboardService = dashboardService;
    }

    @GetMapping()
    public ResponseEntity<DashboardResponse> getDashboard() {
        School school = authorizationService.getUserSchool();
        DashboardResponse response = dashboardService.getDashboardData(school);
        return ResponseEntity.ok(response);
    }
}
