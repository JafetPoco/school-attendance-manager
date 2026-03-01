package com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.UserType;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "names", nullable = false, length = 100)
    private String names;

    @Column(name = "first_last_name", nullable = false, length = 50)
    private String firstLastName;

    @Column(name = "second_last_name", nullable = false, length = 50)
    private String secondLastName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "school_id", nullable = true)
    private School school;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    public UserEntity() {
    }

    public UserEntity(String names, String firstLastName, String secondLastName, String email, School school, UserType userType) {
        this.names = names;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.email = email;
        this.school = school;
        this.userType = userType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public void setFirstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}