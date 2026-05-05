package com.IEASmart.sistemaAsistencias.api.dto.request;

import java.util.ArrayList;
import java.util.List;

public class ParentWithChildRequest {
    private String names;
    private String phoneNumber;
    private List<StudentRequest> children;

    public ParentWithChildRequest() {
        this.children = new ArrayList<>();
    }

    public ParentWithChildRequest(String names, String phoneNumber, List<StudentRequest> children) {
        this.names = names;
        this.phoneNumber = phoneNumber;
        this.children = children;
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

    public List<StudentRequest> getChildren() {
        return children;
    }

    public void setChildren(List<StudentRequest> children) {
        this.children = children;
    }
}
