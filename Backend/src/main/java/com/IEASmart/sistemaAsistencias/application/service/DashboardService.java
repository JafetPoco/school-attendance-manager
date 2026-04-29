package com.IEASmart.sistemaAsistencias.application.service;

import com.IEASmart.sistemaAsistencias.api.dto.response.DashboardResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.StatisticsToday;
import com.IEASmart.sistemaAsistencias.api.dto.response.StudentsTopLate;
import com.IEASmart.sistemaAsistencias.api.dto.response.WeekSumary;
import com.IEASmart.sistemaAsistencias.api.mapper.DashboardApiMapper;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.AttendanceType;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.JustificationStatus;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.repository.AttendanceRepository;
import com.IEASmart.sistemaAsistencias.domain.repository.JustificationRepository;
import com.IEASmart.sistemaAsistencias.domain.repository.StudentRepository;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.projection.AttendanceStats;
import com.IEASmart.sistemaAsistencias.infrastructure.jpa.projection.WeekAttendanceStats;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final AttendanceRepository attendanceRepository;
    private final JustificationRepository justificationRepository;
    private final StudentRepository studentRepository;
    private final DashboardApiMapper dashboardApiMapper;

    public DashboardService(AttendanceRepository attendanceRepository, JustificationRepository justificationRepository, StudentRepository studentRepository, DashboardApiMapper dashboardApiMapper) {
        this.attendanceRepository = attendanceRepository;
        this.justificationRepository = justificationRepository;
        this.studentRepository = studentRepository;
        this.dashboardApiMapper = dashboardApiMapper;
    }

    public DashboardResponse getDashboardData(School school) {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());

        DashboardResponse response = new DashboardResponse();
        long totalStudents = studentRepository.countStudentsBySchool(school);
        response.setTotalStudents(totalStudents);

        response.setStatisticsToday(getStatisticsToday(school, today));

        response.setWeekSumary(getWeekSumary(school, monday, today));

        response.setStudentsTopLate(getTopLate(school, firstDayOfMonth, today));

        return response;
    }

    @Transactional(readOnly = true)
    private StatisticsToday getStatisticsToday(School school, LocalDate today) {
        List<AttendanceStats> stats = attendanceRepository.getAttendanceStats(school, today);
        StatisticsToday dashboard = new StatisticsToday();

        if (stats == null || stats.isEmpty()) {
            dashboard.setTotalPresences(0L);
            dashboard.setTotalAbsences(0L);
            dashboard.setTotalLate(0L);
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

        long pendingJustifications = justificationRepository.countByStatus(JustificationStatus.PENDIENTE, school);

        dashboard.setTotalPresences(present);
        dashboard.setTotalAbsences(absent);
        dashboard.setTotalLate(late);
        dashboard.setTotalPendingJustifications(pendingJustifications);

        return dashboard;
    }

    private List<WeekSumary> getWeekSumary(School school, LocalDate startDate, LocalDate endDate) {
        List<WeekAttendanceStats> weekStats = attendanceRepository.getWeekAttendanceStats(school, startDate, endDate);
        return weekStats.stream()
                .filter(Objects::nonNull)
                .map(dashboardApiMapper::toResponseWeekSumary)
                .collect(Collectors.toList());

    }

    private List<StudentsTopLate> getTopLate(School school, LocalDate startDate, LocalDate endDate) {
        List<StudentsTopLate> topLate = attendanceRepository.getTopLateStudents(school, startDate, endDate).stream()
                .filter(Objects::nonNull)
                .map(dashboardApiMapper::toResponseStudentsTopLate)
                .collect(Collectors.toList());
        return topLate;
    }
}
