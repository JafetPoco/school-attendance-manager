import { http } from '@/api/http'
import { mapApiError } from '@/utils/apiErrorMapper'

import type { ServiceResult } from '@/types/ServiceResult'
import type { ApiHttpError } from '@/api/ApiHttpError'
import type { AttendanceInfoResponse } from '@/types/Attendance'
import type { JustificationFilter, JustificationProfessorRequest, JustificationRequest, JustificationResponse } from '@/types/Justification'
import type { PageRequest, PageResponse, Sort } from '@/types/Pages'

function buildJustificationQuery(filter: JustificationFilter, page: PageRequest, sort?: Sort): string {
  const queryParams = new URLSearchParams()

  Object.entries(filter).forEach(([key, value]) => {
    if (typeof value !== 'string') return
    const normalized = value.trim()
    if (!normalized) return
    queryParams.set(key, normalized)
  })

  queryParams.set('page', String(page.page))
  queryParams.set('size', String(page.size))

  if(sort?.field) {
    queryParams.set('sort', `${sort.field},${sort.direction}`)
  }

  const query = queryParams.toString()
  return query ? `?${query}` : ''
}

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

export async function getPendingJustifications(filter: JustificationFilter, page: PageRequest, sort?: Sort): Promise<ServiceResult<PageResponse<JustificationResponse>, ApiHttpError>> {
  try {
    const query = buildJustificationQuery(filter, page, sort)

    const data = await http<PageResponse<JustificationResponse>>(`/justifications/pending${query}`, {
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

export async function approveJustification(idJustification: bigint): Promise<ServiceResult<JustificationResponse, ApiHttpError>> {
  try {
    const data = await http<JustificationResponse>(`/justifications/${idJustification}/approve`, {
      method: 'POST'
    })  

    return { success: true, data }
  } catch (error) {
    return {
      success: false,
      error: mapApiError(error)
    }
  }
}

export async function rejectJustification(idJustification: bigint): Promise<ServiceResult<JustificationResponse, ApiHttpError>> {
  try {
    const data = await http<JustificationResponse>(`/justifications/${idJustification}/reject`, {
      method: 'POST'
    })  

    return { success: true, data }
  } catch (error) {
    return {
      success: false,
      error: mapApiError(error)
    }
  }
}

export async function addProfessorJustification(request: JustificationProfessorRequest): Promise<ServiceResult<JustificationResponse, ApiHttpError>> {
  try {
    const data = await http<JustificationResponse>(`/justifications/submit`, {
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