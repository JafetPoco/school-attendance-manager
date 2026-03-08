export interface AttendanceRequest {
    dni: string
    attendanceType: 'presente' | 'ausente' | 'tarde' | 'justificado'
}

export interface AttendanceResponse {
    dni: string
    studentName: string
    attendanceType: 'presente' | 'ausente' | 'tarde' | 'justificado'
    date: string
}