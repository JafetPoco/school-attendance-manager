package com.IEASmart.sistemaAsistencias.infrastructure.jpa.specification;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.AttendanceType;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Section;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.AttendanceEntity;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.StudentEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class AttendanceSpecifications {
    private AttendanceSpecifications() {}

    public static Specification<AttendanceEntity> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return null;
            }
            String pattern = name.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("student").get("name")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("student").get("firstLastName")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("student").get("secondLastName")), pattern)
            );
        };
    }

    public static Specification<AttendanceEntity> hasSchool(School school){
        return (root, query, criteriaBuilder) -> school == null ? null : criteriaBuilder.equal(root.get("student").get("school"), school);
    }

    public static Specification<AttendanceEntity> hasDate(LocalDate date){
        return (root, query, criteriaBuilder) -> date == null ? null : criteriaBuilder.equal(root.get("date"), date);
    }

    public static Specification<AttendanceEntity> hasSection(Section section){
        return (root, query, criteriaBuilder) -> section == null ? null : criteriaBuilder.equal(root.get("student").get("section"), section);
    }

    public static Specification<AttendanceEntity> hasAttendanceType(AttendanceType attendanceType){
        return (root, query, criteriaBuilder) -> attendanceType == null ? null : criteriaBuilder.equal(root.get("attendanceType"), attendanceType);
    }
}
