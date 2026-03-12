package com.IEASmart.sistemaAsistencias.api.controller;

import com.IEASmart.sistemaAsistencias.api.dto.request.AttendanceFilter;
import com.IEASmart.sistemaAsistencias.api.dto.request.AttendanceMonthlyFilter;
import com.IEASmart.sistemaAsistencias.api.dto.request.AttendanceRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.AttendanceResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.MonthlyAttendanceResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.PageResponse;
import com.IEASmart.sistemaAsistencias.application.service.AttendanceService;
import com.IEASmart.sistemaAsistencias.application.service.AuthorizationService;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

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

    @GetMapping()
    public PageResponse<AttendanceResponse> getAll(
            AttendanceFilter filter,
            Pageable page
    ) {
        School school = authorizationService.getUserSchool();
        return attendanceService.getAllAttendaces(school, filter, page);
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<MonthlyAttendanceResponse>> getMonthlyAttendance(
            AttendanceMonthlyFilter filter
    ) {
        School school = authorizationService.getUserSchool();
        List<MonthlyAttendanceResponse> response = attendanceService.getMonthlyAttendance(school, filter);
        return ResponseEntity.ok(response);
    }
}
