export interface StudentRequest {
    dni: string
    name: string
    firstLastName: string
    secondLastName: string
    level: 'PRIMARIA' | 'SECUNDARIA'
    grade: 'PRIMERO' | 'SEGUNDO' | 'TERCERO' | 'CUARTO' | 'QUINTO' | 'SEXTO'
    section: 'A' | 'B' | 'C' | 'D' | 'E' | 'F'
}