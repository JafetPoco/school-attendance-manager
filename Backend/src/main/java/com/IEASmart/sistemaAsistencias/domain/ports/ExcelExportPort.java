package com.IEASmart.sistemaAsistencias.domain.ports;

import com.IEASmart.sistemaAsistencias.api.dto.response.MonthlyAttendanceResponse;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Section;

import java.util.List;
import java.util.Map;

public interface ExcelExportPort {
    byte[] exportToExcelMultiSheet(Map<?, List<MonthlyAttendanceResponse>> data);
}
