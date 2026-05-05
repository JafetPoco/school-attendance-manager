package com.IEASmart.sistemaAsistencias.infrastructure.mapper;

import com.IEASmart.sistemaAsistencias.domain.model.Professor;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.ProfessorEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfessorMapper {

    public Professor toDomain(ProfessorEntity entity) {
        if(entity == null) return null;
        Professor professor = new Professor();
        professor.setUserId(entity.getUserId());
        professor.setNames(entity.getNames());
        professor.setFirstLastName(entity.getFirstLastName());
        professor.setSecondLastName(entity.getSecondLastName());
        professor.setEmail(entity.getEmail());
        professor.setUserType(entity.getUserType());
        professor.setSchool(entity.getSchool());

        return professor;
    }

    public ProfessorEntity toEntity(Professor professor) {
        if(professor == null) return null;
        ProfessorEntity entity = new ProfessorEntity();
        entity.setUserId(professor.getUserId());
        entity.setNames(professor.getNames());
        entity.setFirstLastName(professor.getFirstLastName());
        entity.setSecondLastName(professor.getSecondLastName());
        entity.setEmail(professor.getEmail());
        entity.setUserType(professor.getUserType());
        entity.setSchool(professor.getSchool());

        return entity;
    }
}
