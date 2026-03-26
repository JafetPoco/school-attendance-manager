package com.IEASmart.sistemaAsistencias.api.mapper;

import com.IEASmart.sistemaAsistencias.api.dto.request.JustificationRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.JustificationResponse;
import com.IEASmart.sistemaAsistencias.domain.model.Justification;
import com.IEASmart.sistemaAsistencias.domain.model.Student;
import org.springframework.stereotype.Component;

@Component
public class JustificationApiMapper {
    public JustificationResponse toResponse(Justification justification) {
        JustificationResponse response = new JustificationResponse();
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
        return justification;
    }
}
