package com.IEASmart.sistemaAsistencias.domain.model;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.UserType;

public class User {
    private Long userId;
    private String names;
    private String firstLastName;
    private String secondLastName;
    private String email;
    private String pictureUrl;

    public User() {}

    public User(Long userId, String names, String firstLastName, String secondLastName, String email, String pictureUrl) {
        this.userId = userId;
        this.names = names;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.email = email;
        this.pictureUrl = pictureUrl;
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

    public String getPictureUrl() { return pictureUrl; }

    public void setPictureUrl(String pictureUrl) { this.pictureUrl = pictureUrl; }

}
