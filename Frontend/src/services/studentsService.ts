import { http } from '@/api/http'
import { mapApiError } from '@/utils/apiErrorMapper'

import type { ServiceResult } from '@/types/ServiceResult'
import type { ApiHttpError } from '@/api/ApiHttpError'
import type { PageRequest, PageResponse, Sort } from '@/types/Pages'
import type { StudentFilter, StudentResponse, StudentSuggestionResponse } from '@/types/Student'

function buildStudentsQuery(filter: StudentFilter, page: PageRequest, sort?: Sort): string {
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

export async function getStudents(filter: StudentFilter, page: PageRequest, sort?: Sort): Promise<ServiceResult<PageResponse<StudentResponse>, ApiHttpError>> {
  try {
    const query = buildStudentsQuery(filter, page, sort)

    const data = await http<PageResponse<StudentResponse>>(`/students${query}`, {
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

export async function removeStudent(studentId: string): Promise<ServiceResult<void, ApiHttpError>> {
  try {
    const data = await http<void>(`/parents/${studentId}`, {
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

export async function autocompleteStudents(query: string): Promise<ServiceResult<StudentSuggestionResponse[], ApiHttpError>> {
  try {
    const data = await http<StudentSuggestionResponse[]>(`/students/autocomplete?query=${encodeURIComponent(query)}`, {
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