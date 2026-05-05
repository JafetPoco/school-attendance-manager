package com.IEASmart.sistemaAsistencias.infrastructure.jpa.projection;

import java.time.LocalDate;

public interface WeekAttendanceStats {
    LocalDate getDay();
    int getAttendances();
    int getAbsences();
    int getLate();
}
