package com.IEASmart.sistemaAsistencias.api.controller;

import com.IEASmart.sistemaAsistencias.api.dto.request.AttendanceFilter;
import com.IEASmart.sistemaAsistencias.api.dto.request.AttendanceMonthlyFilter;
import com.IEASmart.sistemaAsistencias.api.dto.request.AttendanceRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.*;
import com.IEASmart.sistemaAsistencias.application.service.AttendanceService;
import com.IEASmart.sistemaAsistencias.application.service.AuthorizationService;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    @GetMapping("/monthly/excel")
    public ResponseEntity<byte[]> exportAllSectionsMonthlyExcel(
            @RequestParam int month,
            @RequestParam int year) {

        // Validaciones básicas
        if (month < 1 || month > 12 || year <= 0) {
            return ResponseEntity.badRequest().build();
        }

        School school = authorizationService.getUserSchool();
        byte[] excelBytes;
        try {
            excelBytes = attendanceService.getMonthlyAttendanceExcelAllSections(school, month, year);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }

        if (excelBytes == null || excelBytes.length == 0) {
            return ResponseEntity.noContent().build();
        }

        String safeSchool = school.toString().replaceAll("[^a-zA-Z0-9-_\\. ]", "_");
        String filename = String.format("asistencia_colegio_%s_%d_%02d.xlsx", safeSchool, year, month);
        String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + filename + "\"; filename*=UTF-8''" + encoded;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(excelBytes.length)
                .body(excelBytes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InformationAttendanceResponse> getById(@PathVariable String id) {
        School school = authorizationService.getUserSchool();
        InformationAttendanceResponse response = attendanceService.getAttendanceByStudentId(id, school);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/missed")
    public ResponseEntity<CountResponse> createMissedAttendances() {
        School school = authorizationService.getUserSchool();
        long count = attendanceService.addMissedAttendances(school);
        return ResponseEntity.ok(new CountResponse(count));
    }

    @GetMapping("/contact/{attendanceId}")
    public ResponseEntity<ContactResponse> getContactInfo(@PathVariable String attendanceId) {
        ContactResponse response = attendanceService.getContactInfo(attendanceId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<AttendanceTodayDashboard> getTodayAttendanceInfo() {
        School school = authorizationService.getUserSchool();
        AttendanceTodayDashboard response = attendanceService.getTodayAttendanceInfo(school);
        return ResponseEntity.ok(response);
    }

}
