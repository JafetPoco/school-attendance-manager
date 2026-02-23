package com.IEASmart.sistemaAsistencias.api.mapper;

import com.IEASmart.sistemaAsistencias.api.dto.request.UserRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.CreateUserResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.UserResponse;
import com.IEASmart.sistemaAsistencias.domain.model.Admin;
import com.IEASmart.sistemaAsistencias.domain.model.Professor;
import com.IEASmart.sistemaAsistencias.domain.model.SuperAdmin;
import com.IEASmart.sistemaAsistencias.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserApiMapper {
    public UserResponse toResponse(User user){
        if(user == null) return null;
        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setNames(user.getNames());
        response.setFirstLastName(user.getFirstLastName());
        response.setSecondLastName(user.getSecondLastName());
        response.setEmail(user.getEmail());
        response.setUserType(user.getUserType());
        return response;
    }

    public User toDomain(UserRequest response){
        if(response == null) return null;
        User user = new User();
        user.setNames(response.getNames());
        user.setFirstLastName(response.getFirstLastName());
        user.setSecondLastName(response.getSecondLastName());
        user.setEmail(response.getEmail());
        user.setUserType(response.getUserType());
        user.setSchool(response.getSchool());
        return user;
    }

    public Admin toAdminEntity(UserRequest req) {
        if (req == null) return null;
        Admin a = new Admin();
        a.setNames(req.getNames());
        a.setFirstLastName(req.getFirstLastName());
        a.setSecondLastName(req.getSecondLastName());
        a.setEmail(req.getEmail());
        a.setUserType(req.getUserType());
        a.setSchool(req.getSchool());
        return a;
    }

    public Professor toProfessorEntity(UserRequest req) {
        if (req == null) return null;
        Professor p = new Professor();
        p.setNames(req.getNames());
        p.setFirstLastName(req.getFirstLastName());
        p.setSecondLastName(req.getSecondLastName());
        p.setEmail(req.getEmail());
        p.setUserType(req.getUserType());
        p.setSchool(req.getSchool());
        return p;
    }

    public SuperAdmin toSuperAdminEntity(UserRequest req) {
        if (req == null) return null;
        SuperAdmin s = new SuperAdmin();
        s.setNames(req.getNames());
        s.setFirstLastName(req.getFirstLastName());
        s.setSecondLastName(req.getSecondLastName());
        s.setEmail(req.getEmail());
        s.setUserType(req.getUserType());
        s.setSchool(req.getSchool());
        return s;
    }

    public CreateUserResponse toCreateResponse(User user) {
        if (user == null) return null;
        CreateUserResponse resp = new CreateUserResponse();
        resp.setUserId(user.getUserId());
        resp.setNames(user.getNames());
        resp.setFirstLastName(user.getFirstLastName());
        resp.setSecondLastName(user.getSecondLastName());
        resp.setEmail(user.getEmail());
        resp.setUserType(user.getUserType());
        return resp;
    }
}
