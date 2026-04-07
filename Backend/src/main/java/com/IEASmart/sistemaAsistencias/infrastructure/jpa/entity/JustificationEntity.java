package com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.JustificationStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "justifications")
public class JustificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "attendance_id", unique = true)
    private AttendanceEntity attendance;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(name="url_evidence", length = 255)
    private String urlEvidence;

    @Column(name="justification_date", nullable = false)
    private LocalDateTime justificationDate;

    @Enumerated(EnumType.STRING)
    private JustificationStatus status;

    public JustificationEntity(Long id, AttendanceEntity attendance, String description, String urlEvidence, LocalDateTime justificationDate, JustificationStatus status) {
        this.id = id;
        this.attendance = attendance;
        this.description = description;
        this.urlEvidence = urlEvidence;
        this.justificationDate = justificationDate;
        this.status = status;
    }

    public JustificationEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AttendanceEntity getAttendance() {
        return attendance;
    }

    public void setAttendance(AttendanceEntity attendance) {
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
