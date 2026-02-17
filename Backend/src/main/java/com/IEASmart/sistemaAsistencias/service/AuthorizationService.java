package com.IEASmart.sistemaAsistencias.service;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.UserType;
import com.IEASmart.sistemaAsistencias.domain.repository.AdminRepository;
import com.IEASmart.sistemaAsistencias.domain.repository.ProfessorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationService {
    private final AdminRepository adminRepository;
    private final ProfessorRepository professorRepository;

    public AuthorizationService(AdminRepository adminRepository, ProfessorRepository professorRepository) {
        this.adminRepository = adminRepository;
        this.professorRepository = professorRepository;
    }

    public Optional<UserType> getUserTypeByEmail(String email) {
        if (adminRepository.existsByEmail(email)) {
            return Optional.of(UserType.ADMIN);
        } else if (professorRepository.existsByEmail(email)) {
            return Optional.of(UserType.PROFESSOR);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Long> getIdByEmail(String email) {
        if (adminRepository.existsByEmail(email)) {
            return adminRepository.findByEmail(email).map(a -> a.getUserId());
        } else if (professorRepository.existsByEmail(email)) {
            return professorRepository.findByEmail(email).map(p -> p.getUserId());
        } else {
            return Optional.empty();
        }
    }

}
