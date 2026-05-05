package com.IEASmart.sistemaAsistencias.infrastructure.mapper;

import com.IEASmart.sistemaAsistencias.domain.model.Parent;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.ParentEntity;
import org.springframework.stereotype.Component;

@Component
public class ParentMapper {
    private final StudentMapper studentMapper;

    public ParentMapper(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    public Parent toDomain(ParentEntity entity) {
        if (entity == null) {
            return null;
        }

        Parent parent = new Parent();
        parent.setParentId(entity.getParentId());
        parent.setNames(entity.getNames());
        parent.setPhoneNumber(entity.getPhoneNumber());
        parent.setSchool(entity.getSchool());

        if(entity.getChildren() != null) {
            entity.getChildren().forEach(entityChild -> {
                parent.addChild(studentMapper.toDomain(entityChild));
            });
        }
        return parent;
    }

    public ParentEntity toEntity(Parent parent) {
        if (parent == null) {
            return null;
        }

        ParentEntity entity = new ParentEntity();
        entity.setParentId(parent.getParentId());
        entity.setNames(parent.getNames());
        entity.setPhoneNumber(parent.getPhoneNumber());
        entity.setSchool(parent.getSchool());

        parent.getChildren().forEach(domainChild -> {
            entity.addChild(studentMapper.toEntity(domainChild));
        });
        return entity;
    }
}
