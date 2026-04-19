package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.AttendanceType;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Section;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.AttendanceEntity;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.projection.AttendanceStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceJpaRepository extends JpaRepository<AttendanceEntity, String>, JpaSpecificationExecutor<AttendanceEntity> {
    Optional<AttendanceEntity> findByStudent_DniAndDate(String student, LocalDate date);
    List<AttendanceEntity> findAllByStudent_SchoolAndStudent_SectionAndDateBetween(School school, Section section, LocalDate startDate, LocalDate endDate);
    long countByStudent_DniAndAttendanceTypeAndDateBetween(String studentDni, AttendanceType type, LocalDate startDate, LocalDate endDate);
    List<AttendanceEntity> findAllByStudent_DniAndDateBetweenOrderByDateAsc(String studentDni, LocalDate startDate, LocalDate endDate);

    @Query("SELECT a.attendanceType as attendanceType, COUNT(a) as count " +
            "FROM AttendanceEntity a " +
            "WHERE a.date = :date AND a.student.school = :school " +
            "GROUP BY a.attendanceType")
    List<AttendanceStats> getAttendanceStatsByDateAndSchool(@Param("date") LocalDate date,
                                                            @Param("school") School school);
}
