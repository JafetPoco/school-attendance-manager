package com.IEASmart.sistemaAsistencias.api.controller;

import com.IEASmart.sistemaAsistencias.api.dto.request.AttendanceRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.AttendanceResponse;
import com.IEASmart.sistemaAsistencias.application.service.AttendanceService;
import com.IEASmart.sistemaAsistencias.application.service.AuthorizationService;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendances")
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final AuthorizationService authorizationService;

    public AttendanceController(AttendanceService attendanceService, AuthorizationService authorizationService) {
        this.attendanceService = attendanceService;
        this.authorizationService = authorizationService;
    }

    @PostMapping()
    public ResponseEntity<AttendanceResponse> markAttendance(@RequestBody AttendanceRequest request) {
        School school = authorizationService.getUserSchool();
        AttendanceResponse response = attendanceService.markAttendance(request, school);
        return ResponseEntity.ok(response);
    }
}
