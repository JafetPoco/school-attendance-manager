package com.IEASmart.sistemaAsistencias.application.service;

import com.IEASmart.sistemaAsistencias.domain.exception.ConflictException;
import com.IEASmart.sistemaAsistencias.domain.exception.ResourceNotFoundException;
import com.IEASmart.sistemaAsistencias.domain.model.Attendance;
import com.IEASmart.sistemaAsistencias.domain.model.SchoolPolicy;
import com.IEASmart.sistemaAsistencias.domain.model.Token;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.repository.SchoolPolicyRepository;
import com.IEASmart.sistemaAsistencias.domain.repository.TokenRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final SchoolPolicyRepository schoolPolicyRepository;

    public TokenService(TokenRepository tokenRepository, SchoolPolicyRepository schoolPolicyRepository) {
        this.tokenRepository = tokenRepository;
        this.schoolPolicyRepository = schoolPolicyRepository;
    }

    public List<Token> generateTokens(List<Attendance> attendances, School school) {
        if (attendances.isEmpty()) {
            return Collections.emptyList();
        }

        SchoolPolicy schoolPolicy = schoolPolicyRepository.getBySchool(school)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion del colegio", "School", school));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plus(Duration.ofDays(schoolPolicy.getJustificationExpirationDays()));

        List<Token> tokens = attendances.stream()
                .map(attendance -> createToken(attendance.getId(), now, expiryDate))
                .collect(Collectors.toList());

        List<Token> saved = tokenRepository.saveAll(tokens);

        /*
        List<String> attendanceIds = attendances.stream()
                .map(Attendance::getId)
                .collect(Collectors.toList());
        tokenRepository.markTokensAsUsedByAttendances(attendanceIds);
        */

        return tokens;
    }

    private Token createToken(String attendanceId, LocalDateTime now, LocalDateTime expiryDate) {
        Token token = new Token();
        token.setUsed(false);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(expiryDate);
        token.setCreatedAt(now);
        token.setAttendanceId(attendanceId);
        return token;
    }

    public String getAttendanceIdFromToken(String token) {
        Token tokenDomain = tokenRepository.findByTokenAndUsedFalse(token)
                .orElseThrow(() -> new ConflictException("Token inválido o ya utilizado", "INVALID_TOKEN"));

        if (tokenDomain.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ConflictException("Token expirado", "EXPIRED");
        }

        return tokenDomain.getAttendanceId();
    }

    public void markTokenAsUsed(String token) {
        Optional<Token> tokenOpt = tokenRepository.findByToken(token);
        if(tokenOpt.isEmpty()){
            throw new ResourceNotFoundException("Token", token);
        }
        Token t = tokenOpt.get();
        t.setUsed(true);
        tokenRepository.save(t);
    }

    public long deleteExpiredTokens(School school) {
        LocalDateTime now = LocalDateTime.now();
        long count = tokenRepository.deleteExpiredTokens(now);
        if(count == 0){
            throw new ResourceNotFoundException("Tokens expirados");
        }
        return count;
    }
}
