package com.IEASmart.sistemaAsistencias.application.service;

import com.IEASmart.sistemaAsistencias.api.dto.request.StudentFilter;
import com.IEASmart.sistemaAsistencias.api.dto.response.PageResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.StudentResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.StudentSuggestionResponse;
import com.IEASmart.sistemaAsistencias.api.mapper.StudentApiMapper;
import com.IEASmart.sistemaAsistencias.application.dto.StudentCriteria;
import com.IEASmart.sistemaAsistencias.domain.model.Student;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Level;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Grade;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Section;
import com.IEASmart.sistemaAsistencias.domain.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentApiMapper studentApiMapper;

    public StudentService(StudentRepository studentRepository, StudentApiMapper studentApiMapper) {
        this.studentRepository = studentRepository;
        this.studentApiMapper = studentApiMapper;
    }

    public PageResponse<StudentResponse> getAllStudents(School school, StudentFilter filter, Pageable pageable) {
        Level levelEnum = filter.level() == null ? null : Level.from(filter.level());
        Grade gradeEnum = filter.grade() == null ? null : Grade.from(filter.grade());
        Section sectionEnum = filter.section() == null ? null : Section.from(filter.section());

        StudentCriteria criteria = new StudentCriteria(filter.name(), levelEnum, gradeEnum, sectionEnum);

        Page<Student> studentsPage = studentRepository.findAllByFilters(school, criteria, pageable);
        List<StudentResponse> content = studentsPage
                .getContent()
                .stream()
                .map(studentApiMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                studentsPage.getTotalElements(),
                studentsPage.getTotalPages(),
                studentsPage.getNumber(),
                studentsPage.getSize()
        );
    }

    public Optional<StudentResponse> getStudentById(String studentId, School school) {
        return studentRepository.findById(studentId, school).map(studentApiMapper::toResponse);
    }

    public List<StudentSuggestionResponse> autocompleteStudents(String query, School school){
        if (query == null || query.trim().length() < 3) {
            return List.of();
        }
        return studentRepository.findByNameContainingIgnoreCase(query, school).stream()
                .map(studentApiMapper::toSuggestionResponse)
                .toList();
    }
}
