package com.IEASmart.sistemaAsistencias.infrastructure.jpa.specification;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Grade;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Level;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Section;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.StudentEntity;
import org.springframework.data.jpa.domain.Specification;

public final class StudentSpecifications {
    private StudentSpecifications() {}

    public static Specification<StudentEntity> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return null;
            }
            String pattern = name.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstLastName")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("secondLastName")), pattern)
            );
        };
    }

    public static Specification<StudentEntity> hasLevel(Level level) {
        return (root, query, criteriaBuilder) -> level == null ? null : criteriaBuilder.equal(root.get("classInfo").get("level"), level);
    }

    public static Specification<StudentEntity> hasGrade(Grade grade) {
        return (root, query, criteriaBuilder) -> grade == null ? null : criteriaBuilder.equal(root.get("classInfo").get("grade"), grade);
    }

    public static Specification<StudentEntity> hasSection(String section) {
        return (root, query, criteriaBuilder) -> section == null ? null : criteriaBuilder.equal(root.get("classInfo").get("section"), section);
    }

    public static Specification<StudentEntity> hasSchool(School school) {
        return (root, query, criteriaBuilder) -> school == null ? null : criteriaBuilder.equal(root.get("classInfo").get("school"), school);
    }
}
