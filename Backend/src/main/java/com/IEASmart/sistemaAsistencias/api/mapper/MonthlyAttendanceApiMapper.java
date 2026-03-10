package com.IEASmart.sistemaAsistencias.api.mapper;

import com.IEASmart.sistemaAsistencias.api.dto.response.MonthlyAttendanceResponse;
import com.IEASmart.sistemaAsistencias.domain.model.Student;
import org.springframework.stereotype.Component;

@Component
public class MonthlyAttendanceApiMapper {
    public MonthlyAttendanceResponse toResponse(Student student) {
        MonthlyAttendanceResponse response = new MonthlyAttendanceResponse();
        response.setDni(student.getDni());
        response.setName(student.getName());
        response.setLastName(student.getFirstLastName() + " " + student.getSecondLastName());
        response.setSection(student.getSection().toString());

        return response;
    }
}
