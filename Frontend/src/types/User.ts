export type UserType = 'ADMIN' | 'PROFESSOR'

export interface UserInfoResponse {
    dni: string
    name: string
    firstLastName: string
    secondLastName: string
    email: string
    role: UserType
}

export interface ProfessorRequest {
    names: string
    firstLastName: string
    secondLastName: string
    email: string
}