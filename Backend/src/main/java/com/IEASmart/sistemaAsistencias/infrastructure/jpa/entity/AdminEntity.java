package com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.UserType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class AdminEntity extends UserEntity {

    public AdminEntity() {
        super();
        setUserType(UserType.ADMIN);
    }

    public AdminEntity(String names, String firstLastName, String secondLastName, String email, School school) {
        super(names, firstLastName, secondLastName, email, school, UserType.ADMIN);
    }
}