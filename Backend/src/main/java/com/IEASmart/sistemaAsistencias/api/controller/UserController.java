package com.IEASmart.sistemaAsistencias.api.controller;

import com.IEASmart.sistemaAsistencias.api.dto.request.ProfessorRequest;
import com.IEASmart.sistemaAsistencias.api.dto.request.UserRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.CreateUserResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.UserInfoResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.UserResponse;
import com.IEASmart.sistemaAsistencias.application.service.AuthorizationService;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final AuthorizationService authorizationService;

    public UserController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(Authentication authentication) {
        String picture = null;

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if(principal instanceof OAuth2User) {
                Object picAttr = ((OAuth2User) principal).getAttribute("picture");
                if (picAttr != null) picture = picAttr.toString();
            } else if (principal instanceof UserDetails) {
                // No picture available for UserDetails, so we can ignore this case
            }
        }
        UserResponse response = authorizationService.getUserInfo(picture);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-user")
    public ResponseEntity<CreateUserResponse> addUser(@RequestBody UserRequest request) {
        CreateUserResponse response = authorizationService.createUser(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<List<UserInfoResponse>> getAllUsers() {
        if(!authorizationService.isAdmin()){
            return ResponseEntity.status(403).build();
        }
        School school = authorizationService.getUserSchool();
        List<UserInfoResponse> response = authorizationService.getAllUsers(school);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<UserInfoResponse> addProfessor(@RequestBody ProfessorRequest request) {
        if(!authorizationService.isAdmin()){
            return ResponseEntity.status(403).build();
        }
        School s = authorizationService.getUserSchool();
        UserInfoResponse response = authorizationService.createProfessor(request, s);
        return ResponseEntity.ok(response);
    }
}

