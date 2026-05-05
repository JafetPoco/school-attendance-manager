package com.IEASmart.sistemaAsistencias.application.service;

import com.IEASmart.sistemaAsistencias.api.dto.request.SchoolPolicyRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.SchoolPolicyResponse;
import com.IEASmart.sistemaAsistencias.api.mapper.SchoolPolicyApiMapper;
import com.IEASmart.sistemaAsistencias.domain.exception.InvalidArgumentException;
import com.IEASmart.sistemaAsistencias.domain.exception.ResourceNotFoundException;
import com.IEASmart.sistemaAsistencias.domain.model.SchoolPolicy;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.repository.SchoolPolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
@Transactional
public class SchoolPolicyService {
    private final SchoolPolicyRepository schoolPolicyRepository;
    private final SchoolPolicyApiMapper mapper;

    public SchoolPolicyService(SchoolPolicyRepository schoolPolicyRepository, SchoolPolicyApiMapper mapper) {
        this.schoolPolicyRepository = schoolPolicyRepository;
        this.mapper = mapper;
    }

    public SchoolPolicyResponse createSchoolPolicy(SchoolPolicyRequest request, School school) {
        if (request == null || request.getJustificationExpirationDays() == null || request.getLateAttendaceTime() == null) {
            throw new InvalidArgumentException("Invalid school policy request. Justification expiration days and late attendance time are required.", "INVALID_SCHOOL_POLICY_REQUEST");
        }

        SchoolPolicy schoolPolicy = mapper.toDomain(request);
        schoolPolicy.setSchool(school);
        schoolPolicyRepository.save(schoolPolicy);
        return mapper.toResponse(schoolPolicy);
    }

    public SchoolPolicyResponse getSchoolPolicy(School school) {
        Optional<SchoolPolicy> schoolPolicy = schoolPolicyRepository.getBySchool(school);
        if(schoolPolicy.isEmpty()) {
            throw new ResourceNotFoundException("Configuracion del colegio", "School", school);
        }
        return mapper.toResponse(schoolPolicy.get());
    }

    public SchoolPolicyResponse updateSchoolPolicy(SchoolPolicyRequest request, School school) {
        if (request == null) {
            throw new InvalidArgumentException("Invalid patch request", "INVALID_SCHOOL_POLICY_PATCH_REQUEST");
        }

        Optional<SchoolPolicy> opt = schoolPolicyRepository.getBySchool(school);
        if (opt.isEmpty()) {
            throw new ResourceNotFoundException("Configuracion del colegio", "School", school);
        }

        SchoolPolicy existing = opt.get();

        if (request.getJustificationExpirationDays() != null) {
            existing.setJustificationExpirationDays(request.getJustificationExpirationDays());
        }

        if (request.getLateAttendaceTime() != null) {
            try {
                existing.setLateAttendaceTime(LocalTime.parse(request.getLateAttendaceTime()));
            } catch (DateTimeParseException ex) {
                throw new InvalidArgumentException("Invalid time format for lateAttendaceTime. Expected HH:mm or ISO_LOCAL_TIME.", "INVALID_TIME_FORMAT");
            }
        }

        schoolPolicyRepository.save(existing);
        return mapper.toResponse(existing);
    }
}
