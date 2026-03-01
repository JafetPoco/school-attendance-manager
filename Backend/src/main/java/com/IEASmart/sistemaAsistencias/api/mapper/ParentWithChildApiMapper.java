package com.IEASmart.sistemaAsistencias.api.mapper;

import com.IEASmart.sistemaAsistencias.api.dto.request.ParentWithChildRequest;
import com.IEASmart.sistemaAsistencias.api.dto.request.StudentRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.ParentWithChildResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.StudentResponse;
import com.IEASmart.sistemaAsistencias.domain.model.Parent;
import com.IEASmart.sistemaAsistencias.domain.model.Student;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ParentWithChildApiMapper {
    private final StudentApiMapper studentApiMapper;

    public ParentWithChildApiMapper(StudentApiMapper studentApiMapper) {
        this.studentApiMapper = studentApiMapper;
    }

    public ParentWithChildResponse toResponse(Parent parent){
        if (parent == null) return null;

        ParentWithChildResponse parentWithChildResponse = new ParentWithChildResponse();
        parentWithChildResponse.setParentId(parent.getParentId());
        parentWithChildResponse.setNames(parent.getNames());
        parentWithChildResponse.setPhoneNumber(parent.getPhoneNumber());

        List<Student> studentList = parent.getChildren();
        if (studentList == null || studentList.isEmpty()) {
            parentWithChildResponse.setChildren(Collections.emptyList());
            parentWithChildResponse.setTotalChildren(0);
        } else {
            List<StudentResponse> studentResponses = studentList.stream()
                    .map(studentApiMapper::toResponse)
                    .toList();
            parentWithChildResponse.setChildren(studentResponses);
            parentWithChildResponse.setTotalChildren(studentResponses.toArray().length);
        }
        return parentWithChildResponse;
    }

    public Parent toDomain(ParentWithChildRequest request){
        if (request == null) return null;

        Parent parent = new Parent();
        parent.setNames(request.getNames());
        parent.setPhoneNumber(request.getPhoneNumber());

        List<StudentRequest> studentRequest = request.getChildren();
        if (studentRequest == null || studentRequest.isEmpty()) {
            parent.setChildren(Collections.emptyList());
        } else {
            List<Student> students = studentRequest.stream()
                    .map(studentApiMapper::toDomain)
                    .toList();
            parent.setChildren(students);
        }
        return parent;
    }
}
