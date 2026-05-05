package com.IEASmart.sistemaAsistencias.api.mapper;

import com.IEASmart.sistemaAsistencias.api.dto.request.ClassRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.ClassFullInfoResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.ClassResponse;
import com.IEASmart.sistemaAsistencias.domain.model.Class;
import org.springframework.stereotype.Component;

@Component
public class ClassApiMapper {
    public ClassResponse toResponse(Class classSchool) {
        ClassResponse response = new ClassResponse();
        response.setId(classSchool.getId());

        if(classSchool.getSection().length() == 1){
            response.setName(classSchool.getSection() + "-" + classSchool.getGrade() + "-" + classSchool.getLevel());
        } else {
            response.setName(classSchool.getSection());
        }
        return response;
    }

    public ClassFullInfoResponse toFullInfoResponse(Class classSchool) {
        ClassFullInfoResponse response = new ClassFullInfoResponse();
        response.setId(classSchool.getId());
        response.setSection(classSchool.getSection());
        response.setGrade(classSchool.getGrade());
        response.setLevel(classSchool.getLevel());
        return response;
    }

    public Class toDomain(ClassRequest request) {
        Class classSchool = new Class();
        classSchool.setSection(request.getSection());
        classSchool.setGrade(request.getGrade());
        classSchool.setLevel(request.getLevel());
        return classSchool;
    }
}
