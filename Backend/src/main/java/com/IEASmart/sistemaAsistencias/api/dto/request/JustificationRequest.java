package com.IEASmart.sistemaAsistencias.api.dto.request;

public class JustificationRequest {
    private String token;
    private Long idAttendance;
    private String description;
    private String urlEvidence;

    public JustificationRequest(String token, Long idAttendance, String description, String urlEvidence) {
        this.token = token;
        this.idAttendance = idAttendance;
        this.description = description;
        this.urlEvidence = urlEvidence;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getUrlEvidence() {
        return urlEvidence;
    }

    public void setUrlEvidence(String urlEvidence) {
        this.urlEvidence = urlEvidence;
    }
}
