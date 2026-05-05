import { http } from '@/api/http'
import { mapApiError } from '@/utils/apiErrorMapper'
import type { ServiceResult } from '@/types/ServiceResult'
import type { ApiHttpError } from '@/api/ApiHttpError'

export async function importStudentsExcel(file: File): Promise<ServiceResult<null, ApiHttpError>> {
  try {
    const formData = new FormData()
    formData.append('file', file)

    const data = await http<null>('/parents/import', {
      method: 'POST',
      body: formData
    }, 35000)

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