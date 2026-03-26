package com.IEASmart.sistemaAsistencias.api.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class JustificationResponse {
    private String studentName;
    private LocalDate attendanceDate;
    private String description;
    private String urlEvidence;
    private LocalDateTime justificationDate;

    public JustificationResponse(String studentName, LocalDate attendanceDate, String description, String urlEvidence, LocalDateTime justificationDate) {
        this.studentName = studentName;
        this.attendanceDate = attendanceDate;
        this.description = description;
        this.urlEvidence = urlEvidence;
        this.justificationDate = justificationDate;
    }

    public JustificationResponse() {}

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
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
}
