package com.IEASmart.sistemaAsistencias.api.dto.request;

import com.IEASmart.sistemaAsistencias.domain.model.Student;

import java.util.List;

public class ParentRequest {
    private Long parentId;
    private String names;
    private String phoneNumber;

    public ParentRequest() {}

    public ParentRequest(Long parentId, String names, String phoneNumber) {
        this.parentId = parentId;
        this.names = names;
        this.phoneNumber = phoneNumber;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
