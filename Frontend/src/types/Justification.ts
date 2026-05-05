export interface JustificationRequest {
    token: string
    idAttendance: bigint
    description: string
    urlEvidence: string
}

export interface JustificationProfessorRequest {
    idAttendance: bigint
    description: string
}

export interface JustificationResponse {
    id: bigint
    studentName: string
    attendanceDate: string
    description: string
    urlEvidence: string
    justificationDate: string
}

export interface JustificationFilter {
    dateFilter?: 'TODAY' | 'WEEK' | 'MONTH' 
}

export interface AceptJustificationFilter {
    date: string
    name: string
}