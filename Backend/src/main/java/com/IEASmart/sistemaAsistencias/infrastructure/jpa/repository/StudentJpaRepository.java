package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentJpaRepository extends JpaRepository<StudentEntity, String>, JpaSpecificationExecutor<StudentEntity> {
    List<StudentEntity> findAllBySchool(School school);

    Optional<StudentEntity> findByDniAndSchool(String id, School school);

    @Query("SELECT s FROM StudentEntity s LEFT JOIN AttendanceEntity a ON s.dni = a.student.dni AND a.date = :date WHERE s.school = :school AND a.id IS NULL")
    List<StudentEntity> findAllBySchoolAndWithoutAttendanceOnDate(@Param("school") School school, @Param("date") LocalDate date);
}
