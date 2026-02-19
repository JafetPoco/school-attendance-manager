package com.IEASmart.sistemaAsistencias.domain.model;

import java.util.List;

public class Parent {
    private Long parentId;
    private String names;
    private String phoneNumber;
    private List<Student> children;

    public Parent(Long parentId, String names, String phoneNumber, List<Student> children) {
        this.parentId = parentId;
        this.names = names;
        this.phoneNumber = phoneNumber;
        this.children = children;
    }

    public Parent() {}

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

    public List<Student> getChildren() {
        return children;
    }

    public void setChildren(List<Student> children) {
        this.children = children;
    }

    public void addChild(Student child) {
        this.children.add(child);
    }

    public void removeChild(Student child) {
        this.children.remove(child);
    }
}
