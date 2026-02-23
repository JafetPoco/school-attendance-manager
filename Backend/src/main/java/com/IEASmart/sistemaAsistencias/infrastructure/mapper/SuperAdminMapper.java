package com.IEASmart.sistemaAsistencias.infrastructure.mapper;

import com.IEASmart.sistemaAsistencias.domain.model.SuperAdmin;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.SuperAdminEntity;
import org.springframework.stereotype.Component;

@Component
public class SuperAdminMapper {
    public SuperAdmin toDomain(SuperAdminEntity entity){
        if(entity == null) return null;
        SuperAdmin superAdmin = new SuperAdmin();
        superAdmin.setUserId(entity.getUserId());
        superAdmin.setNames(entity.getNames());
        superAdmin.setFirstLastName(entity.getFirstLastName());
        superAdmin.setSecondLastName(entity.getSecondLastName());
        superAdmin.setEmail(entity.getEmail());
        superAdmin.setUserType(entity.getUserType());
        superAdmin.setSchool(entity.getSchool());

        return superAdmin;
    }

    public SuperAdminEntity toEntity(SuperAdmin superAdmin){
        if(superAdmin == null) return null;
        SuperAdminEntity entity = new SuperAdminEntity();
        entity.setUserId(superAdmin.getUserId());
        entity.setNames(superAdmin.getNames());
        entity.setFirstLastName(superAdmin.getFirstLastName());
        entity.setSecondLastName(superAdmin.getSecondLastName());
        entity.setEmail(superAdmin.getEmail());
        entity.setUserType(superAdmin.getUserType());
        entity.setSchool(superAdmin.getSchool());

        return entity;
    }
}
