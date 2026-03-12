export interface MonthlyAttendance {
    dni: string,
    name: string,
    lastName: string,
    section: string,
    dailyAttendance: Record<number, 'presente' | 'ausente' | 'tarde' | 'justificado'>
}

export interface MonthlyAttendanceFilter {
    month: number
    section: string
}