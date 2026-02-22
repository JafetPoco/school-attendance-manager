package com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parents")
public class ParentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parent_id", nullable = false)
    private Long parentId;

    @Column(name = "names", nullable = false, length = 100)
    private String names;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "school", nullable = false)
    private School school;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudentEntity> children;

    public ParentEntity() {
        this.children = new ArrayList<>();
    }

    public ParentEntity(String names, String phoneNumber, School school, List<StudentEntity> children) {
        this.names = names;
        this.phoneNumber = phoneNumber;
        this.school = school;
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

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public List<StudentEntity> getChildren() {
        if (this.children == null) this.children = new ArrayList<>();
        return children;
    }

    public void setChildren(List<StudentEntity> children) {
        this.children = (children == null) ? new ArrayList<>() : children;
    }

    public void addChild(StudentEntity child) {
        if (child == null) return;
        if (this.children == null) this.children = new ArrayList<>();
        if (!this.children.contains(child)) {
            children.add(child);
            child.setParent(this);
        }
    }
}
