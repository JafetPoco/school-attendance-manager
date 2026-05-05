package com.IEASmart.sistemaAsistencias.infrastructure.mapper;

import com.IEASmart.sistemaAsistencias.domain.model.Class;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.ClassEntity;
import org.springframework.stereotype.Component;

@Component
public class ClassMapper {
    public ClassEntity toEntity(Class domain){
        if(domain == null){
            return null;
        }

        ClassEntity entity = new ClassEntity();
        entity.setId(domain.getId());
        entity.setSection(domain.getSection());
        entity.setGrade(domain.getGrade());
        entity.setLevel(domain.getLevel());
        entity.setSchool(domain.getSchool());
        return entity;
    }

    public Class toDomain(ClassEntity entity){
        if(entity == null){
            return null;
        }

        Class domain = new Class();
        domain.setId(entity.getId());
        domain.setSection(entity.getSection());
        domain.setGrade(entity.getGrade());
        domain.setLevel(entity.getLevel());
        domain.setSchool(entity.getSchool());
        return domain;
    }
}
