package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Class;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Grade;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Level;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClassJpaRepository extends JpaRepository<ClassEntity, Long> {
    List<ClassEntity> findAllBySchool(School school);
    Optional<ClassEntity> findBySectionAndGradeAndLevelAndSchoolAllIgnoreCase(String section, Grade grade, Level level,School school);

    @Query("SELECT c.id FROM ClassEntity c WHERE c.school = :school")
    List<Long> findAllIdsBySchool(@Param("school") School school);

    ClassEntity save(ClassEntity classEntity);
}
