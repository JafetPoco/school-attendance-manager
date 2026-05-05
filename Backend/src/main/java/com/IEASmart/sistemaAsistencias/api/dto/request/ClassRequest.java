package com.IEASmart.sistemaAsistencias.api.dto.request;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Grade;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Level;

public class ClassRequest {
    private Level level;
    private Grade grade;
    private String section;

    public ClassRequest() {}

    public ClassRequest(Level level, Grade grade, String section) {
        this.level = level;
        this.grade = grade;
        this.section = section;
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
