export interface JustificationRequest {
    token: string
    idAttendance: bigint
    description: string
    urlEvidence: string
}

export interface JustificationResponse {
    studentName: string
    attendanceDate: string
    description: string
    urlEvidence: string
    justificationDate: string
}