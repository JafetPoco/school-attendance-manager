import { http } from '@/api/http'
import { mapApiError } from '@/utils/apiErrorMapper'
import type { ServiceResult } from '@/types/ServiceResult'
import type { ApiHttpError } from '@/api/ApiHttpError'
import type { AttendanceRequest, AttendanceResponse } from '@/types/Attendance'

export async function addAttendance(request: AttendanceRequest): Promise<ServiceResult<AttendanceResponse, ApiHttpError>> {
  try {
    const data = await http<AttendanceResponse>('/attendances', {
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