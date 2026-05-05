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

        if(student.getClassSchool().getSection().length() == 1){
            response.setSection(student.getClassSchool().getSection()+"-"+student.getClassSchool().getGrade()+"-"+student.getClassSchool().getLevel());
        } else {
            response.setSection(student.getClassSchool().getSection());
        }
        return response;
    }
}
