package com.IEASmart.sistemaAsistencias.api.mapper;

import com.IEASmart.sistemaAsistencias.api.dto.request.JustificationProfessorRequest;
import com.IEASmart.sistemaAsistencias.api.dto.request.JustificationRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.JustificationResponse;
import com.IEASmart.sistemaAsistencias.domain.model.Justification;
import com.IEASmart.sistemaAsistencias.domain.model.Student;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.JustificationStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class JustificationApiMapper {
    public JustificationResponse toResponse(Justification justification) {
        JustificationResponse response = new JustificationResponse();
        response.setId(justification.getId());
        response.setDescription(justification.getDescription());
        response.setJustificationDate(justification.getJustificationDate());
        response.setAttendanceDate(justification.getAttendance().getDate());
        Student student = justification.getAttendance().getStudent();
        response.setStudentName(student.getName() + " " + student.getFirstLastName() + " " + student.getSecondLastName());
        response.setUrlEvidence(justification.getUrlEvidence());
        return response;
    }

    public Justification toDomain(JustificationRequest request) {
        Justification justification = new Justification();
        justification.setDescription(request.getDescription());
        justification.setUrlEvidence(request.getUrlEvidence());
        justification.setJustificationDate(LocalDateTime.now());
        return justification;
    }

    public Justification toProfessorDomain(JustificationProfessorRequest request) {
        Justification justification = new Justification();
        justification.setStatus(JustificationStatus.ACEPTADA);
        justification.setDescription(request.getDescription());
        justification.setJustificationDate(LocalDateTime.now());
        return justification;
    }
}
