import { http } from '@/api/http'
import { mapApiError } from '@/utils/apiErrorMapper'

import type { ServiceResult } from '@/types/ServiceResult'
import type { ApiHttpError } from '@/api/ApiHttpError'
import type { ProfessorRequest, UserInfoResponse } from '@/types/User'

export async function getUsers(): Promise<ServiceResult<UserInfoResponse[], ApiHttpError>> {
  try {
    const data = await http<UserInfoResponse[]>(`/user`, {
      method: 'GET'
    })

    return { success: true, data }
  } catch (error) {
    return {
      success: false,
      error: mapApiError(error)
    }
  }
}

export async function addProfessor(request: ProfessorRequest): Promise<ServiceResult<UserInfoResponse, ApiHttpError>> {
  try {
    const data = await http<UserInfoResponse>(`/user`, {
      method: 'POST',
      body: JSON.stringify(request)
    })

    return { success: true, data }
  } catch (error) {
    return {
      success: false,
      error: mapApiError(error)
    }
  }
}

