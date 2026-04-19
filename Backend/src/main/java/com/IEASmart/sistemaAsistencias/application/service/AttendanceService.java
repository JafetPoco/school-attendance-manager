package com.IEASmart.sistemaAsistencias.application.service;

import com.IEASmart.sistemaAsistencias.api.dto.request.AttendanceFilter;
import com.IEASmart.sistemaAsistencias.api.dto.request.AttendanceMonthlyFilter;
import com.IEASmart.sistemaAsistencias.api.dto.request.AttendanceRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.*;
import com.IEASmart.sistemaAsistencias.api.mapper.AttendanceApiMapper;
import com.IEASmart.sistemaAsistencias.api.mapper.MonthlyAttendanceApiMapper;
import com.IEASmart.sistemaAsistencias.api.mapper.StudentApiMapper;
import com.IEASmart.sistemaAsistencias.application.dto.AttendanceCriteria;
import com.IEASmart.sistemaAsistencias.application.dto.StudentCriteria;
import com.IEASmart.sistemaAsistencias.domain.exception.ConflictException;
import com.IEASmart.sistemaAsistencias.domain.exception.ResourceNotFoundException;
import com.IEASmart.sistemaAsistencias.domain.model.Attendance;
import com.IEASmart.sistemaAsistencias.domain.model.Parent;
import com.IEASmart.sistemaAsistencias.domain.model.Student;
import com.IEASmart.sistemaAsistencias.domain.model.Token;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.AttendanceType;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.JustificationStatus;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Section;
import com.IEASmart.sistemaAsistencias.domain.repository.*;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.projection.AttendanceStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final AttendanceApiMapper mapper;
    private final MonthlyAttendanceApiMapper monthlyAttendanceApiMapper;
    private final StudentApiMapper studentApiMapper;
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;
    private final JustificationRepository justificationRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, StudentRepository studentRepository, ParentRepository parentRepository, AttendanceApiMapper mapper, MonthlyAttendanceApiMapper monthlyAttendanceApiMapper, StudentApiMapper studentApiMapper, TokenService tokenService, TokenRepository tokenRepository, JustificationRepository justificationRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.mapper = mapper;
        this.monthlyAttendanceApiMapper = monthlyAttendanceApiMapper;
        this.studentApiMapper = studentApiMapper;
        this.tokenService = tokenService;
        this.tokenRepository = tokenRepository;
        this.justificationRepository = justificationRepository;
    }

    public AttendanceResponse markAttendance(AttendanceRequest request, School school) {
        Student student = studentRepository.findById(request.getDni(), school)
                .orElseThrow(() -> new ConflictException("Student with DNI " + request.getDni() + " not found in school " + school.getFullName(), "STUDENT_NOT_FOUND"));

        Attendance attendance = mapper.toDomain(request);
        attendance.setStudent(student);
        LocalDate today = LocalDate.now();
        if (attendanceRepository.existsByStudentAndDate(student.getDni(), today)) {
            throw new ConflictException("Attendance already marked for student " + student.getName() + " on date " + today, "ATTENDANCE_ALREADY_MARKED");
        }
        attendance.setDate(LocalDate.now());
        attendance.setTime(LocalTime.now());

        attendanceRepository.save(attendance);
        return mapper.toResponse(attendance);
    }

    public PageResponse<AttendanceResponse> getAllAttendaces(School school, AttendanceFilter filter, Pageable page) {
        LocalDate date = filter.date() == null ? null : LocalDate.parse(filter.date());
        Section section = filter.section() == null ? null : Section.from(filter.section());
        AttendanceType attendanceType = filter.attendanceType() == null ? null : AttendanceType.from(filter.attendanceType());

        AttendanceCriteria criteria = new AttendanceCriteria(date, filter.name(), section, attendanceType);
        Page<Attendance> attendances = attendanceRepository.findAllByFilter(school, criteria, page);
        List<AttendanceResponse> content = attendances
                .getContent()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                attendances.getTotalElements(),
                attendances.getTotalPages(),
                attendances.getNumber(),
                attendances.getSize()
        );
    }

    @Transactional(readOnly = true)
    public List<MonthlyAttendanceResponse> getMonthlyAttendance(School school, AttendanceMonthlyFilter filter) {
        if (filter.section() == null) {
            throw new ConflictException("El filtro de sección es obligatorio", "SECTION_REQUIRED");
        }
        if (filter.month() == null || filter.month() < 1 || filter.month() > 12) {
            throw new ConflictException("Valor de mes invalido: " + filter.month() + ". El mes debe ser entre 1 y 12", "INVALID_MONTH_VALUE");
        }

        Section section = Section.from(filter.section());
        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), filter.month(), 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        StudentCriteria studentCriteria = new StudentCriteria(null, null, null, section);
        List<Student> students = studentRepository.findAllByFilters(school, studentCriteria, Sort.by(Sort.Order.asc("firstLastName"), Sort.Order.asc("secondLastName")));

        // Obtener sólo las asistencias del rango de fechas y de la sección solicitada
        List<Attendance> attendances = attendanceRepository.findByStudentSchoolAndSectionAndDateBetween(school, section, startDate, endDate);

        Map<String, Map<Integer, String>> dailyByDni = new HashMap<>();
        for (Attendance a : attendances) {
            String dni = a.getStudent().getDni();
            int day = a.getDate().getDayOfMonth();
            dailyByDni.computeIfAbsent(dni, k -> new HashMap<>()).put(day, a.getAttendanceType().getFullName());
        }

        return students.stream()
                .map(s -> {
                    MonthlyAttendanceResponse resp = monthlyAttendanceApiMapper.toResponse(s);
                    // Asignar mapa (vacío si no hay registros para el alumno)
                    Map<Integer, String> daily = dailyByDni.get(s.getDni());
                    resp.setDailyAttendance(Objects.requireNonNullElseGet(daily, Map::of));
                    return resp;
                })
                .toList();
    }

    public InformationAttendanceResponse getAttendanceByStudentId(String id, School school) {
        Optional<Student> studentOpt = studentRepository.findById(id, school);
        if (studentOpt.isEmpty()) {
            throw new ResourceNotFoundException("Estudiante", id);
        }
        Student student = studentOpt.get();
        InformationAttendanceResponse response = new InformationAttendanceResponse();
        response.setStudent(studentApiMapper.toResponse(student));

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.withDayOfMonth(1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        long totalAttendances = attendanceRepository.countByStudentDniAndAttendanceTypeAndDateBetween(id, AttendanceType.PRESENTE, startDate, endDate);
        long totalAbsences = attendanceRepository.countByStudentDniAndAttendanceTypeAndDateBetween(id, AttendanceType.AUSENTE, startDate, endDate);
        long totalTardies = attendanceRepository.countByStudentDniAndAttendanceTypeAndDateBetween(id, AttendanceType.TARDE, startDate, endDate);
        long totalJustifiedAbsences = attendanceRepository.countByStudentDniAndAttendanceTypeAndDateBetween(id, AttendanceType.JUSTIFICADO, startDate, endDate);

        response.setTotalAttendances(totalAttendances);
        response.setTotalAbsences(totalAbsences);
        response.setTotalLate(totalTardies);
        response.setTotalExcusedAbsences(totalJustifiedAbsences);
        response.setTotal(totalAttendances + totalAbsences + totalTardies + totalJustifiedAbsences);

        LocalDate fistDayOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate lastDayOfWeek = fistDayOfWeek.plusDays(6);

        List<Attendance> attendances = attendanceRepository.findByStudentAndDateBetween(id, fistDayOfWeek, lastDayOfWeek);
        Map<LocalDate, String> dailyAttendance = attendances.stream()
                .collect(HashMap::new, (map, attendance) -> map.put(attendance.getDate(), attendance.getAttendanceType().getFullName()), HashMap::putAll);
        response.setAttendances(dailyAttendance);
        return response;
    }

    public AttendanceInfoResponse getAttendanceById(String id) {
        Optional<Attendance> attendanceOpt = attendanceRepository.findById(id);
        if (attendanceOpt.isEmpty()) {
            throw new ResourceNotFoundException("Asistencia", id.toString());
        }
        return mapper.toInfoResponse(attendanceOpt.get());
    }

    @Transactional
    public long addMissedAttendances(School school) {
        LocalDate today = LocalDate.now();
        List<Student> students = studentRepository.findAllWithoutAttendanceOnDate(school, today);
        if(students.isEmpty()) {
            throw new ConflictException("No hay estudiantes sin asistencia registrada para la fecha " + today, "NO_STUDENTS_WITHOUT_ATTENDANCE");
        }
        List<Attendance> attendances = new ArrayList<>();
        for (Student s : students) {
            Attendance a = new Attendance();
            a.setStudent(s);
            a.setDate(today);
            a.setTime(LocalTime.now());
            a.setAttendanceType(AttendanceType.AUSENTE);
            attendances.add(a);
        }

        List<Attendance> saved = attendanceRepository.saveAll(attendances);
        List<Token> tokens = new ArrayList<>(saved.size());
        for(Attendance a : saved) {
            Token t = tokenService.generateToken(a.getId(), school);
            tokens.add(t);
        }
        tokenRepository.saveAll(tokens);

        return saved.size();
    }

    public ContactResponse getContactInfo(String attendanceId) {
        ContactResponse response = new ContactResponse();
        Optional<Attendance> attendanceOpt = attendanceRepository.findById(attendanceId);
        if(attendanceOpt.isEmpty()) {
            throw new ResourceNotFoundException("Asistencia");
        }

        Optional<Token> tokenOpt = tokenRepository.findByAttendanceIdAndUsedFalse(attendanceId);
        if(tokenOpt.isEmpty()) {
            throw new ResourceNotFoundException("Token");
        }
        Token token = tokenOpt.get();
        response.setToken(token.getToken());

        Optional<Parent> parentOpt = parentRepository.findByAlumnoId(attendanceOpt.get().getStudent().getDni());
        if(parentOpt.isEmpty()) {
            throw new ResourceNotFoundException("Padre del estudiante");
        }
        Parent parent = parentOpt.get();
        response.setNumber(parent.getPhoneNumber());

        return response;
    }

    @Transactional(readOnly = true)
    public AttendanceTodayDashboard getTodayAttendanceInfo(School school){
        LocalDateTime today = LocalDateTime.now();

        List<AttendanceStats> stats = attendanceRepository.getAttendanceStats(school, today.toLocalDate());
        AttendanceTodayDashboard dashboard = new AttendanceTodayDashboard();

        if (stats == null || stats.isEmpty()) {
            dashboard.setTotalPresences(0L);
            dashboard.setTotalAbsences(0L);
            dashboard.setTotalLate(0L);
            dashboard.setTotalAttendances(0L);
            return dashboard;
        }

        Map<AttendanceType, Long> countsByType = stats.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        AttendanceStats::getAttendanceType,
                        Collectors.summingLong(AttendanceStats::getCount)
                ));

        long present = countsByType.getOrDefault(AttendanceType.PRESENTE, 0L);
        long absent = countsByType.getOrDefault(AttendanceType.AUSENTE, 0L);
        long late = countsByType.getOrDefault(AttendanceType.TARDE, 0L);
        long justified = countsByType.getOrDefault(AttendanceType.JUSTIFICADO, 0L);

        long pendingJustifications = justificationRepository.countByStatus(JustificationStatus.PENDIENTE, school);

        dashboard.setTotalPresences(present);
        dashboard.setTotalAbsences(absent);
        dashboard.setTotalLate(late);
        dashboard.setTotalPendingJustifications(pendingJustifications);

        long totalStudents = studentRepository.countStudentsBySchool(school);
        dashboard.setTotalAttendances(totalStudents);

        return dashboard;
    }
}
