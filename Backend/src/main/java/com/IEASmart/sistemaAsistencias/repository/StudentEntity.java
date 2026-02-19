package com.IEASmart.sistemaAsistencias.repository;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Grade;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Level;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Section;
import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class StudentEntity {
    @Id
    @Column(name = "dni", nullable = false, length = 8)
    private String dni;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "first_last_name", nullable = false, length = 50)
    private String firstLastName;

    @Column(name = "second_last_name", nullable = false, length = 50)
    private String secondLastName;

    @Column(name = "level", nullable = false)
    private Level level;

    @Column(name = "grade", nullable = false)
    private Grade grade;

    @Column(name = "section", nullable = false)
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ParentEntity parent;

    public StudentEntity() {}

    public StudentEntity(String dni, String name, String firstLastName, String secondLastName, Level level, Grade grade, Section section, ParentEntity parent) {
        this.dni = dni;
        this.name = name;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.level = level;
        this.grade = grade;
        this.section = section;
        this.parent = parent;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public ParentEntity getParent() {
        return parent;
    }

    public void setParent(ParentEntity parent) {
        this.parent = parent;
    }
}
