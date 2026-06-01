import { http } from '@/api/http'
import { mapApiError } from '@/utils/apiErrorMapper'

import type { ServiceResult } from '@/types/ServiceResult'
import type { ApiHttpError } from '@/api/ApiHttpError'
import type { PageRequest, PageResponse, Sort } from '@/types/Pages'
import type { AttendanceFilter, AttendanceResponse, ContactResponse, MissedAttendance, StudentAttendanceDetailsResponse } from '@/types/Attendance'

function buildStudentsQuery(filter: AttendanceFilter, page: PageRequest, sort?: Sort): string {
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

  queryParams.set('page', String(page.page))
  queryParams.set('size', String(page.size))

  if(sort?.field) {
    queryParams.set('sort', `${sort.field},${sort.direction}`)
  }

  const query = queryParams.toString()
  return query ? `?${query}` : ''
}

export async function getAttendances(filter: AttendanceFilter, page: PageRequest, sort?: Sort): Promise<ServiceResult<PageResponse<AttendanceResponse>, ApiHttpError>> {
  try {
    const query = buildStudentsQuery(filter, page, sort)

    const data = await http<PageResponse<AttendanceResponse>>(`/attendances${query}`, {
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

export async function getStudentAttendance(studentId: string): Promise<ServiceResult<StudentAttendanceDetailsResponse, ApiHttpError>> {
  try {
    const data = await http<StudentAttendanceDetailsResponse>(`/attendances/${studentId}`, {
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

export async function createMissedAttendance(): Promise<ServiceResult<MissedAttendance, ApiHttpError>> {
  try {
    const data = await http<MissedAttendance>(`/attendances/missed`, {
      method: 'GET',
    }, 600000)

    return { success: true, data }
  } catch (error) {
    return {
      success: false,
      error: mapApiError(error)
    }
  }
}

export async function contactStudent(id: string): Promise<ServiceResult<ContactResponse, ApiHttpError>> {
  try {
    const data = await http<ContactResponse>(`/attendances/contact/${id}`, {
      method: 'GET',
    })  
    return { success: true, data }
  } catch (error) {
    return {
      success: false,
      error: mapApiError(error)
    }
  }
}

export async function getMonthlyExcel(month: number): Promise<ServiceResult<Blob, ApiHttpError>> {
  try {
    const queryParams = new URLSearchParams({
      month: String(month),
    })

    const data = await http<Blob>(`/attendances/monthly/excel?${queryParams.toString()}`, {
      method: 'GET',
      responseType: 'blob',
    }, 600000)
    return { success: true, data }
  } catch (error) {
    return {
      success: false,
      error: mapApiError(error)
    }
  }
}