export interface PageResponse<T> {
    content: T[]
    totalElements: bigint
    totalPages: number
    page: number
    size: number
}

export interface PageRequest {
    page: number
    size: number
}

export interface Sort {
    field: string
    direction: 'asc' | 'desc'
}