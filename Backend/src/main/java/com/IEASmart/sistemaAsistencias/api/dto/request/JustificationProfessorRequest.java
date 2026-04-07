package com.IEASmart.sistemaAsistencias.api.dto.request;

public class JustificationProfessorRequest {
    private Long idAttendance;
    private String description;

    public JustificationProfessorRequest() {}

    public JustificationProfessorRequest(Long idAttendance, String description) {
        this.idAttendance = idAttendance;
        this.description = description;
    }

    public Long getIdAttendance() {
        return idAttendance;
    }

    public void setIdAttendance(Long idAttendance) {
        this.idAttendance = idAttendance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
