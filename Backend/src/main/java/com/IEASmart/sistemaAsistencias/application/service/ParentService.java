package com.IEASmart.sistemaAsistencias.application.service;

import com.IEASmart.sistemaAsistencias.api.dto.request.ParentWithChildRequest;
import com.IEASmart.sistemaAsistencias.api.dto.request.StudentRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.ParentResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.ParentWithChildResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.StudentResponse;
import com.IEASmart.sistemaAsistencias.api.mapper.ParentApiMapper;
import com.IEASmart.sistemaAsistencias.api.mapper.ParentWithChildApiMapper;
import com.IEASmart.sistemaAsistencias.api.mapper.StudentApiMapper;
import com.IEASmart.sistemaAsistencias.domain.model.Parent;
import com.IEASmart.sistemaAsistencias.domain.model.Student;
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

    @Transactional(readOnly = true)
    public List<ParentResponse> getAllParents() {
        List<Parent> parents = parentRepository.getAll();
        return parents.stream()
                .map(parentApiMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<ParentWithChildResponse> getParentById(Long parentId) {
        Optional<Parent> parentOpt = parentRepository.findById(parentId);
        if(parentOpt.isEmpty()){
            return Optional.empty();
        } else {
            Parent parent = parentOpt.get();
            ParentWithChildResponse response = parentWithChildApiMapper.toResponse(parent);
            return Optional.of(response);
        }
    }

    public StudentResponse addChildToParent(Long parentId, StudentRequest student) {
        if(student == null) throw new IllegalArgumentException("Student cannot be null");

        if (student.getDni() == null || student.getDni().isBlank()) {
            throw new IllegalArgumentException("Student DNI is required and must not be empty when creating a student");
        }

        Optional<Parent> parentOpt = parentRepository.findById(parentId);
        if (parentOpt.isPresent()) {
            Parent parent = parentOpt.get();

            // Convertir request a dominio (una sola vez)
            Student domainStudent = studentApiMapper.toDomain(student);

            // Agregar al padre y guardar
            parent.addChild(domainStudent);
            Parent savedParent = parentRepository.save(parent);

            String studentDni = student.getDni();

            // Buscar el estudiante recién guardado dentro del padre guardado por su dni
            Optional<Student> savedStudentOpt = savedParent.getChildren().stream()
                    .filter(s -> s != null && studentDni != null && studentDni.equals(s.getDni()))
                    .findFirst();

            StudentResponse studentResponse;
            if (savedStudentOpt.isPresent()) {
                studentResponse = studentApiMapper.toResponse(savedStudentOpt.get());
            } else {
                // Si no se encuentra (caso raro), mapear desde la entidad que intentamos guardar
                studentResponse = studentApiMapper.toResponse(domainStudent);
            }

            // Asignar el nombre del padre directamente desde el padre guardado
            studentResponse.setParentName(savedParent.getNames());
            return studentResponse;
        }
        throw new IllegalArgumentException("Parent with id " + parentId + " not found");
    }

    public ParentResponse addParentWithChild(ParentWithChildRequest request) {
        if(request == null) throw new IllegalArgumentException("Request cannot be null");

        // Validar hijos: si vienen, cada uno debe tener DNI porque StudentEntity usa dni como @Id
        if (request.getChildren() != null) {
            for (com.IEASmart.sistemaAsistencias.api.dto.request.StudentRequest s : request.getChildren()) {
                if (s == null) continue;
                if (s.getDni() == null || s.getDni().isBlank()) {
                    throw new IllegalArgumentException("Each child in request must have a non-empty DNI to be persisted");
                }
            }
        }

        Parent parent = parentWithChildApiMapper.toDomain(request);
        Parent savedParent = parentRepository.save(parent);
        return parentApiMapper.toResponse(savedParent);
    }

    public List<StudentResponse> addChildrenToParent(Long parentId, List<StudentRequest> students) {
        if (students == null) throw new IllegalArgumentException("Students cannot be null");

        // Validar que cada student tenga DNI porque StudentEntity usa dni como @Id
        for (StudentRequest s : students) {
            if (s == null) continue;
            if (s.getDni() == null || s.getDni().isBlank()) {
                throw new IllegalArgumentException("Each student must have a non-empty DNI to be persisted");
            }
        }

        Optional<Parent> parentOpt = parentRepository.findById(parentId);
        if(parentOpt.isPresent()) {
            Parent parent = parentOpt.get();
            List<Student> studentEntities = students.stream()
                    .map(studentApiMapper::toDomain)
                    .toList();
            studentEntities.forEach(parent::addChild);
            Parent saved = parentRepository.save(parent);
            return saved.getChildren().stream()
                    .map(studentApiMapper::toResponse)
                    .toList();
        }
        throw new IllegalArgumentException("Parent with id " + parentId + " not found");


    }

    public List<StudentResponse> getChildrenOfParent(Long parentId) {
        Optional<Parent> parentOpt = parentRepository.findById(parentId);
        if (parentOpt.isPresent()) {
            Parent parent = parentOpt.get();
            return parent.getChildren().stream()
                    .map(studentApiMapper::toResponse)
                    .toList();
        }
        throw new IllegalArgumentException("Parent with id " + parentId + " not found");
    }
}
