package com.IEASmart.sistemaAsistencias.api.dto.response;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Grade;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Level;

public class ClassFullInfoResponse {
    Long id;
    Level level;
    Grade grade;
    String section;

    public ClassFullInfoResponse() {}

    public ClassFullInfoResponse(Long id, Level level, Grade grade, String section) {
        this.id = id;
        this.level = level;
        this.grade = grade;
        this.section = section;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
