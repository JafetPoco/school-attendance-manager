export interface StudentRequest {
    dni: string
    name: string
    firstLastName: string
    secondLastName: string
    level: 'PRIMARIA' | 'SECUNDARIA'
    grade: 'PRIMERO' | 'SEGUNDO' | 'TERCERO' | 'CUARTO' | 'QUINTO' | 'SEXTO'
    section: 'BENJAMIN' | 'NOE' | 'MOISES' | 'DAVID' | 'SALOMON' | 'JACOB' | 'ENOC' | 'JOSE' | 'GEDEON' | 'JOSUE' | 'ELIAS' | 'ELISEO' | 'DANIEL' | 'ESTEBAN' | 'MATEO' | 'JONATAN'
}

export interface StudentResponse {
    dni: string
    name: string
    firstLastName: string
    secondLastName: string
    level: 'PRIMARIA' | 'SECUNDARIA'
    grade: 'PRIMERO' | 'SEGUNDO' | 'TERCERO' | 'CUARTO' | 'QUINTO' | 'SEXTO'
    section: 'BENJAMIN' | 'NOE' | 'MOISES' | 'DAVID' | 'SALOMON' | 'JACOB' | 'ENOC' | 'JOSE' | 'GEDEON' | 'JOSUE' | 'ELIAS' | 'ELISEO' | 'DANIEL' | 'ESTEBAN' | 'MATEO' | 'JONATAN'
}

export interface StudentFilter {
    name?: string
    level?: string
    grade?: string
    section?: string
}