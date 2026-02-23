package com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.UserType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "super_admin")
public class SuperAdminEntity extends UserEntity {
    public SuperAdminEntity() {
        super();
        setUserType(UserType.SUPER_ADMIN);
    }

    public SuperAdminEntity(Long userId, String names, String firstLastName, String secondLastName, String email) {
        super(names, firstLastName, secondLastName, email, null, UserType.SUPER_ADMIN);
    }
}
