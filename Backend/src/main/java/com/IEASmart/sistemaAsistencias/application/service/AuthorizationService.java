package com.IEASmart.sistemaAsistencias.application.service;

import com.IEASmart.sistemaAsistencias.api.dto.request.ProfessorRequest;
import com.IEASmart.sistemaAsistencias.api.dto.request.UserRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.CreateUserResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.UserInfoResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.UserResponse;
import com.IEASmart.sistemaAsistencias.api.mapper.UserApiMapper;
import com.IEASmart.sistemaAsistencias.domain.exception.ConflictException;
import com.IEASmart.sistemaAsistencias.domain.exception.InvalidArgumentException;
import com.IEASmart.sistemaAsistencias.domain.exception.ResourceNotFoundException;
import com.IEASmart.sistemaAsistencias.domain.exception.UnauthorizedOperationException;
import com.IEASmart.sistemaAsistencias.domain.model.Admin;
import com.IEASmart.sistemaAsistencias.domain.model.Professor;
import com.IEASmart.sistemaAsistencias.domain.model.User;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.UserType;
import com.IEASmart.sistemaAsistencias.domain.repository.AdminRepository;
import com.IEASmart.sistemaAsistencias.domain.repository.ProfessorRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorizationService {
    private final AdminRepository adminRepository;
    private final ProfessorRepository professorRepository;
    private final UserApiMapper mapper;

    public AuthorizationService(AdminRepository adminRepository,
                               ProfessorRepository professorRepository,
                               UserApiMapper userApiMapper) {
        this.adminRepository = adminRepository;
        this.professorRepository = professorRepository;
        this.mapper = userApiMapper;
    }

    private Authentication getAuthenticationFromContext() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private String getEmailFromAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedOperationException("No autenticado");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            return (String) principal;
        } else if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof OAuth2User) {
            Object emailAttr = ((OAuth2User) principal).getAttribute("email");
            if (emailAttr != null) {
                return emailAttr.toString();
            }
        }
        throw new UnauthorizedOperationException("No se pudo extraer el email del usuario autenticado");
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw InvalidArgumentException.required("email");
        }
    }

    private User resolveUserByEmail() {
        Authentication auth = getAuthenticationFromContext();
        String email = getEmailFromAuthentication(auth);
        validateEmail(email);

        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        Optional<Professor> profOpt = professorRepository.findByEmail(email);

        List<User> matches = new ArrayList<>(3);
        adminOpt.ifPresent(matches::add);
        profOpt.ifPresent(matches::add);

        if (matches.isEmpty()) {
            throw new ResourceNotFoundException("User", "email", email);
        }

        if (matches.size() > 1) {
            throw new ConflictException(
                    "El email '" + email + "' existe en más de un tipo de usuario. Debe ser único.",
                    "DUPLICATE_EMAIL"
            );
        }

        return matches.get(0);
    }

    public UserType getUserType() {
        return resolveUserByEmail().getUserType();
    }

    public School getUserSchool() {
        School school = resolveUserByEmail().getSchool();
        if(school == null) {
            throw new ResourceNotFoundException("School", "user email", resolveUserByEmail().getEmail());
        }
        return school;
    }

    public UserResponse getUserInfo(String pictureUrl) {
        User userEntity = resolveUserByEmail();

        UserResponse response = mapper.toResponse(userEntity);
        response.setUrlPicture(pictureUrl);
        return response;
    }

    public CreateUserResponse createUser(UserRequest user) {
        if (user == null) {
            throw new InvalidArgumentException("Los datos del usuario son requeridos", "INVALID_REQUEST");
        }
        if (user.getUserType() == null) {
            throw InvalidArgumentException.required("userType");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw InvalidArgumentException.required("email");
        }

        // Regla: email debe ser único entre todos los tipos
        boolean emailExists = adminRepository.findByEmail(user.getEmail()).isPresent()
                || professorRepository.findByEmail(user.getEmail()).isPresent();
        if (emailExists) {
            throw new ConflictException("Ya existe un usuario con el email: " + user.getEmail(), "EMAIL_ALREADY_EXISTS");
        }

        User savedEntity;
        switch (user.getUserType()) {
            case ADMIN -> savedEntity = adminRepository.save(mapper.toAdminEntity(user));
            case PROFESSOR -> savedEntity = professorRepository.save(mapper.toProfessorEntity(user));
            default -> throw new InvalidArgumentException(
                    "Tipo de usuario no soportado: " + user.getUserType(),
                    "UNSUPPORTED_USER_TYPE"
            );
        }

        return mapper.toCreateResponse(savedEntity);
    }

    public UserInfoResponse createProfessor(ProfessorRequest professor, School school) {
        if (professor.getEmail() == null || professor.getEmail().isBlank()) {
            throw InvalidArgumentException.required("email");
        }

        boolean emailExists = adminRepository.findByEmail(professor.getEmail()).isPresent()
                || professorRepository.findByEmail(professor.getEmail()).isPresent();
        if (emailExists) {
            throw new ConflictException("Ya existe un usuario con el email: " + professor.getEmail(), "EMAIL_ALREADY_EXISTS");
        }

        Professor p = new Professor();
        p.setNames(professor.getNames());
        p.setFirstLastName(professor.getFirstLastName());
        p.setSecondLastName(professor.getSecondLastName());
        p.setEmail(professor.getEmail());
        p.setSchool(school);
        p.setUserType(UserType.PROFESSOR);

        p = professorRepository.save(p);

        return mapper.toInfoResponse(p);
    }

    public boolean isAdmin() {
        return resolveUserByEmail().getUserType() == UserType.ADMIN;
    }

    public List<UserInfoResponse> getAllUsers(School school) {
        List<Admin> admins = adminRepository.findAllBySchool(school);
        List<Professor> professors = professorRepository.findAllBySchool(school);

        List<UserInfoResponse> responses = new ArrayList<>(admins.size() + professors.size());
        responses.addAll(admins.stream().map(mapper::toInfoResponse).toList());
        responses.addAll(professors.stream().map(mapper::toInfoResponse).toList());

        return responses;
    }
}
