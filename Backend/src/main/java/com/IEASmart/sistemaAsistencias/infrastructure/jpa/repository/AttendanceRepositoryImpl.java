package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Attendance;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.repository.AttendanceRepository;
import com.IEASmart.sistemaAsistencias.infrastructure.mapper.AttendanceMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class AttendanceRepositoryImpl implements AttendanceRepository {
    private final AttendanceJpaRepository attendanceJpaRepository;
    private final AttendanceMapper mapper;

    public AttendanceRepositoryImpl(AttendanceJpaRepository attendanceJpaRepository, AttendanceMapper mapper) {
        this.attendanceJpaRepository = attendanceJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean existsByStudentAndDate(String student, LocalDate date){
        return attendanceJpaRepository.findByStudent_DniAndDate(student, date).isPresent();
    }

    @Override
    public Attendance save(Attendance attendance){
        return mapper.toDomain(attendanceJpaRepository.save(mapper.toEntity(attendance)));
    }

    @Override
    public List<Attendance> findAllBySchool(School school) {
        return attendanceJpaRepository.findAllByStudent_School(school).stream().map(mapper::toDomain).toList();
    }
}
