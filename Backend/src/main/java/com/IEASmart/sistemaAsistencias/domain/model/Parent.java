package com.IEASmart.sistemaAsistencias.domain.model;

import java.util.ArrayList;
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

    public Parent() {
        this.children = new ArrayList<>();
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

    public List<Student> getChildren() {
        if (this.children == null) this.children = new ArrayList<>();
        return children;
    }

    public void setChildren(List<Student> children) {
        this.children = (children == null) ? new ArrayList<>() : children;
    }

    public void addChild(Student child) {
        if (child == null) return;
        if (this.children == null) this.children = new ArrayList<>();
        if (!this.children.contains(child)) {
            this.children.add(child);
        }
    }

    public void removeChild(Student child) {
        if (child == null) return;
        if (this.children == null) return;
        this.children.remove(child);
    }
}
