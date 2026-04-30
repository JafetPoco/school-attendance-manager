package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Class;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;

import java.util.List;

public interface ClassRepository {
   List<Class> findAllBySchool(School schoolId);
   Class save(Class newClass);
}
