package com.IEASmart.sistemaAsistencias.infrastructure.jpa.projection;

public interface TopLateInfo {
    String getName();
    String getFirstLastName();
    String getSecondLastName();
    String getSection();
    String getGrade();
    int getLateCount();
}
