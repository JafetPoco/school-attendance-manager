import { http } from '@/api/http'
import { mapApiError } from '@/utils/apiErrorMapper'

import type { ServiceResult } from '@/types/ServiceResult'
import type { ApiHttpError } from '@/api/ApiHttpError'
import type { CountResponse } from '@/types/token'

export async function deleteExpiredTokens(): Promise<ServiceResult<CountResponse, ApiHttpError>> {
  try {
    const data = await http<CountResponse>(`/tokens/expired`, {
      method: 'DELETE'
    })

    return { success: true, data }
  } catch (error) {
    return {
      success: false,
      error: mapApiError(error)
    }
  }
}