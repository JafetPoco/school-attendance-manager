package com.IEASmart.sistemaAsistencias.domain.model;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.UserType;

public class Professor extends User {
    public Professor(int userId, String names, String firstLastName, String secondLastName, String email) {
        super(userId, names, firstLastName, secondLastName, email, UserType.PROFESSOR);
    }
}
