package com.IEASmart.sistemaAsistencias.api.controller;

import com.IEASmart.sistemaAsistencias.api.dto.response.CountResponse;
import com.IEASmart.sistemaAsistencias.application.service.AuthorizationService;
import com.IEASmart.sistemaAsistencias.application.service.TokenService;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tokens")
public class TokenController {
    private final TokenService tokenService;
    private final AuthorizationService authorizationService;

    public TokenController(TokenService tokenService, AuthorizationService authorizationService) {
        this.tokenService = tokenService;
        this.authorizationService = authorizationService;
    }

    @DeleteMapping("/expired")
    public ResponseEntity<CountResponse> deleteExpiredTokens() {
        School school = authorizationService.getUserSchool();
        long deletedCount = tokenService.deleteExpiredTokens(school);
        return ResponseEntity.ok(new CountResponse(deletedCount));
    }
}