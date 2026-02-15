package com.IEASmart.sistemaAsistencias.repository;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@DiscriminatorValue("PROFESSOR")
@Table(name = "professors")
public class ProfessorEntity extends UserEntity {

    public ProfessorEntity() {
        super();
    }

    public ProfessorEntity(String names, String firstLastName, String secondLastName, String email) {
        super(names, firstLastName, secondLastName, email);
    }
}
