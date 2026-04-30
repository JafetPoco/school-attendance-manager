package com.IEASmart.sistemaAsistencias.application.service;

import com.IEASmart.sistemaAsistencias.api.dto.request.ClassRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.ClassResponse;
import com.IEASmart.sistemaAsistencias.api.mapper.ClassApiMapper;
import com.IEASmart.sistemaAsistencias.domain.exception.InvalidArgumentException;
import com.IEASmart.sistemaAsistencias.domain.model.Class;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.repository.ClassRepository;

import java.util.List;

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

    public ClassResponse createClass(ClassRequest request, School school) {
        if(request == null) {
            throw new InvalidArgumentException("request", "Request cannot be null");
        }

        Class newClass = classApiMapper.toDomain(request);
        newClass.setSchool(school);

        return classApiMapper.toResponse(classRepository.save(newClass));
    }
}
