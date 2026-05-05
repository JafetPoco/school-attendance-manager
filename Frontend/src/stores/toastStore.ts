// stores/toastStore.ts
import { defineStore } from 'pinia'
import { CheckCircle, XCircle, Info, AlertTriangle } from 'lucide-vue-next'

export interface Toast {
  id: number
  type: 'success' | 'error' | 'info' | 'warning'
  title: string
  message: string
  icon: any
  duration?: number
}

interface ToastState {
  toasts: Toast[]
  nextId: number
}

export const useToastStore = defineStore('toast', {
  state: (): ToastState => ({
    toasts: [],
    nextId: 1
  }),

  actions: {
    addToast(toast: Omit<Toast, 'id' | 'icon'>) {
      const icons = {
        success: CheckCircle,
        error: XCircle,
        info: Info,
        warning: AlertTriangle
      }

      const newToast: Toast = {
        id: this.nextId++,
        ...toast,
        icon: icons[toast.type]
      }

      this.toasts.push(newToast)

      // Auto-remove after duration
      setTimeout(() => {
        this.removeToast(newToast.id)
      }, toast.duration || 3000)
    },

    removeToast(id: number) {
      this.toasts = this.toasts.filter(t => t.id !== id)
    },

    clearAllToasts() {
      this.toasts = []
    },

    // Métodos helpers para cada tipo
    success(title: string, message: string, duration?: number) {
      this.addToast({ type: 'success', title, message, duration })
    },

    error(title: string, message: string, duration?: number) {
      this.addToast({ type: 'error', title, message, duration })
    },

    info(title: string, message: string, duration?: number) {
      this.addToast({ type: 'info', title, message, duration })
    },

    warning(title: string, message: string, duration?: number) {
      this.addToast({ type: 'warning', title, message, duration })
    }
  }
})