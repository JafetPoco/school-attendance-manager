package com.IEASmart.sistemaAsistencias.repository;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "parents")
public class ParentEntity {

    @Id
    @Column(name = "parent_id", nullable = false)
    private Long parentId;

    @Column(name = "names", nullable = false, length = 100)
    private String names;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudentEntity> children;

    public ParentEntity() {}

    public ParentEntity(Long parentId, String names, String phoneNumber, List<StudentEntity> children) {
        this.parentId = parentId;
        this.names = names;
        this.phoneNumber = phoneNumber;
        this.children = children;
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

    public List<StudentEntity> getChildren() {
        return children;
    }

    public void setChildren(List<StudentEntity> children) {
        this.children = children;
    }

    public void addChild(StudentEntity child) {
        children.add(child);
        child.setParent(this);
    }
}
