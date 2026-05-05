package com.IEASmart.sistemaAsistencias.application.service;

import com.IEASmart.sistemaAsistencias.api.dto.request.ClassRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.ClassFullInfoResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.ClassResponse;
import com.IEASmart.sistemaAsistencias.api.mapper.ClassApiMapper;
import com.IEASmart.sistemaAsistencias.domain.exception.InvalidArgumentException;
import com.IEASmart.sistemaAsistencias.domain.model.Class;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.repository.ClassRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassService {
    private final ClassRepository classRepository;
    private final ClassApiMapper classApiMapper;

    public ClassService(ClassRepository classRepository, ClassApiMapper classApiMapper) {
        this.classRepository = classRepository;
        this.classApiMapper = classApiMapper;
    }

    public List<ClassResponse> getAllClasses(School school) {
        return classRepository.findAllBySchool(school).stream().map(classApiMapper::toResponse).toList();
    }

    public List<ClassFullInfoResponse> getAllClassesFullInfo(School school) {
        return classRepository.findAllBySchool(school).stream().map(classApiMapper::toFullInfoResponse).toList();
    }

    public ClassResponse createClass(ClassRequest request, School school) {
        if(request == null) {
            throw new InvalidArgumentException("request", "Request cannot be null");
        }

        Class newClass = classApiMapper.toDomain(request);
        newClass.setSchool(school);

        return classApiMapper.toResponse(classRepository.save(newClass));
    }

    public ClassFullInfoResponse updateClass(Long classId, ClassRequest request, School school) {
        if(request == null) {
            throw new InvalidArgumentException("request", "Request cannot be null");
        }

        Optional<Class> existingClass = classRepository.findById(classId);

        if(existingClass.isEmpty()) {
            throw new InvalidArgumentException("classId", "Class not found for the given id and school");
        }

        if(request.getLevel() != null) {
            existingClass.get().setLevel(request.getLevel());
        }
        if(request.getGrade() != null) {
            existingClass.get().setGrade(request.getGrade());
        }
        if(request.getSection() != null) {
            existingClass.get().setSection(request.getSection());
        }

        return classApiMapper.toFullInfoResponse(classRepository.save(existingClass.get()));
    }
}
