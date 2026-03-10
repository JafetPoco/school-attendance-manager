import { http } from '@/api/http'
import { mapApiError } from '@/utils/apiErrorMapper'

import type { ServiceResult } from '@/types/ServiceResult'
import type { ApiHttpError } from '@/api/ApiHttpError'
import type { MonthlyAttendance, MonthlyAttendanceFilter } from '@/types/MonthlyAttendance'

function buildStudentsQuery(filter: MonthlyAttendanceFilter): string {
  const queryParams = new URLSearchParams()

  Object.entries(filter).forEach(([key, value]) => {
    if (value === undefined || value === null) return
    if (typeof value === 'string') {
      const normalized = value.trim()
      if (!normalized) return
      queryParams.set(key, normalized)
      return
    }
    queryParams.set(key, String(value))
  })
  const query = queryParams.toString()
  return query ? `?${query}` : ''
}

export async function getMonthlyAttendance(filter: MonthlyAttendanceFilter): Promise<ServiceResult<MonthlyAttendance[], ApiHttpError>> {
  try {
    const query = buildStudentsQuery(filter)

    console.log('Query:', query)
    const data = await http<MonthlyAttendance[]>(`/attendances/monthly${query}`, {
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