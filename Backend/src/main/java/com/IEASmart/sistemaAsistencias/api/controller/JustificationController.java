package com.IEASmart.sistemaAsistencias.api.controller;

import com.IEASmart.sistemaAsistencias.api.dto.request.JustificationProfessorRequest;
import com.IEASmart.sistemaAsistencias.api.dto.request.JustificationRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.AttendanceInfoResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.JustificationResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.PageResponse;
import com.IEASmart.sistemaAsistencias.application.service.AttendanceService;
import com.IEASmart.sistemaAsistencias.application.service.AuthorizationService;
import com.IEASmart.sistemaAsistencias.application.service.JustificationService;
import com.IEASmart.sistemaAsistencias.application.service.TokenService;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/justifications")
public class JustificationController {
    private final AuthorizationService authorizationService;
    private final JustificationService justificationService;
    private final AttendanceService attendanceService;
    private final TokenService tokenService;

    public JustificationController(AuthorizationService authorizationService, JustificationService justificationService, AttendanceService attendanceService, TokenService tokenService) {
        this.authorizationService = authorizationService;
        this.justificationService = justificationService;
        this.attendanceService = attendanceService;
        this.tokenService = tokenService;
    }

    @GetMapping("/public/{token}")
    public ResponseEntity<AttendanceInfoResponse> getJustificationForm(@PathVariable String token) {
        String attendanceId = tokenService.getAttendanceIdFromToken(token);
        return ResponseEntity.ok(attendanceService.getAttendanceById(attendanceId));
    }

    @PostMapping("/public/submit")
    public ResponseEntity<JustificationResponse> submitJustification(
            @RequestBody JustificationRequest request) {
        return ResponseEntity.ok(justificationService.createJustification(request));
    }

    @PostMapping("/submit")
    public ResponseEntity<JustificationResponse> submitJustificationForStudent(
            @RequestBody JustificationProfessorRequest request) {
        School school = authorizationService.getUserSchool();
        return ResponseEntity.ok(justificationService.createJustificationForStudent(request));
    }

    @GetMapping("/pending")
    public PageResponse<JustificationResponse> getPendingJustifications(
            @RequestParam(required = false) String dateFilter,
            Pageable pageable
    ) {
        School school = authorizationService.getUserSchool();
        return justificationService.getPendingJustifications(school, dateFilter, pageable);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<JustificationResponse> approveJustification(@PathVariable Long id) {
        return ResponseEntity.ok(justificationService.approveJustification(id));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<JustificationResponse> rejectJustification(@PathVariable Long id) {
        return ResponseEntity.ok(justificationService.rejectJustification(id));
    }

}
