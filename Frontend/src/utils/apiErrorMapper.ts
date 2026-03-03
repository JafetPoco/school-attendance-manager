import { ApiHttpError } from '../api/ApiHttpError'

export function mapApiError(error: unknown): ApiHttpError {
  if (error instanceof ApiHttpError) {
    return error
  }

  if (error instanceof Error) {
    return new ApiHttpError(error.message, 0)
  }

  return new ApiHttpError('Ocurrió un error inesperado.', 0)
}
