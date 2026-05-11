import { http } from '@/api/http'
import { mapApiError } from '@/utils/apiErrorMapper'
import type { ServiceResult } from '@/types/ServiceResult'
import type { ApiHttpError } from '@/api/ApiHttpError'
import type { ImportResponse } from '@/types/Import'

export async function importStudentsExcel(file: File): Promise<ServiceResult<ImportResponse, ApiHttpError>> {
  try {
    const formData = new FormData()
    formData.append('file', file)

    const data = await http<ImportResponse>('/parents/import', {
      method: 'POST',
      body: formData
    }, 600000)

    return { success: true, data }
  } catch (error) {
    return {
      success: false,
      error: mapApiError(error)
    }
  }
}

export async function getTemplate(): Promise<ServiceResult<Blob, ApiHttpError>> {
  try {
    const response = await fetch('http://localhost:8081/api/parents/template', {
      method: 'GET',
      credentials: 'include'
    })

    if (!response.ok) {
      throw new Error(`Error HTTP ${response.status}`)
    }

    const blob = await response.blob()
    return { success: true, data: blob }
  } catch (error) {
    return {
      success: false,
      error: mapApiError(error)
    }
  }
}