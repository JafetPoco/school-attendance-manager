package com.IEASmart.sistemaAsistencias.api.controller;

import com.IEASmart.sistemaAsistencias.api.dto.request.UserRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.CreateUserResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.UserResponse;
import com.IEASmart.sistemaAsistencias.application.service.AuthorizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final AuthorizationService authorizationService;

    public UserController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(Authentication authentication) {
        // Si no está autenticado devolvemos 401
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();
        String email = null;
        String picture = null;

        if (principal instanceof OAuth2User) {
            OAuth2User oauth = (OAuth2User) principal;
            Object emailAttr = oauth.getAttribute("email");
            if (emailAttr != null) email = emailAttr.toString();
            Object picAttr = oauth.getAttribute("picture");
            if (picAttr != null) picture = picAttr.toString();
        } else if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            // fallback: use authentication.getName()
            email = authentication.getName();
        }

        if (email == null || email.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        UserResponse response = authorizationService.getUserInfoByEmail(email, picture);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-user")
    public ResponseEntity<CreateUserResponse> addUser(@RequestBody UserRequest request) {
        CreateUserResponse response = authorizationService.createUser(request);
        return ResponseEntity.ok(response);
    }

}
