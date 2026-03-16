package com.IEASmart.sistemaAsistencias.api.mapper;

import com.IEASmart.sistemaAsistencias.api.dto.request.SchoolPolicyRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.SchoolPolicyResponse;
import com.IEASmart.sistemaAsistencias.domain.model.SchoolPolicy;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class SchoolPolicyApiMapper {
    public SchoolPolicyResponse toResponse(SchoolPolicy schoolPolicy) {
        SchoolPolicyResponse response = new SchoolPolicyResponse();
        response.setLateAttendaceTime(schoolPolicy.getLateAttendaceTime());
        response.setJustificationExpirationDays(schoolPolicy.getJustificationExpirationDays());

        return response;
    }

    public SchoolPolicy toDomain(SchoolPolicyRequest response) {
        SchoolPolicy schoolPolicy = new SchoolPolicy();
        schoolPolicy.setLateAttendaceTime(LocalTime.parse(response.getLateAttendaceTime()));
        schoolPolicy.setJustificationExpirationDays(response.getJustificationExpirationDays());

        return schoolPolicy;
    }
}
