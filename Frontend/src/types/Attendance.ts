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
    token: string
    idAttendance: bigint
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

export interface AttendanceInfoResponse {
    id: bigint
    fullName: string
    date: string
    grade: string
}

export interface ContactResponse {
    number: string
    parentName: string
    studentName: string
    token: string
}

export interface AttendanceStats {
    totalAttendances: number
    totalPresences: number
    totalAbsences: number
    totalPendingJustifications: number
    totalLate: number
}
