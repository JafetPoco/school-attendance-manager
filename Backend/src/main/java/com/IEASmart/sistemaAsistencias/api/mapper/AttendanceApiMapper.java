package com.IEASmart.sistemaAsistencias.api.mapper;

import com.IEASmart.sistemaAsistencias.api.dto.request.AttendanceRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.AttendanceResponse;
import com.IEASmart.sistemaAsistencias.domain.model.Attendance;
import org.springframework.stereotype.Component;

@Component
public class AttendanceApiMapper {
    public AttendanceResponse toResponse(Attendance attendance) {
        AttendanceResponse response = new AttendanceResponse();
        response.setDni(attendance.getStudent().getDni());
        response.setStudentName(attendance.getStudent().getName());
        response.setAttendanceType(attendance.getAttendanceType().getFullName());
        response.setDate(attendance.getDate().toString());
        return response;
    }

    public Attendance toDomain(AttendanceRequest request) {
        Attendance attendance = new Attendance();
        attendance.setAttendanceType(request.getAttendanceType());
        return attendance;
    }


}
