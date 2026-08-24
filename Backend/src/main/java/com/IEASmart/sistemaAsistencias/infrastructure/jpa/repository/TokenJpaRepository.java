package com.IEASmart.sistemaAsistencias.infrastructure.jpa.repository;

import com.IEASmart.sistemaAsistencias.infrastructure.jpa.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenJpaRepository extends JpaRepository<TokenEntity, String> {
    Optional<TokenEntity> findByTokenAndUsedFalse(String token);
    Optional<TokenEntity> findByAttendanceIdAndUsedFalse(String attendanceId);

    @Transactional
    @Modifying
    @Query("""
        DELETE FROM TokenEntity t
        WHERE t.expiryDate < :date
    """)
    long deleteBySchoolAndDate(
            @Param("date") LocalDateTime date
    );
}
