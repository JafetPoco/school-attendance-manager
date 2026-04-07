import { useToastStore, type Toast } from '@/stores/toastStore'

export const useToast = () => {
  const toastStore = useToastStore()

  return {
    showSuccess: (title: string, message: string, duration?: number) => {
      toastStore.success(title, message, duration)
    },
    showError: (title: string, message: string, duration?: number) => {
      toastStore.error(title, message, duration)
    },
    showInfo: (title: string, message: string, duration?: number) => {
      toastStore.info(title, message, duration)
    },
    showWarning: (title: string, message: string, duration?: number) => {
      toastStore.warning(title, message, duration)
    },
    showToast: (type: Toast['type'], title: string, message: string, duration?: number) => {
      toastStore.addToast({ type, title, message, duration })
    }
  }
}