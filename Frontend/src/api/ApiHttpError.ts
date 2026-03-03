import type { ErrorResponse } from '@/types/Error'

export class ApiHttpError extends Error {
  readonly status: number
  readonly errorResponse?: ErrorResponse

  constructor(message: string, status: number, errorResponse?: ErrorResponse) {
    super(message)
    this.name = 'ApiHttpError'
    this.status = status
    this.errorResponse = errorResponse
    Object.setPrototypeOf(this, new.target.prototype)
  }
}
