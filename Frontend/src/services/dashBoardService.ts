import type { ApiHttpError } from '@/api/ApiHttpError'
import { http } from '@/api/http'
import type { DashboardResponse } from '@/types/Attendance'
import type { AuthUser } from '@/types/AuthUser'
import type { ServiceResult } from '@/types/ServiceResult'
import { mapApiError } from '@/utils/apiErrorMapper'

export async function getCurrentUser(): Promise<AuthUser> {
  return http<AuthUser>('/user/me')
}

export async function logout(): Promise<void> {
  await http<void>('/logout', {
    method: 'POST'
  })
}

export async function attendancesStats(): Promise<ServiceResult<DashboardResponse, ApiHttpError>> {
  try {
    const data = await http<DashboardResponse>(`/dashboard`, {
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