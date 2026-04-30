package com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Grade;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Level;
import jakarta.persistence.*;

@Entity
@Table(name = "class")
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "section", nullable = false, length = 50)
    private String Section;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade", nullable = false, length = 50)
    private Grade grade;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false, length = 50)
    private Level level;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public ClassEntity() {}

    public ClassEntity(Long id, String section, Grade grade, Level level, String name) {
        this.id = id;
        Section = section;
        this.grade = grade;
        this.level = level;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSection() {
        return Section;
    }

    public void setSection(String section) {
        Section = section;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}