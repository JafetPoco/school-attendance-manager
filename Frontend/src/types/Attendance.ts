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