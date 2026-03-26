package com.IEASmart.sistemaAsistencias.infrastructure.mapper;

import com.IEASmart.sistemaAsistencias.domain.model.Attendance;
import com.IEASmart.sistemaAsistencias.domain.model.Justification;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.AttendanceEntity;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.JustificationEntity;
import org.springframework.stereotype.Component;

@Component
public class JustificationMapper {
    private final AttendanceMapper attendanceMapper;

    public JustificationMapper(AttendanceMapper attendanceMapper) {
        this.attendanceMapper = attendanceMapper;
    }

    public Justification toDomain(JustificationEntity entity){
        Justification justification = new Justification();
        justification.setId(entity.getId());
        justification.setDescription(entity.getDescription());
        justification.setJustificationDate(entity.getJustificationDate());
        justification.setStatus(entity.getStatus());
        justification.setUrlEvidence(entity.getUrlEvidence());

        Attendance attendance = entity.getAttendance() != null ? attendanceMapper.toDomain(entity.getAttendance()) : null;
        justification.setAttendance(attendance);
        return justification;
    }

    public JustificationEntity toEntity(Justification justification){
        JustificationEntity entity = new JustificationEntity();
        entity.setId(justification.getId());
        entity.setDescription(justification.getDescription());
        entity.setJustificationDate(justification.getJustificationDate());
        entity.setStatus(justification.getStatus());
        entity.setUrlEvidence(justification.getUrlEvidence());

        AttendanceEntity attendance = justification.getAttendance() != null ? attendanceMapper.toEntity(justification.getAttendance()) : null;
        entity.setAttendance(attendance);
        return entity;
    }
}
