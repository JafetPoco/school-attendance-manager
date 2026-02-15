package com.IEASmart.sistemaAsistencias.repository;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@DiscriminatorValue("ADMIN")
@Table(name = "admin")
public class AdminEntity extends UserEntity {
    public AdminEntity(String names, String firstLastName, String secondLastName, String email) {
        super(names, firstLastName, secondLastName, email);
    }
}