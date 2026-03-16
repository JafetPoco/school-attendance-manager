package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.domain.model.SchoolPolicy;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;

import java.util.Optional;

public interface SchoolPolicyRepository {
    SchoolPolicy save(SchoolPolicy schoolPolicy);
    Optional<SchoolPolicy> getBySchool(School school);
}
