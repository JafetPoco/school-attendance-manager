import { http } from '@/api/http'
import type { AuthUser } from '@/types/AuthUser'

export async function getCurrentUser(): Promise<AuthUser> {
  return http<AuthUser>('/user/me')
}

export async function logout(): Promise<void> {
  await http<void>('/logout', {
    method: 'POST'
  })
}
