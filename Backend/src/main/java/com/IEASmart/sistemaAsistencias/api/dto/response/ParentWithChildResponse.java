package com.IEASmart.sistemaAsistencias.api.dto.response;

import com.IEASmart.sistemaAsistencias.domain.model.Student;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;

public class ParentWithChildResponse {
    private Long parentId;
    private String names;
    private String phoneNumber;
    private List<StudentResponse> children;
    private Integer totalChildren;

    public ParentWithChildResponse() {
        this.children = new ArrayList<>();
    }

    public ParentWithChildResponse(Long parentId, String names, String phoneNumber, List<StudentResponse> children, Integer totalChildren) {
        this.parentId = parentId;
        this.names = names;
        this.phoneNumber = phoneNumber;
        this.children = children;
        this.totalChildren = totalChildren;
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

    public List<StudentResponse> getChildren() {
        return children;
    }

    public void setChildren(List<StudentResponse> children) {
        this.children = children;
    }

    public Integer getTotalChildren() {
        return totalChildren;
    }

    public void setTotalChildren(Integer totalChildren) {
        this.totalChildren = totalChildren;
    }
}
