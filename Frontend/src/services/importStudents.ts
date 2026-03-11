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
    })

    return { success: true, data }
  } catch (error) {
    return {
      success: false,
      error: mapApiError(error)
    }
  }
}