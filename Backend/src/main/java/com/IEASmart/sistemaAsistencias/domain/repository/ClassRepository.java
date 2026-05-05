package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Class;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Grade;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Level;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;

import java.util.List;
import java.util.Optional;

public interface ClassRepository {
   List<Class> findAllBySchool(School schoolId);
   List<Long> findAllIdsBySchool(School school);
   Class save(Class newClass);
   Optional<Class> findByClassInformation(String section, Grade grade, Level level, School school);
   Class getRefernceById(Long classId);
   Optional<Class> findById(Long classId);
}
