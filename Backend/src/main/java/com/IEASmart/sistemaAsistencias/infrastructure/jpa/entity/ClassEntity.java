package com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Grade;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Level;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import jakarta.persistence.*;

@Entity
@Table(name = "class")
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "section", nullable = false, length = 50)
    private String section;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade", nullable = false, length = 50)
    private Grade grade;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false, length = 50)
    private Level level;

    @Enumerated(EnumType.STRING)
    @Column(name = "school", nullable = false, length = 50)
    private School school;

    public ClassEntity() {}

    public ClassEntity(Long id, String section, Grade grade, Level level, School school) {
        this.id = id;
        this.section = section;
        this.grade = grade;
        this.level = level;
        this.school = school;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
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

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}