export interface StudentRequest {
    dni: string
    name: string
    firstLastName: string
    secondLastName: string
    classId: number
}

export interface StudentResponse {
    dni: string
    name: string
    firstLastName: string
    secondLastName: string
    level: 'PRIMARIA' | 'SECUNDARIA'
    grade: 'PRIMERO' | 'SEGUNDO' | 'TERCERO' | 'CUARTO' | 'QUINTO' | 'SEXTO'
    section: string
}

export interface StudentFilter {
    name?: string
    level?: string
    grade?: string
    section?: string
}

export interface StudentSuggestionResponse {
    fullName: string
    dni: string
}