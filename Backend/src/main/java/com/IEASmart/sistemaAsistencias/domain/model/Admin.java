package com.IEASmart.sistemaAsistencias.domain.model;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.UserType;

public class Admin extends User {
    private final UserType userType = UserType.ADMIN;

    public Admin(Long userId, String names, String firstLastName, String secondLastName, String email, String pictureUrl) {
        super(userId, names, firstLastName, secondLastName, email, pictureUrl);
    }

    public Admin() {
        super();
    }

    public UserType getUserType() {
        return userType;
    }
}
