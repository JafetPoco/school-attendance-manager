package com.IEASmart.sistemaAsistencias.domain.model;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.UserType;

public class Professor extends User {
    private final UserType userType = UserType.PROFESSOR;

    public Professor(Long userId, String names, String firstLastName, String secondLastName, String email, String pictureUrl) {
        super(userId, names, firstLastName, secondLastName, email, pictureUrl);
    }

    public Professor() {
        super();
    }

    public UserType getUserType() {
        return userType;
    }
}
