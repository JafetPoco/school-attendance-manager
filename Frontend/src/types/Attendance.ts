import type { StudentResponse } from "./Student"

export interface AttendanceRequest {
    dni: string
    attendanceType: 'presente' | 'ausente' | 'tarde' | 'justificado'
}

export interface AttendanceResponse {
    dni: string
    studentName: string
    studentFirstLastName: string
    studentSecondLastName: string
    attendanceType: 'presente' | 'ausente' | 'tarde' | 'justificado'
    date: string
}

export interface AttendanceFilter {
    date?: string
    name?: string
    section?: string
    attendanceType?: string
}

export interface StudentAttendanceDetailsResponse {
    student: StudentResponse
    totalAttendances: number
    totalAbsences: number
    totalLate: number
    totalExcusedAbsences: number
    total: number
    attendances: Map<string, 'presente' | 'ausente' | 'tarde' | 'justificado'> // Mapa de fecha a tipo de asistencia
}

export interface MissedAttendance {
    count: number
}