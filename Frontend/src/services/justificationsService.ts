import { http } from '@/api/http'
import { mapApiError } from '@/utils/apiErrorMapper'

import type { ServiceResult } from '@/types/ServiceResult'
import type { ApiHttpError } from '@/api/ApiHttpError'
import type { AttendanceInfoResponse } from '@/types/Attendance'
import type { JustificationRequest, JustificationResponse } from '@/types/Justification'

export async function getJustificationFormInfo(token: string): Promise<ServiceResult<AttendanceInfoResponse, ApiHttpError>> {
  try {
    const data = await http<AttendanceInfoResponse>(`/justifications/public/${token}`, {
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

export async function addJustification(request: JustificationRequest): Promise<ServiceResult<JustificationResponse, ApiHttpError>> {
  try {
    const data = await http<JustificationResponse>(`/justifications/public/submit`, {
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