package com.IEASmart.sistemaAsistencias.application.service;

import com.IEASmart.sistemaAsistencias.domain.exception.ConflictException;
import com.IEASmart.sistemaAsistencias.domain.exception.ResourceNotFoundException;
import com.IEASmart.sistemaAsistencias.domain.model.Attendance;
import com.IEASmart.sistemaAsistencias.domain.model.SchoolPolicy;
import com.IEASmart.sistemaAsistencias.domain.model.Token;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.AttendanceType;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.repository.AttendanceRepository;
import com.IEASmart.sistemaAsistencias.domain.repository.SchoolPolicyRepository;
import com.IEASmart.sistemaAsistencias.domain.repository.TokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final AttendanceRepository attendanceRepository;
    private final SchoolPolicyRepository schoolPolicyRepository;

    public TokenService(TokenRepository tokenRepository, AttendanceRepository attendanceRepository, SchoolPolicyRepository schoolPolicyRepository) {
        this.tokenRepository = tokenRepository;
        this.attendanceRepository = attendanceRepository;
        this.schoolPolicyRepository = schoolPolicyRepository;
    }

    public Token generateToken(Long attendanceId, School school) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Asistencia no encontrada"));

        if (attendance.getAttendanceType() != AttendanceType.AUSENTE) {
            throw new ConflictException("Solo se pueden generar tokens para asistencias marcadas como AUSENTE", "INVALID_ATTENDANCE_TYPE");
        }

        // Invalidar tokens anteriores para esta asistencia
        //tokenRepository.markTokensAsUsedByAttendance(attendanceId);
        Optional<SchoolPolicy> schoolPolicyOpt = schoolPolicyRepository.getBySchool(school);
        if(schoolPolicyOpt.isEmpty()) {
            throw new ResourceNotFoundException("Configuracion del colegio", "School", school);
        }
        SchoolPolicy schoolPolicy = schoolPolicyOpt.get();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plus(Duration.ofDays(schoolPolicy.getJustificationExpirationDays()));

        String tokenCode = UUID.randomUUID().toString();
        Token token= new Token();
        token.setUsed(false);
        token.setToken(tokenCode);
        token.setExpiryDate(expiryDate);
        token.setCreatedAt(now);
        token.setAttendanceId(attendanceId);

        return token;
    }

    public Long getAttendanceIdFromToken(String token) {
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
/*
    @Scheduled(cron = "0 0 2 * * ?") // Cada día a las 2 AM
    public void cleanExpiredTokens() {
        tokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
    }
    */
}
