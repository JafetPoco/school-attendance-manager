package com.IEASmart.sistemaAsistencias.infrastructure.mapper;

import com.IEASmart.sistemaAsistencias.domain.model.Attendance;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.AttendanceEntity;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {
    private final StudentMapper studentMapper;

    public AttendanceMapper(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    public Attendance toDomain(AttendanceEntity entity){
        Attendance attendance = new Attendance();
        attendance.setId(entity.getId());
        attendance.setDate(entity.getDate());
        attendance.setTime(entity.getTime());
        attendance.setAttendanceType(entity.getAttendanceType());
        attendance.setStudent(studentMapper.toDomain(entity.getStudent()));
        return attendance;
    }

    public AttendanceEntity toEntity(Attendance attendance){
        AttendanceEntity entity = new AttendanceEntity();
        entity.setId(attendance.getId());
        entity.setDate(attendance.getDate());
        entity.setTime(attendance.getTime());
        entity.setAttendanceType(attendance.getAttendanceType());
        entity.setStudent(studentMapper.toEntity(attendance.getStudent()));
        return entity;
    }
}
