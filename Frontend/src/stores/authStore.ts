import { defineStore } from 'pinia'
import type { AuthUser } from '../types/AuthUser'
import { getCurrentUser, logout as logoutService } from '../services/DashBoardService'

interface AuthState {
  user: AuthUser | null
  loading: boolean
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    user: null,
    loading: false
  }),

  getters: {
    isAuthenticated: (state): boolean => !!state.user
  },

  actions: {
    async fetchUser(): Promise<void> {
      this.loading = true
      try {
        this.user = await getCurrentUser()
      } catch (error) {
        this.user = null
      } finally {
        this.loading = false
      }
    },

    async logout(): Promise<void> {
      try {
        await logoutService()
      } finally {
        this.user = null
      }
    }
  }
})
