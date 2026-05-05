import type { ErrorResponse } from '@/types/Error'
import { ApiHttpError } from './ApiHttpError'

const BASE_URL = import.meta.env.VITE_API_URL

function isErrorResponse(value: unknown): value is ErrorResponse {
  if (!value || typeof value !== 'object') return false
  const candidate = value as Partial<ErrorResponse>
  return (
    (typeof candidate.message === 'string') ||
    (typeof candidate.error === 'string') ||
    (typeof candidate.code === 'string') ||
    (typeof candidate.status === 'number'))
}

async function safeParseJson(response: Response): Promise<unknown> {
  try {
    return await response.json()
  } catch {
    return null
  }
}

async function parseSuccessfulResponse<T>(
  response: Response,
  responseType: 'json' | 'blob' | 'text'
): Promise<T> {
  if (response.status === 204) {
    return null as T
  }

  if (responseType === 'blob') {
    return response.blob() as Promise<T>
  }

  if (responseType === 'text') {
    return response.text() as Promise<T>
  }

  return response.json() as Promise<T>
}

export async function http<T>(
  endpoint: string,
  options: (RequestInit & { responseType?: 'json' | 'blob' | 'text' }) = {},
  timeout = 10000
): Promise<T> {
  const controller = new AbortController()
  const timeoutId = setTimeout(() => controller.abort(), timeout)

  try {
    const defaultHeaders: Record<string, string> = {
      ...(options.headers as Record<string, string> | undefined)
    }

    // Let the browser set multipart boundaries when sending FormData.
    if (!(options.body instanceof FormData) && !defaultHeaders['Content-Type']) {
      defaultHeaders['Content-Type'] = 'application/json'
    }

    const { responseType = 'json', ...requestOptions } = options

    const response = await fetch(`${BASE_URL}${endpoint}`, {
      credentials: 'include',
      headers: defaultHeaders,
      signal: controller.signal,
      ...requestOptions
    })

    clearTimeout(timeoutId)

    if (!response.ok) {
      const parsedBody = await safeParseJson(response)

      if (isErrorResponse(parsedBody)) {
        const message =
          parsedBody.message ??
          parsedBody.error ??
          `Error HTTP ${response.status}`

        throw new ApiHttpError(message, response.status, parsedBody)
      }

      throw new ApiHttpError(
        `Error HTTP ${response.status}: ${response.statusText}`, 
        response.status
      )
    }

    return parseSuccessfulResponse<T>(response, responseType)
  } catch (error) {
    clearTimeout(timeoutId)
    
    if (error instanceof ApiHttpError) {
      throw error
    }
    
    if (error instanceof DOMException && error.name === 'AbortError') {
      throw new ApiHttpError('Timeout de la petición', 408, undefined)
    }
    
    // Error de red
    throw new ApiHttpError(
      error instanceof Error ? error.message : 'Error de conexión',
      0,
      undefined
    )
  }
}