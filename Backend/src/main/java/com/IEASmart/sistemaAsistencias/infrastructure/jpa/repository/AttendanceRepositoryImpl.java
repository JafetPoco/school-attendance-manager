package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.application.dto.AttendanceCriteria;
import com.IEASmart.sistemaAsistencias.domain.model.Attendance;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Section;
import com.IEASmart.sistemaAsistencias.domain.repository.AttendanceRepository;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.AttendanceEntity;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.specification.AttendanceSpecifications;
import com.IEASmart.sistemaAsistencias.infrastructure.mapper.AttendanceMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<Attendance> findAllByFilter(School school, AttendanceCriteria criteria, Pageable pageable) {
        Specification<AttendanceEntity> spec = Specification
                .where(AttendanceSpecifications.hasSchool(school))
                .and(AttendanceSpecifications.hasDate(criteria.date()))
                .and(AttendanceSpecifications.hasName(criteria.name()))
                .and(AttendanceSpecifications.hasSection(criteria.section()))
                .and(AttendanceSpecifications.hasAttendanceType(criteria.attendanceType()));

        Page<AttendanceEntity> page = attendanceJpaRepository.findAll(spec, pageable);
        return page.map(mapper::toDomain);
    }

    @Override
    public List<Attendance> findByStudentSchoolAndSectionAndDateBetween(School school, Section section, LocalDate startDate, LocalDate endDate) {
        return attendanceJpaRepository.findAllByStudent_SchoolAndStudent_SectionAndDateBetweenOrderByStudent_FirstLastNameAsc(school, section, startDate, endDate)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
