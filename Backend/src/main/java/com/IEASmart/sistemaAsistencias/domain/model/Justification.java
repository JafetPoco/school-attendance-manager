package com.IEASmart.sistemaAsistencias.domain.model;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.JustificationStatus;

import java.time.LocalDateTime;

public class Justification {
    private Long id;
    private Attendance attendance;
    private String description;
    private String urlEvidence;
    private LocalDateTime justificationDate;
    private JustificationStatus status;

    public Justification() {}

    public Justification(Long id, Attendance attendance, String description, String urlEvidence, LocalDateTime justificationDate, JustificationStatus status) {
        this.id = id;
        this.attendance = attendance;
        this.description = description;
        this.urlEvidence = urlEvidence;
        this.justificationDate = justificationDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlEvidence() {
        return urlEvidence;
    }

    public void setUrlEvidence(String urlEvidence) {
        this.urlEvidence = urlEvidence;
    }

    public LocalDateTime getJustificationDate() {
        return justificationDate;
    }

    public void setJustificationDate(LocalDateTime justificationDate) {
        this.justificationDate = justificationDate;
    }

    public JustificationStatus getStatus() {
        return status;
    }

    public void setStatus(JustificationStatus status) {
        this.status = status;
    }
}
