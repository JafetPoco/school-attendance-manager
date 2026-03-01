package com.IEASmart.sistemaAsistencias.application.service;

import com.IEASmart.sistemaAsistencias.api.dto.request.ParentWithChildRequest;
import com.IEASmart.sistemaAsistencias.api.dto.request.StudentRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.ParentResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.ParentWithChildResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.StudentResponse;
import com.IEASmart.sistemaAsistencias.api.mapper.ParentApiMapper;
import com.IEASmart.sistemaAsistencias.api.mapper.ParentWithChildApiMapper;
import com.IEASmart.sistemaAsistencias.api.mapper.StudentApiMapper;
import com.IEASmart.sistemaAsistencias.domain.exception.InvalidArgumentException;
import com.IEASmart.sistemaAsistencias.domain.model.Parent;
import com.IEASmart.sistemaAsistencias.domain.model.Student;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.repository.ParentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ParentService {
    private final ParentRepository parentRepository;
    private final ParentApiMapper parentApiMapper;
    private final StudentApiMapper studentApiMapper;
    private final ParentWithChildApiMapper parentWithChildApiMapper;

    public ParentService(ParentRepository parentRepository, ParentApiMapper parentApiMapper, StudentApiMapper studentApiMapper, ParentWithChildApiMapper parentWithChildApiMapper) {
        this.parentRepository = parentRepository;
        this.parentApiMapper = parentApiMapper;
        this.studentApiMapper = studentApiMapper;
        this.parentWithChildApiMapper = parentWithChildApiMapper;
    }

    public StudentResponse addChildToParent(Long parentId, StudentRequest student, School school) {
        if(student == null) throw new IllegalArgumentException("Student cannot be null");

        if (student.getDni() == null || student.getDni().isBlank()) {
            throw new IllegalArgumentException("Student DNI is required and must not be empty when creating a student");
        }

        Optional<Parent> parentOpt = parentRepository.findById(parentId, school);
        if (parentOpt.isPresent()) {
            Parent parent = parentOpt.get();

            Student domainStudent = studentApiMapper.toDomain(student);
            domainStudent.setSchool(school);

            parent.addChild(domainStudent);
            Parent savedParent = parentRepository.save(parent);

            String studentDni = student.getDni();

            // Buscar el estudiante recién guardado dentro del padre guardado por su dni
            Optional<Student> savedStudentOpt = savedParent.getChildren().stream()
                    .filter(s -> s.getDni().equals(studentDni))
                    .findFirst();

            StudentResponse studentResponse;
            if (savedStudentOpt.isPresent()) {
                studentResponse = studentApiMapper.toResponse(savedStudentOpt.get());
            } else {
                studentResponse = studentApiMapper.toResponse(domainStudent);
            }

            studentResponse.setParentName(savedParent.getNames());
            return studentResponse;
        }
        throw new IllegalArgumentException("Parent with id " + parentId + " not found");
    }

    public ParentResponse addParentWithChildren(ParentWithChildRequest request, School school) {
        if(request == null) throw new InvalidArgumentException("Request cannot be null", "request");

        if (request.getChildren() != null) {
            for (StudentRequest s : request.getChildren()) {
                if (s == null) continue;
                if (s.getDni() == null || s.getDni().isBlank()) {
                    throw new InvalidArgumentException("Each student must have a non-empty DNI to be persisted", "children");
                }
            }
        }

        Parent parent = parentWithChildApiMapper.toDomain(request);
        parent.setSchool(school);
        for(Student child : parent.getChildren()) {
            child.setSchool(school);
        }

        Parent savedParent = parentRepository.save(parent);
        return parentApiMapper.toResponse(savedParent);
    }

    public List<StudentResponse> addChildrenToParent(Long parentId, List<StudentRequest> students, School school) {
        if (students == null) throw new InvalidArgumentException("Students list cannot be null", "students");

        for (StudentRequest s : students) {
            if (s == null) continue;
            if (s.getDni() == null || s.getDni().isBlank()) {
                throw new InvalidArgumentException("Each student must have a non-empty DNI to be persisted", "students");
            }
        }

        Optional<Parent> parentOpt = parentRepository.findById(parentId, school);
        if(parentOpt.isPresent()) {
            Parent parent = parentOpt.get();

            for(Student child : parent.getChildren()) {
                child.setSchool(school);
            }

            List<Student> studentEntities = students.stream()
                    .map(studentApiMapper::toDomain)
                    .toList();
            studentEntities.forEach(parent::addChild);
            Parent saved = parentRepository.save(parent);
            return saved.getChildren().stream()
                    .map(studentApiMapper::toResponse)
                    .toList();
        }
        throw new InvalidArgumentException("Parent with id " + parentId + " not found", "parentId");
    }

    @Transactional(readOnly = true)
    public List<ParentResponse> getAllParents(School school) {
        List<Parent> parents = parentRepository.getAll(school);
        return parents.stream()
                .map(parentApiMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<ParentWithChildResponse> getParentById(Long parentId, School school) {
        Optional<Parent> parentOpt = parentRepository.findById(parentId, school);
        if(parentOpt.isEmpty()){
            return Optional.empty();
        } else {
            Parent parent = parentOpt.get();
            ParentWithChildResponse response = parentWithChildApiMapper.toResponse(parent);
            return Optional.of(response);
        }
    }

    public List<StudentResponse> getChildrenOfParent(Long parentId, School school) {
        Optional<Parent> parentOpt = parentRepository.findById(parentId, school);
        if (parentOpt.isPresent()) {
            Parent parent = parentOpt.get();
            return parent.getChildren().stream()
                    .map(studentApiMapper::toResponse)
                    .toList();
        }
        throw new IllegalArgumentException("Parent with id " + parentId + " not found");
    }
}
