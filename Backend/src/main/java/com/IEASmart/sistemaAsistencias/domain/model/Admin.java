package com.IEASmart.sistemaAsistencias.domain.model;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.UserType;

public class Admin extends User {
    public Admin(Long userId, String names, String firstLastName, String secondLastName, String email, School school) {
        super(userId, names, firstLastName, secondLastName, email, school, UserType.ADMIN);
    }

    public Admin() {
        super();
        setUserType(UserType.ADMIN);
    }
}
