package com.IEASmart.sistemaAsistencias.infrastructure.mapper;

import com.IEASmart.sistemaAsistencias.domain.model.Admin;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.AdminEntity;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {
    public Admin toDomain(AdminEntity entity){
        if(entity == null) return null;
        Admin admin = new Admin();
        admin.setUserId(entity.getUserId());
        admin.setNames(entity.getNames());
        admin.setFirstLastName(entity.getFirstLastName());
        admin.setSecondLastName(entity.getSecondLastName());
        admin.setEmail(entity.getEmail());
        admin.setUserType(entity.getUserType());
        admin.setSchool(entity.getSchool());

        return admin;
    }

    public AdminEntity toEntity(Admin admin){
        if(admin == null) return null;
        AdminEntity entity = new AdminEntity();
        entity.setUserId(admin.getUserId());
        entity.setNames(admin.getNames());
        entity.setFirstLastName(admin.getFirstLastName());
        entity.setSecondLastName(admin.getSecondLastName());
        entity.setEmail(admin.getEmail());
        entity.setUserType(admin.getUserType());
        entity.setSchool(admin.getSchool());

        return entity;
    }
}
