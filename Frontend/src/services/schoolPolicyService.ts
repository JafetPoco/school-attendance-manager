import { http } from '@/api/http'
import { mapApiError } from '@/utils/apiErrorMapper'

import type { ServiceResult } from '@/types/ServiceResult'
import type { ApiHttpError } from '@/api/ApiHttpError'
import type { SchoolPolicyRequest, SchoolPolicyResponse } from '@/types/SchoolPolicy'

export async function getSchoolPolicy(): Promise<ServiceResult<SchoolPolicyResponse, ApiHttpError>> {
  try {
    const data = await http<SchoolPolicyResponse>(`/policies`, {
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

export async function addSchoolPolicies(request: SchoolPolicyRequest): Promise<ServiceResult<SchoolPolicyResponse, ApiHttpError>> {
  try {
    const data = await http<SchoolPolicyResponse>(`/policies`, {
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

export async function updateSchoolPolicies(request: SchoolPolicyRequest): Promise<ServiceResult<SchoolPolicyResponse, ApiHttpError>> {
    try {
        const data = await http<SchoolPolicyResponse>(`/policies`, {
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