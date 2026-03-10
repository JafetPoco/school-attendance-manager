package com.IEASmart.sistemaAsistencias.application.service;

import com.IEASmart.sistemaAsistencias.api.dto.request.AttendanceFilter;
import com.IEASmart.sistemaAsistencias.api.dto.request.AttendanceMonthlyFilter;
import com.IEASmart.sistemaAsistencias.api.dto.request.AttendanceRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.AttendanceResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.MonthlyAttendanceResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.PageResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.StudentResponse;
import com.IEASmart.sistemaAsistencias.api.mapper.AttendanceApiMapper;
import com.IEASmart.sistemaAsistencias.application.dto.AttendanceCriteria;
import com.IEASmart.sistemaAsistencias.application.dto.StudentCriteria;
import com.IEASmart.sistemaAsistencias.domain.exception.ConflictException;
import com.IEASmart.sistemaAsistencias.domain.model.Attendance;
import com.IEASmart.sistemaAsistencias.domain.model.Student;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.AttendanceType;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Section;
import com.IEASmart.sistemaAsistencias.domain.repository.AttendanceRepository;
import com.IEASmart.sistemaAsistencias.domain.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final AttendanceApiMapper mapper;

    public AttendanceService(AttendanceRepository attendanceRepository, StudentRepository studentRepository, AttendanceApiMapper mapper) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.mapper = mapper;
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
                .toList();

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
        Page<Student> studentsPage = studentRepository.findAllByFilters(school, studentCriteria, Pageable.unpaged());

        List<Student> students = studentsPage.getContent();
        List<Attendance> attendances = attendanceRepository.findByStudentSchoolAndDateBetween(school, startDate, endDate);

        Map<String, Map<AttendanceType, Long>> countsByDni = attendances.stream()
                .filter(a -> a.getStudent() != null && a.getStudent().getDni() != null && a.getAttendanceType() != null)
                .collect(Collectors.groupingBy(a -> a.getStudent().getDni(),
                        Collectors.groupingBy(Attendance::getAttendanceType, Collectors.counting())));

        Map<String, Map<Integer, String>> dailyByDni = new HashMap<>();
        for (Attendance a : attendances) {
            if (a == null || a.getStudent() == null || a.getStudent().getDni() == null || a.getDate() == null || a.getAttendanceType() == null)
                continue;
            String dni = a.getStudent().getDni();
            int day = a.getDate().getDayOfMonth();
            Map<Integer, String> days = dailyByDni.computeIfAbsent(dni, k -> new HashMap<>());
            days.put(day, a.getAttendanceType().getFullName());
        }

        return students.stream().map(student -> {
            String dni = student.getDni();
            Map<AttendanceType, Long> counts = countsByDni.getOrDefault(dni, Map.of());
            long presentCount = counts.getOrDefault(AttendanceType.PRESENTE, 0L);
            long absentCount = counts.getOrDefault(AttendanceType.AUSENTE, 0L);
            long lateCount = counts.getOrDefault(AttendanceType.TARDE, 0L);
            long justifiedCount = counts.getOrDefault(AttendanceType.JUSTIFICADO, 0L);
            long total = presentCount + absentCount + lateCount + justifiedCount;

            StudentResponse studentResponse = new StudentResponse(
                    dni,
                    student.getName(),
                    student.getFirstLastName(),
                    student.getSecondLastName(),
                    student.getLevel(),
                    student.getGrade(),
                    student.getSection()
            );

            Map<Integer, String> daily = dailyByDni.getOrDefault(dni, Map.of());
            Map<String, Object> summary = new HashMap<>();
            summary.put("present", presentCount);
            summary.put("absent", absentCount);
            summary.put("late", lateCount);
            summary.put("justified", justifiedCount);
            summary.put("total", total);

            return new MonthlyAttendanceResponse(studentResponse, daily, summary);
        }).toList();
    }

}
