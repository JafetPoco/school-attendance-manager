package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.domain.model.Token;
import com.IEASmart.sistemaAsistencias.domain.repository.TokenRepository;
import com.IEASmart.sistemaAsistencias.infrastructure.mapper.TokenMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TokenRepositoryImpl implements TokenRepository {
    private final TokenJpaRepository tokenJpaRepository;
    private final TokenMapper mapper;

    public TokenRepositoryImpl(TokenJpaRepository tokenJpaRepository, TokenMapper mapper) {
        this.tokenJpaRepository = tokenJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Token> findByTokenAndUsedFalse(String token) {
        return tokenJpaRepository.findByTokenAndUsedFalse(token).map(mapper::toDomain);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return tokenJpaRepository.findById(token).map(mapper::toDomain);
    }

    @Override
    public Token save(Token token) {
        return mapper.toDomain(tokenJpaRepository.save(mapper.toEntity(token)));
    }

    @Override
    public List<Token> saveAll(List<Token> tokens) {
        return tokenJpaRepository.saveAll(tokens.stream().map(mapper::toEntity).toList())
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Token> findByAttendanceIdAndUsedFalse(String attendanceId) {
        return tokenJpaRepository.findByAttendanceIdAndUsedFalse(attendanceId).map(mapper::toDomain);
    }
}
