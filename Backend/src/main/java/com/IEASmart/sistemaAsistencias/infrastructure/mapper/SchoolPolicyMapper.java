package com.IEASmart.sistemaAsistencias.infrastructure.mapper;

import com.IEASmart.sistemaAsistencias.domain.model.SchoolPolicy;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.SchoolPolicyEntity;
import org.springframework.stereotype.Component;

@Component
public class SchoolPolicyMapper {
    public SchoolPolicy toDomain(SchoolPolicyEntity entity){
        SchoolPolicy schoolPolicy = new SchoolPolicy();
        schoolPolicy.setId(entity.getId());
        schoolPolicy.setLateAttendaceTime(entity.getLateAttendaceTime());
        schoolPolicy.setJustificationExpirationDays(entity.getJustificationExpirationDays());
        schoolPolicy.setSchool(entity.getSchool());

        return schoolPolicy;
    }

    public SchoolPolicyEntity toEntity(SchoolPolicy schoolPolicy){
        SchoolPolicyEntity entity = new SchoolPolicyEntity();
        entity.setId(schoolPolicy.getId());
        entity.setLateAttendaceTime(schoolPolicy.getLateAttendaceTime());
        entity.setJustificationExpirationDays(schoolPolicy.getJustificationExpirationDays());
        entity.setSchool(schoolPolicy.getSchool());

        return entity;
    }
}
