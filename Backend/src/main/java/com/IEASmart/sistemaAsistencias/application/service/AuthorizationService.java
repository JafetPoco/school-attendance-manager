package com.IEASmart.sistemaAsistencias.application.service;

import com.IEASmart.sistemaAsistencias.api.dto.request.UserRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.CreateUserResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.UserResponse;
import com.IEASmart.sistemaAsistencias.api.mapper.UserApiMapper;
import com.IEASmart.sistemaAsistencias.domain.model.Admin;
import com.IEASmart.sistemaAsistencias.domain.model.Professor;
import com.IEASmart.sistemaAsistencias.domain.model.SuperAdmin;
import com.IEASmart.sistemaAsistencias.domain.model.User;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.UserType;
import com.IEASmart.sistemaAsistencias.domain.repository.AdminRepository;
import com.IEASmart.sistemaAsistencias.domain.repository.ProfessorRepository;
import com.IEASmart.sistemaAsistencias.domain.repository.SuperAdminRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationService {
    private final AdminRepository adminRepository;
    private final ProfessorRepository professorRepository;
    private final SuperAdminRepository superAdminRepository;
    private final UserApiMapper mapper;

    public AuthorizationService(AdminRepository adminRepository, ProfessorRepository professorRepository, SuperAdminRepository superAdminRepository, UserApiMapper userApiMapper) {
        this.adminRepository = adminRepository;
        this.professorRepository = professorRepository;
        this.superAdminRepository = superAdminRepository;
        this.mapper = userApiMapper;
    }

    public Optional<UserType> getUserTypeByEmail(String email) {
        if (adminRepository.existsByEmail(email)) {
            return Optional.of(UserType.ADMIN);
        } else if (professorRepository.existsByEmail(email)) {
            return Optional.of(UserType.PROFESSOR);
        } else if (superAdminRepository.existsByEmail(email)) {
            return Optional.of(UserType.SUPER_ADMIN);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Long> getIdByEmail(String email) {
        if (adminRepository.existsByEmail(email)) {
            return adminRepository.findByEmail(email).map(Admin::getUserId);
        } else if (professorRepository.existsByEmail(email)) {
            return professorRepository.findByEmail(email).map(Professor::getUserId);
        } else if (superAdminRepository.existsByEmail(email)) {
            return superAdminRepository.findByEmail(email).map(SuperAdmin::getUserId);
        } else {
            return Optional.empty();
        }
    }

    private String getSchoolNameByEmail(User user) {
        if(user.getUserType() != UserType.SUPER_ADMIN) {
            return null;
        }

        switch (user.getSchool()) {
            case GJS:
                return "Gral. José de San Martín";
            case EFF:
                return "Eduardo Francisco Forga";
            default:
                throw new IllegalStateException("Unexpected school: " + user.getSchool());
        }
    }

    public UserResponse getUserInfoByEmail(String email, String pictureUrl) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email is required");
        }

        Optional<UserType> userTypeOpt = getUserTypeByEmail(email);
        UserType type = userTypeOpt.get();
        User userEntity;
        switch (type) {
            case ADMIN:
                Admin admin = adminRepository.findByEmail(email).get();
                userEntity = admin;
                break;
            case PROFESSOR:
                Professor prof = professorRepository.findByEmail(email).get();
                userEntity = prof;
                break;
            case SUPER_ADMIN:
                SuperAdmin sup = superAdminRepository.findByEmail(email).get();
                userEntity = sup;
                break;
            default:
                throw new IllegalStateException("Unexpected user type: " + type);
        }

        UserResponse response = mapper.toResponse(userEntity);
        response.setUrlPicture(pictureUrl);

        if(response.getUserType() == UserType.ADMIN || response.getUserType() == UserType.PROFESSOR) {
            response.setSchoolName(getSchoolNameByEmail(userEntity));
        } else {
            response.setSchoolName(null);
        }

        return response;
    }

    public CreateUserResponse createUser(UserRequest user) {
        if (user == null) {
            throw new IllegalArgumentException("User data is required");
        }

        if (user.getUserType() == null) {
            throw new IllegalArgumentException("User type is required");
        }

        User savedEntity = null;
        switch (user.getUserType()) {
            case ADMIN: {
                Admin adminEntity = mapper.toAdminEntity(user);
                Admin savedAdmin = adminRepository.save(adminEntity);
                savedEntity = savedAdmin;
                break;
            }
            case PROFESSOR: {
                Professor profEntity = mapper.toProfessorEntity(user);
                Professor savedProf = professorRepository.save(profEntity);
                savedEntity = savedProf;
                break;
            }
            case SUPER_ADMIN: {
                SuperAdmin supEntity = mapper.toSuperAdminEntity(user);
                SuperAdmin savedSup = superAdminRepository.save(supEntity);
                savedEntity = savedSup;
                break;
            }
            default:
                throw new IllegalStateException("Unexpected user type: " + user.getUserType());
        }

        CreateUserResponse response = mapper.toCreateResponse(savedEntity);
        if (savedEntity.getSchool() != null) {
            response.setSchoolName(getSchoolNameByEmail(savedEntity));
        }
        return response;
    }
}
