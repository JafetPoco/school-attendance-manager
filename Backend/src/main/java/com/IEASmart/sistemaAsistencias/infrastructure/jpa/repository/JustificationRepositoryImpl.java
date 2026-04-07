package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Justification;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.JustificationStatus;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.repository.JustificationRepository;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.JustificationEntity;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.specification.JustificationSpecifications;
import com.IEASmart.sistemaAsistencias.infrastructure.mapper.JustificationMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class JustificationRepositoryImpl implements JustificationRepository {
    private final JustificationJpaRepository jpaRepository;
    private final JustificationMapper mapper;

    public JustificationRepositoryImpl(JustificationJpaRepository jpaRepository, JustificationMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Justification> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Justification> findByAttendanceId(Long attendanceId) {
        return jpaRepository.findByAttendance_Id(attendanceId).map(mapper::toDomain);
    }

    @Override
    public List<Justification> findAllByStatus(JustificationStatus status, School school) {
        return jpaRepository.findAllByStatusAndAttendance_Student_School(status, school).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Page<Justification> findAllByFilter(School school, JustificationStatus justificationStatus, LocalDate startDate, LocalDate endDate, Pageable pageable){
        Specification<JustificationEntity> spec = Specification
                .where(JustificationSpecifications.hasSchool(school))
                .and(JustificationSpecifications.hasJustificationStatus(justificationStatus))
                .and(JustificationSpecifications.hasDateBeetween(startDate, endDate));

        Page<JustificationEntity> page = jpaRepository.findAll(spec, pageable);
        return page.map(mapper::toDomain);
    }

    @Override
    public Justification save(Justification justification) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(justification)));
    }
}
