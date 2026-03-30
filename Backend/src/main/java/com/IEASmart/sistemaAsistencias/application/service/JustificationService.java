package com.IEASmart.sistemaAsistencias.application.service;

import com.IEASmart.sistemaAsistencias.api.dto.request.JustificationRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.JustificationResponse;
import com.IEASmart.sistemaAsistencias.api.mapper.JustificationApiMapper;
import com.IEASmart.sistemaAsistencias.domain.exception.ConflictException;
import com.IEASmart.sistemaAsistencias.domain.exception.ResourceNotFoundException;
import com.IEASmart.sistemaAsistencias.domain.model.Attendance;
import com.IEASmart.sistemaAsistencias.domain.model.Justification;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.AttendanceType;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.JustificationStatus;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.repository.AttendanceRepository;
import com.IEASmart.sistemaAsistencias.domain.repository.JustificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JustificationService {
    private final TokenService tokenService;
    private final AttendanceRepository attendanceRepository;
    private final JustificationRepository justificationRepository;
    private final JustificationApiMapper justificationApiMapper;

    public JustificationService(TokenService tokenService, AttendanceRepository attendanceRepository, JustificationRepository justificationRepository, JustificationApiMapper justificationApiMapper) {
        this.tokenService = tokenService;
        this.attendanceRepository = attendanceRepository;
        this.justificationRepository = justificationRepository;
        this.justificationApiMapper = justificationApiMapper;
    }

    public JustificationResponse createJustification(JustificationRequest request){
        validateToken(request.getToken(), request.getIdAttendance());

        Attendance attendance = getAndValidateAttendance(request.getIdAttendance());

        validateNoExistingJustification(attendance.getId());

        Justification justification = justificationApiMapper.toDomain(request);
        justification.setAttendance(attendance);
        justification.setJustificationDate(LocalDateTime.now());
        justification.setStatus(JustificationStatus.PENDIENTE);

        justificationRepository.save(justification);
        tokenService.markTokenAsUsed(request.getToken());
        return justificationApiMapper.toResponse(justification);
    }

    /**
     * Valida que el token sea válido y corresponda a la asistencia
     */
    private void validateToken(String token, Long attendanceId) {
        Long attendanceIdFromToken = tokenService.getAttendanceIdFromToken(token);

        if (!attendanceIdFromToken.equals(attendanceId)) {
            throw new SecurityException("Token inválido para esta asistencia");
        }
    }

    /**
     * Obtiene y valida que la asistencia exista y esté en estado FALTA
     */
    private Attendance getAndValidateAttendance(Long attendanceId) {
        Optional<Attendance> attendanceOpt = attendanceRepository.findById(attendanceId);
        if(attendanceOpt.isEmpty()){
            throw new ResourceNotFoundException("Asistencia");
        }

        Attendance attendance = attendanceOpt.get();
        if (attendance.getAttendanceType() != AttendanceType.AUSENTE) {
            throw new ConflictException("Solo se pueden justificar asistencias marcadas como ausencia", "INVALID_ATTENDANCE_TYPE");
        }
        return attendance;
    }

    /**
     * Verifica que no exista una justificación pendiente o aprobada
     */
    private void validateNoExistingJustification(Long attendanceId) {
        Optional<Justification> existingJustification = justificationRepository.findByAttendanceId(attendanceId);

        if (existingJustification.isPresent()) {
            Justification justification = existingJustification.get();

            if (justification.getStatus() == JustificationStatus.PENDIENTE) {
                throw new ConflictException("Ya existe una justificación pendiente para esta asistencia", "JUSTIFICATION_PENDING");
            }

            if (justification.getStatus() == JustificationStatus.ACEPTADA) {
                throw new ConflictException("Ya existe una justificación aprobada para esta asistencia", "JUSTIFICATION_APPROVED");
            }

        }
    }

    public List<JustificationResponse> getPendingJustifications(School school) {
        List<Justification> pendingJustifications = justificationRepository.findAllByStatus(JustificationStatus.PENDIENTE, school);
        return pendingJustifications.stream().map(justificationApiMapper::toResponse).toList();
    }

    public JustificationResponse approveJustification(Long justificationId) {
        Justification justification = justificationRepository.findById(justificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Justificación", justificationId));

        if (justification.getStatus() != JustificationStatus.PENDIENTE) {
            throw new ConflictException("Solo se pueden aprobar justificaciones pendientes", "INVALID_JUSTIFICATION_STATUS");
        }

        justification.setStatus(JustificationStatus.ACEPTADA);
        justificationRepository.save(justification);

        Attendance attendance = justification.getAttendance();
        attendance.setAttendanceType(AttendanceType.JUSTIFICADO);
        attendanceRepository.save(attendance);

        return justificationApiMapper.toResponse(justification);
    }

    public JustificationResponse rejectJustification(Long justificationId) {
        Justification justification = justificationRepository.findById(justificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Justificación", justificationId));

        if (justification.getStatus() != JustificationStatus.PENDIENTE) {
            throw new ConflictException("Solo se pueden rechazar justificaciones pendientes", "INVALID_JUSTIFICATION_STATUS");
        }

        justification.setStatus(JustificationStatus.RECHAZADA);
        justificationRepository.save(justification);
        return justificationApiMapper.toResponse(justification);
     }
}
