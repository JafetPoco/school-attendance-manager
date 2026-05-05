import type { ApiHttpError } from "@/api/ApiHttpError"
import { http } from "@/api/http"
import type { ClassFullInfoResponse, ClassRequest, ClassResponse } from "@/types/Class"
import type { ServiceResult } from "@/types/ServiceResult"
import { mapApiError } from "@/utils/apiErrorMapper"

export async function createClass(request: ClassRequest): Promise<ServiceResult<ClassResponse, ApiHttpError>> {
  try {
    const data = await http<ClassResponse>(`/classes`, {
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

export async function getClasses(): Promise<ServiceResult<ClassResponse[], ApiHttpError>> {
  try {
    const data = await http<ClassResponse[]>(`/classes/public`, {
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

export async function getClassesFullInfo(): Promise<ServiceResult<ClassFullInfoResponse[], ApiHttpError>> {
  try {
    const data = await http<ClassFullInfoResponse[]>(`/classes`, {
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

export async function editClass(classId: number, request: ClassRequest): Promise<ServiceResult<ClassFullInfoResponse, ApiHttpError>> {
  try {
    const data = await http<ClassFullInfoResponse>(`/classes/${classId}`, {
      method: 'PATCH',
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