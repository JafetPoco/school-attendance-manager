package com.IEASmart.sistemaAsistencias.infrastructure.jpa.specification;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.JustificationStatus;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.JustificationEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class JustificationSpecifications {
    private JustificationSpecifications() {}

    public static Specification<JustificationEntity> hasDateBeetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            // si no hay rango, no agregar predicado
            if (startDate == null && endDate == null) {
                return null;
            }

            // both bounds provided: start <= date < end
            if (startDate != null && endDate != null) {
                return criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("attendance").get("date"), startDate),
                        criteriaBuilder.lessThan(root.get("attendance").get("date"), endDate)
                );
            }

            // only startDate: date >= startDate
            if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("attendance").get("date"), startDate);
            }

            // only endDate: date < endDate
            return criteriaBuilder.lessThan(root.get("attendance").get("date"), endDate);
        };
    }

    public static Specification<JustificationEntity> hasJustificationStatus(JustificationStatus justificationStatus) {
        return (root, query, criteriaBuilder) -> justificationStatus == null ? null : criteriaBuilder.equal(root.get("status"), justificationStatus);
    }

    public static Specification<JustificationEntity> hasSchool(School school) {
        return (root, query, criteriaBuilder) -> school == null ? null :  criteriaBuilder.equal(root.get("attendance").get("student").get("classInfo").get("school"), school);
    }

    public static Specification<JustificationEntity> hasDate(LocalDate date) {
        return (root, query, criteriaBuilder) -> date == null ? null : criteriaBuilder.equal(root.get("attendance").get("date"), date);
    }

    public static Specification<JustificationEntity> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return null;
            }
            String pattern = name.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("attendance").get("student").get("name")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("attendance").get("student").get("firstLastName")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("attendance").get("student").get("secondLastName")), pattern)
            );
        };
    }
}
