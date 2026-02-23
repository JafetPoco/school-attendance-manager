package com.IEASmart.sistemaAsistencias.domain.model;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.UserType;

public class Professor extends User {
    public Professor(Long userId, String names, String firstLastName, String secondLastName, String email, School school) {
        super(userId, names, firstLastName, secondLastName, email, school, UserType.PROFESSOR);
    }

    public Professor() {
        super();
        setUserType(UserType.PROFESSOR);
    }
}
