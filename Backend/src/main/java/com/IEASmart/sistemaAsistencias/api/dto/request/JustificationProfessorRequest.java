package com.IEASmart.sistemaAsistencias.api.dto.request;

public class JustificationProfessorRequest {
    private String idAttendance;
    private String description;

    public JustificationProfessorRequest() {}

    public JustificationProfessorRequest(String idAttendance, String description) {
        this.idAttendance = idAttendance;
        this.description = description;
    }

    public String getIdAttendance() {
        return idAttendance;
    }

    public void setIdAttendance(String idAttendance) {
        this.idAttendance = idAttendance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
