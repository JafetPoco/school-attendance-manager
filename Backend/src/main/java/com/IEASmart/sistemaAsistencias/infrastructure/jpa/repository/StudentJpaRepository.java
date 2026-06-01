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
import java.util.Set;

public interface StudentJpaRepository extends JpaRepository<StudentEntity, String>, JpaSpecificationExecutor<StudentEntity> {
    // Obtener todos los estudiantes de una escuela (la escuela está en classInfo.school)
    @Query("SELECT s FROM StudentEntity s JOIN FETCH s.classInfo c WHERE c.school = :school ORDER BY c.id ASC, s.firstLastName ASC, s.secondLastName ASC, s.name ASC")
    List<StudentEntity> findAllByClassInfo_School(@Param("school") School school);

    Optional<StudentEntity> findByDniAndClassInfo_School(String id, School school);
    List<StudentEntity> findByNameContainingIgnoreCaseAndClassInfo_School(String query, School school);

    @Query("SELECT s.dni FROM StudentEntity s WHERE s.classInfo.school = :school AND NOT EXISTS (SELECT a FROM AttendanceEntity a WHERE a.student.dni = s.dni AND a.date = :date)")
    List<String> findAllByClassInfo_SchoolAndWithoutAttendanceOnDate(@Param("school") School school, @Param("date") LocalDate date);

    @Query("SELECT s FROM StudentEntity s JOIN FETCH s.classInfo WHERE s.classInfo.id = :classId ORDER BY s.firstLastName ASC, s.secondLastName ASC, s.name ASC")
    List<StudentEntity> findAllByClassInfo_IdOrderByFirstLastNameAsc(@Param("classId") Long classId);

    long countByClassInfo_School(School school);

    @Query("SELECT s.dni FROM StudentEntity s WHERE s.classInfo.school = :school")
    Set<String> findExistingDnis(@Param("school") School school);

    @Override
    StudentEntity getReferenceById(String dni);
}
