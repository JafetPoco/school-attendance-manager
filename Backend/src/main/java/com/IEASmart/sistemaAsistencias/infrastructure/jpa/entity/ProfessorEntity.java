package com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.UserType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "professors")
public class ProfessorEntity extends UserEntity {

    public ProfessorEntity() {
        super();
        setUserType(UserType.PROFESSOR);
    }

    public ProfessorEntity(String names, String firstLastName, String secondLastName, String email, School school) {
        super(names, firstLastName, secondLastName, email, school, UserType.PROFESSOR);
    }
}
