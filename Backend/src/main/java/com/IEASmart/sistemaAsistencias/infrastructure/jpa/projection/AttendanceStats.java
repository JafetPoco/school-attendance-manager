package com.IEASmart.sistemaAsistencias.infrastructure.jpa.projection;

import com.IEASmart.sistemaAsistencias.domain.model.valueObject.AttendanceType;

public interface AttendanceStats {
    AttendanceType getAttendanceType();
    long getCount();
}
