package com.IEASmart.sistemaAsistencias.domain.model;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.UserType;

public class SuperAdmin extends User {
    public SuperAdmin() {
        super();
        setUserType(UserType.SUPER_ADMIN);
    }

    public SuperAdmin(Long userId, String names, String firstLastName, String secondLastName, String email) {
        super(userId, names, firstLastName, secondLastName, email, null, UserType.SUPER_ADMIN);
    }
}
