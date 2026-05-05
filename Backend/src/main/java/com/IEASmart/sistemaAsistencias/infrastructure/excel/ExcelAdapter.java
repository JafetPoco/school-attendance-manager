package com.IEASmart.sistemaAsistencias.infrastructure.excel;

import com.IEASmart.sistemaAsistencias.api.dto.response.MonthlyAttendanceResponse;
import com.IEASmart.sistemaAsistencias.domain.ports.ExcelExportPort;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.*;

@Component
public class ExcelAdapter implements ExcelExportPort {

    @Override
    public byte[] exportToExcelMultiSheet(Map<?, List<MonthlyAttendanceResponse>> data){
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Estilos (definidos una sola vez para todo el libro)
            CellStyle headerStyle = createHeaderStyle(workbook);
            // estilos específicos para estados (creados vía helper para consistencia)
            CellStyle presentCellStyle = createStateStyle(workbook, IndexedColors.LIGHT_GREEN);
            CellStyle absentCellStyle = createStateStyle(workbook, IndexedColors.ROSE);
            CellStyle lateCellStyle = createStateStyle(workbook, IndexedColors.LIGHT_ORANGE);
            CellStyle justifiedCellStyle = createStateStyle(workbook, IndexedColors.LIGHT_BLUE);

            // Crear una hoja por cada sección
            for (Map.Entry<?, List<MonthlyAttendanceResponse>> entry : data.entrySet()) {
                Object section = entry.getKey();
                List<MonthlyAttendanceResponse> sectionData = entry.getValue();

                // Nombre de la hoja (ej: "5to A", "5to B", etc.)
                String sheetName = getSheetName(section);

                // Crear hoja
                Sheet sheet = workbook.createSheet(sheetName);

                // Crear encabezados
                String[] headers = getHeaders();
                createHeaderRow(sheet, headers, headerStyle);

                // Llenar datos
                int rowNum = 1;
                for (MonthlyAttendanceResponse item : sectionData) {
                    Row row = sheet.createRow(rowNum++);
                    // pasar los estilos adecuados
                    mapToRow(row, item, presentCellStyle, absentCellStyle, lateCellStyle, justifiedCellStyle);
                }

                // Configurar hoja
                autoSizeColumns(sheet, headers.length);
                sheet.createFreezePane(0, 1);
            }

            // Crear hoja de resumen (opcional pero recomendado)
            createSummarySheet(workbook, data, headerStyle);

            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando Excel multisección", e);
        }
    }

    private String[] getHeaders() {
        // Cabeceras dinámicas para los días del mes
        List<String> headers = new ArrayList<>();
        headers.add("Alumno");

        // Días del mes (1 al 31)
        for (int day = 1; day <= 31; day++) {
            headers.add(String.valueOf(day));
        }
        return headers.toArray(new String[0]);
    }

    // mapToRow aplica estilos de éxito/advertencia según el estado
    private void mapToRow(Row row, MonthlyAttendanceResponse attendance,
                          CellStyle presentStyle, CellStyle absentStyle, CellStyle lateStyle, CellStyle justifiedStyle) {
        Cell cell = row.createCell(0);
        cell.setCellValue((attendance.getName() == null ? "" : attendance.getName()) + " " + (attendance.getLastName() == null ? "" : attendance.getLastName()));

        Map<Integer, String> dailyAttendance = attendance.getDailyAttendance();
        if (dailyAttendance == null || dailyAttendance.isEmpty()) return;

        for (Map.Entry<Integer, String> entry : dailyAttendance.entrySet()) {
            Integer dayObj = entry.getKey();
            if (dayObj == null) continue;
            int day = dayObj;
            String status = entry.getValue();

            // Validar rango de columna: columna 0 = alumno, por tanto día 1 va en columna 1
            int colIndex = Math.max(1, Math.min(31, day));
            Cell dayCell = row.createCell(colIndex);

            String display = status == null ? "" : parseStatus(status);
            dayCell.setCellValue(display);

            // Aplicar estilos según el estado de asistencia
            if (status != null) {
                String s = status.trim().toUpperCase();
                if (s.contains("PRES") || s.contains("PRESENTE") || s.equals("P")) {
                    dayCell.setCellStyle(presentStyle);
                } else if (s.contains("AUS") || s.contains("AUSENTE") || s.equals("A")) {
                    dayCell.setCellStyle(absentStyle);
                } else if (s.contains("TARD") || s.contains("TARDANZA") || s.equals("T")) {
                    dayCell.setCellStyle(lateStyle);
                } else if (s.contains("JUS") || s.contains("JUSTIFICADA") || s.equals("J")) {
                    dayCell.setCellStyle(justifiedStyle);
                }
            }
        }
    }

    private String parseStatus(String s) {
        if (s == null) return "";
        if (s.isEmpty()) return "";

        String up = s.trim().toUpperCase();
        if (up.contains("PRES") || up.contains("PRESENTE") || up.equals("P")) {
            return "P";
        } else if (up.contains("AUS") || up.contains("AUSENTE") || up.equals("A")) {
            return "F";
        } else if (up.contains("TARD") || up.contains("TARDANZA") || up.equals("T")) {
            return "T";
        } else if (up.contains("JUS") || up.contains("JUSTIFICADA") || up.equals("J")) {
            return "J";
        }
        return s; // valor original si no se reconoce
    }

    // helper para crear estilos de estado consistentes
    private CellStyle createStateStyle(Workbook workbook, IndexedColors fillColor) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(fillColor.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    // Metodo para crear hoja de resumen general
    private void createSummarySheet(Workbook workbook, Map<?, List<MonthlyAttendanceResponse>> dataBySheet,
                                    CellStyle headerStyle) {
        Sheet summarySheet = workbook.createSheet("📊 Resumen General");

        // Encabezados
        String[] headers = {"Sección", "Total Alumnos", "Presentes", "Ausentes",
                "Tardanzas", "Asistencia %"};
        Row headerRow = summarySheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Llenar resumen por sección
        int rowNum = 1;
        for (Map.Entry<?, List<MonthlyAttendanceResponse>> entry : dataBySheet.entrySet()) {
            Row row = summarySheet.createRow(rowNum++);
            List<MonthlyAttendanceResponse> data = entry.getValue();

            row.createCell(0).setCellValue(entry.getKey().toString()); // Sección
            row.createCell(1).setCellValue(data.size()); // Total alumnos

            // Calcular estadísticas de asistencia
            Map<String, Long> stats = calculateAttendanceStats(data);
            row.createCell(2).setCellValue(stats.getOrDefault("PRESENTE", 0L));
            row.createCell(3).setCellValue(stats.getOrDefault("AUSENTE", 0L));
            row.createCell(4).setCellValue(stats.getOrDefault("TARDANZA", 0L));

            double attendancePercent = calculateAttendancePercentage(stats);
            row.createCell(5).setCellValue(attendancePercent + "%");
        }

        autoSizeColumns(summarySheet, headers.length);
    }

    // Helper: Nombre de la hoja
    private String getSheetName(Object section) {
        String baseName = section == null ? "Seccion" : section.toString();
        // Limitar a 31 caracteres (límite de Excel)
        if (baseName.length() > 31) {
            baseName = baseName.substring(0, 28) + "...";
        }
        return baseName;
    }


    private void countStatusFromObject(Map<String, Long> stats, Object v) {
        if (v == null) return;
        String s = v.toString().trim().toUpperCase();
        if (s.isEmpty()) return;

        // Heurísticas: buscar subcadenas comunes
        if (s.contains("PRES") || s.contains("PRESENTE") || s.equals("P")) {
            stats.merge("PRESENTE", 1L, Long::sum);
        } else if (s.contains("AUS") || s.contains("AUSENTE") || s.equals("A")) {
            stats.merge("AUSENTE", 1L, Long::sum);
        } else if (s.contains("TARD") || s.contains("TARDANZA") || s.equals("T")) {
            stats.merge("TARDANZA", 1L, Long::sum);
        }
    }

    private double calculateAttendancePercentage(Map<String, Long> stats) {
        long total = stats.values().stream().mapToLong(Long::longValue).sum();
        if (total == 0) return 0.0;
        long presentes = stats.getOrDefault("PRESENTE", 0L);
        return Math.round((presentes * 100.0 / total) * 10) / 10.0;
    }

    private Map<String, Long> calculateAttendanceStats(List<MonthlyAttendanceResponse> data) {
        Map<String, Long> stats = new HashMap<>();
        for (MonthlyAttendanceResponse item : data) {
            if (item == null) continue;
            Map<Integer, String> daily = item.getDailyAttendance();
            if (daily == null) continue;
            for (Object v : daily.values()) {
                countStatusFromObject(stats, v);
            }
        }
        return stats;
    }

    private void createHeaderRow(Sheet sheet, String[] headers, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
            if (sheet.getColumnWidth(i) > 8000) {
                sheet.setColumnWidth(i, 8000);
            }
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
}
