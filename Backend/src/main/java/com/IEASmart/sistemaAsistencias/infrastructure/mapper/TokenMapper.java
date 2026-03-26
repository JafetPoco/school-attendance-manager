package com.IEASmart.sistemaAsistencias.infrastructure.mapper;

import com.IEASmart.sistemaAsistencias.domain.model.Token;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.TokenEntity;
import org.springframework.stereotype.Component;

@Component
public class TokenMapper {

    public Token toDomain(TokenEntity entity){
        Token token = new Token();
        token.setToken(entity.getToken());
        token.setAttendanceId(entity.getAttendanceId());
        token.setCreatedAt(entity.getCreatedAt());
        token.setExpiryDate(entity.getExpiryDate());
        token.setUsed(entity.isUsed());

        return token;
    }

    public TokenEntity toEntity(Token token){
        TokenEntity entity = new TokenEntity();
        entity.setToken(token.getToken());
        entity.setAttendanceId(token.getAttendanceId());
        entity.setCreatedAt(token.getCreatedAt());
        entity.setExpiryDate(token.getExpiryDate());
        entity.setUsed(token.isUsed());

        return entity;
    }
}
