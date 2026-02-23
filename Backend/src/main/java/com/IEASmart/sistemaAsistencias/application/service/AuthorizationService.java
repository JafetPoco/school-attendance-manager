package com.IEASmart.sistemaAsistencias.application.service;

import com.IEASmart.sistemaAsistencias.api.dto.request.UserRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.CreateUserResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.UserResponse;
import com.IEASmart.sistemaAsistencias.api.mapper.UserApiMapper;
import com.IEASmart.sistemaAsistencias.domain.exception.ConflictException;
import com.IEASmart.sistemaAsistencias.domain.exception.InvalidArgumentException;
import com.IEASmart.sistemaAsistencias.domain.exception.ResourceNotFoundException;
import com.IEASmart.sistemaAsistencias.domain.exception.UnauthorizedOperationException;
import com.IEASmart.sistemaAsistencias.domain.model.Admin;
import com.IEASmart.sistemaAsistencias.domain.model.Professor;
import com.IEASmart.sistemaAsistencias.domain.model.SuperAdmin;
import com.IEASmart.sistemaAsistencias.domain.model.User;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.UserType;
import com.IEASmart.sistemaAsistencias.domain.repository.AdminRepository;
import com.IEASmart.sistemaAsistencias.domain.repository.ProfessorRepository;
import com.IEASmart.sistemaAsistencias.domain.repository.SuperAdminRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorizationService {
    private final AdminRepository adminRepository;
    private final ProfessorRepository professorRepository;
    private final SuperAdminRepository superAdminRepository;
    private final UserApiMapper mapper;

    public AuthorizationService(AdminRepository adminRepository,
                               ProfessorRepository professorRepository,
                               SuperAdminRepository superAdminRepository,
                               UserApiMapper userApiMapper) {
        this.adminRepository = adminRepository;
        this.professorRepository = professorRepository;
        this.superAdminRepository = superAdminRepository;
        this.mapper = userApiMapper;
    }

    private User resolveUserByEmail(String email) {
        validateEmail(email);

        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        Optional<Professor> profOpt = professorRepository.findByEmail(email);
        Optional<SuperAdmin> supOpt = superAdminRepository.findByEmail(email);

        List<User> matches = new ArrayList<>(3);
        adminOpt.ifPresent(matches::add);
        profOpt.ifPresent(matches::add);
        supOpt.ifPresent(matches::add);

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

    public UserType getUserTypeByEmail(String email) {
        return resolveUserByEmail(email).getUserType();
    }

    public Long getIdByEmail(String email) {
        Long id = resolveUserByEmail(email).getUserId();
        if (id == null) {
            throw new ConflictException("El usuario no tiene id asignado", "USER_ID_NOT_ASSIGNED");
        }
        return id;
    }

    private String getSchoolName(User user) {
        if (user == null) {
            throw new InvalidArgumentException("Usuario es requerido", "USER_REQUIRED");
        }

        if (user.getUserType() == UserType.SUPER_ADMIN) {
            return null;
        }

        if (user.getSchool() == null) {
            throw new ConflictException(
                    "El Usuario debe tener un colegio asignado (school)",
                    "SCHOOL_REQUIRED"
            );
        }

        return switch (user.getSchool()) {
            case GJS -> "Gral. José de San Martín";
            case EFF -> "Eduardo Francisco Forga";
        };
    }

    public UserResponse getUserInfoByEmail(String email, String pictureUrl) {
        User userEntity = resolveUserByEmail(email);

        UserResponse response = mapper.toResponse(userEntity);
        response.setUrlPicture(pictureUrl);
        response.setSchoolName(getSchoolName(userEntity));

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
                || professorRepository.findByEmail(user.getEmail()).isPresent()
                || superAdminRepository.findByEmail(user.getEmail()).isPresent();
        if (emailExists) {
            throw new ConflictException("Ya existe un usuario con el email: " + user.getEmail(), "EMAIL_ALREADY_EXISTS");
        }

        User savedEntity;
        switch (user.getUserType()) {
            case ADMIN -> savedEntity = adminRepository.save(mapper.toAdminEntity(user));
            case PROFESSOR -> savedEntity = professorRepository.save(mapper.toProfessorEntity(user));
            case SUPER_ADMIN -> savedEntity = superAdminRepository.save(mapper.toSuperAdminEntity(user));
            default -> throw new InvalidArgumentException(
                    "Tipo de usuario no soportado: " + user.getUserType(),
                    "UNSUPPORTED_USER_TYPE"
            );
        }

        CreateUserResponse response = mapper.toCreateResponse(savedEntity);

        if(response.getUserType() == UserType.SUPER_ADMIN) {
            response.setSchoolName(null);
        } else {
            response.setSchoolName(getSchoolName(savedEntity));
        }
        return response;
    }

    public boolean hasAnyRole(Authentication authentication, UserType... requiredRoles) {
        if (!isAuthenticated(authentication)) {
            return false;
        }

        for (UserType role : requiredRoles) {
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role.name()))) {
                return true;
            }
        }
        return false;
    }

    public void requireAnyRole(Authentication authentication, UserType... requiredRoles) {
        if (!hasAnyRole(authentication, requiredRoles)) {
            throw UnauthorizedOperationException.insufficientRole(String.valueOf(requiredRoles));
        }
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw InvalidArgumentException.required("email");
        }
    }
}
