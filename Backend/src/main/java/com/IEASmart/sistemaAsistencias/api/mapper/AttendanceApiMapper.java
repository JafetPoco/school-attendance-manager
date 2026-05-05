package com.IEASmart.sistemaAsistencias.api.mapper;

import com.IEASmart.sistemaAsistencias.api.dto.request.AttendanceRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.AttendanceInfoResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.AttendanceResponse;
import com.IEASmart.sistemaAsistencias.domain.model.Attendance;
import org.springframework.stereotype.Component;

@Component
public class AttendanceApiMapper {
    public AttendanceResponse toResponse(Attendance attendance) {
        AttendanceResponse response = new AttendanceResponse();
        response.setDni(attendance.getStudent().getDni());
        response.setStudentName(attendance.getStudent().getName());
        response.setStudentFirstLastName(attendance.getStudent().getFirstLastName());
        response.setStudentSecondLastName(attendance.getStudent().getSecondLastName());
        response.setAttendanceType(attendance.getAttendanceType().getFullName());
        response.setDate(attendance.getDate().toString());
        response.setIdAttendance(attendance.getId());
        return response;
    }

    public Attendance toDomain(AttendanceRequest request) {
        Attendance attendance = new Attendance();
        attendance.setAttendanceType(request.getAttendanceType());
        return attendance;
    }

    public AttendanceInfoResponse toInfoResponse(Attendance attendance) {
        AttendanceInfoResponse response = new AttendanceInfoResponse();
        response.setId(attendance.getId());
        response.setDate(attendance.getDate());
        response.setFullName(attendance.getStudent().getName() + " " + attendance.getStudent().getFirstLastName() + " " + attendance.getStudent().getSecondLastName());
        response.setGrade(attendance.getStudent().getClassSchool().getLevel() + " - " + attendance.getStudent().getClassSchool().getGrade() + " - " + attendance.getStudent().getClassSchool().getSection());
        return response;
    }

}
