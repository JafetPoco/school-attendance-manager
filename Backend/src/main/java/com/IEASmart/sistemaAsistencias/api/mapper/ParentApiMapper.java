package com.IEASmart.sistemaAsistencias.api.mapper;

import com.IEASmart.sistemaAsistencias.api.dto.response.ParentResponse;
import com.IEASmart.sistemaAsistencias.domain.model.Parent;
import org.springframework.stereotype.Component;

@Component
public class ParentApiMapper {
    public ParentResponse toResponse(Parent parent){
        if (parent == null) return null;
        ParentResponse parentResponse = new ParentResponse();
        parentResponse.setParentId(parent.getParentId());
        parentResponse.setNames(parent.getNames());
        parentResponse.setPhoneNumber(parent.getPhoneNumber());
        return parentResponse;
    }
}
