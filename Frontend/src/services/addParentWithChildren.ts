import { http } from '@/api/http'
import { mapApiError } from '@/utils/apiErrorMapper'
import type { ParentWithChildrenRequest } from '@/types/ParentWithChildren'
import type { ParentResponse } from '@/types/Parent'
import type { ServiceResult } from '@/types/ServiceResult'
import type { ApiHttpError } from '@/api/ApiHttpError'

export async function addParentWithChildren(request: ParentWithChildrenRequest): Promise<ServiceResult<ParentResponse, ApiHttpError>> {
  try {
    const data = await http<ParentResponse>('/parents/with-children', {
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