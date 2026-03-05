export interface StudentRequest {
    dni: string
    name: string
    firstLastName: string
    secondLastName: string
    level: 'PRIMARIA' | 'SECUNDARIA'
    grade: 'PRIMERO' | 'SEGUNDO' | 'TERCERO' | 'CUARTO' | 'QUINTO' | 'SEXTO'
    section: 'A' | 'B' | 'C' | 'D' | 'E' | 'F'
}

export interface StudentResponse {
    dni: string
    name: string
    firstLastName: string
    secondLastName: string
    level: 'PRIMARIA' | 'SECUNDARIA'
    grade: 'PRIMERO' | 'SEGUNDO' | 'TERCERO' | 'CUARTO' | 'QUINTO' | 'SEXTO'
    section: 'A' | 'B' | 'C' | 'D' | 'E' | 'F'
}

export interface StudentFilter {
    name?: string
    level?: string
    grade?: string
    section?: string
}