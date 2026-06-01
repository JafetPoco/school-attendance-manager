package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.AttendanceType;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Section;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.AttendanceEntity;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.projection.AttendanceStats;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.projection.TopLateInfo;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.projection.WeekAttendanceStats;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceJpaRepository extends JpaRepository<AttendanceEntity, String>, JpaSpecificationExecutor<AttendanceEntity> {
    Optional<AttendanceEntity> findByStudent_DniAndDate(String student, LocalDate date);
    List<AttendanceEntity> findAllByStudent_ClassInfo_IdAndDateBetween(Long classId, LocalDate startDate, LocalDate endDate);
    long countByStudent_DniAndAttendanceTypeAndDateBetween(String studentDni, AttendanceType type, LocalDate startDate, LocalDate endDate);
    List<AttendanceEntity> findAllByStudent_DniAndDateBetweenOrderByDateAsc(String studentDni, LocalDate startDate, LocalDate endDate);

    @Query("SELECT a FROM AttendanceEntity a " +
            "JOIN FETCH a.student s " +
            "JOIN FETCH s.classInfo c " +
            "WHERE c.school = :school " +
            "AND a.date BETWEEN :startDate AND :endDate " +
            "ORDER BY c.id ASC, s.firstLastName ASC, s.secondLastName ASC, s.name ASC, a.date ASC")
    List<AttendanceEntity> findAllBySchoolAndDateBetween(@Param("school") School school,
                                                         @Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);

    @Query("SELECT a.attendanceType as attendanceType, COUNT(a) as count " +
            "FROM AttendanceEntity a " +
            "WHERE a.date = :date AND a.student.classInfo.school = :school " +
            "GROUP BY a.attendanceType")
    List<AttendanceStats> getAttendanceStatsByDateAndSchool(@Param("date") LocalDate date,
                                                            @Param("school") School school);

    @Query("SELECT a.date as day, " +
            "SUM(CASE WHEN a.attendanceType = 0 THEN 1 ELSE 0 END) as attendances, " +
            "SUM(CASE WHEN a.attendanceType = 1 THEN 1 ELSE 0 END) as absences, " +
            "SUM(CASE WHEN a.attendanceType = 2 THEN 1 ELSE 0 END) as late " +
            "FROM AttendanceEntity a " +
            "WHERE a.student.classInfo.school = :school " +
            "AND a.date BETWEEN :startDate AND :endDate " +
            "GROUP BY a.date " +
            "ORDER BY a.date ASC")
    List<WeekAttendanceStats> getDailyStatistics(
            @Param("school") School school,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT a.student.name as name, a.student.firstLastName as firstLastName, " +
            "a.student.secondLastName as secondLastName, a.student.classInfo.section as section, " +
            "a.student.classInfo.grade as grade, " +
            "COUNT(a) as lateCount " +
            "FROM AttendanceEntity a " +
            "WHERE a.attendanceType = 2 AND a.student.classInfo.school = :school " +
            "AND a.date BETWEEN :startDate AND :endDate " +
            "GROUP BY a.student.dni, a.student.name, a.student.firstLastName, a.student.secondLastName, " +
            "a.student.classInfo.section, a.student.classInfo.grade " +
            "ORDER BY lateCount DESC")
    List<TopLateInfo> getTopLateStudents(
            @Param("school") School school,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );
}
