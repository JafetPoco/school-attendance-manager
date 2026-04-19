package com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity;

import com.IEASmart.sistemaAsistencias.domain.model.Student;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.AttendanceType;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "attendances")
public class AttendanceEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "attendance_type", nullable = false)
    private AttendanceType attendanceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dni", nullable = false, foreignKey = @ForeignKey(name = "fk_attendance_student", foreignKeyDefinition = "FOREIGN KEY (dni) REFERENCES students(dni) ON DELETE CASCADE"))
    private StudentEntity student;

    public AttendanceEntity() {}

    public AttendanceEntity(String id, LocalTime time, LocalDate date, AttendanceType attendanceType, StudentEntity student) {
        this.id = id;
        this.time = time;
        this.date = date;
        this.attendanceType = attendanceType;
        this.student = student;
    }

    @PrePersist
    public void generarId() {
        if (id == null) {
            id = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0,10);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public AttendanceType getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(AttendanceType attendanceType) {
        this.attendanceType = attendanceType;
    }

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }
}