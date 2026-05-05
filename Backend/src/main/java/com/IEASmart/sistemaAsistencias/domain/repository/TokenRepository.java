package com.IEASmart.sistemaAsistencias.domain.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Token;

import java.util.List;
import java.util.Optional;

public interface TokenRepository {
    Optional<Token> findByTokenAndUsedFalse(String token);
    Optional<Token> findByToken(String token);
    Token save(Token token);
    List<Token> saveAll(List<Token> tokens);
    Optional<Token> findByAttendanceIdAndUsedFalse(String attendanceId);
}
