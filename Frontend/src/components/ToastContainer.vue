<template>
  <transition-group name="toast" tag="div" class="fixed bottom-4 right-4 z-50 space-y-2">
    <div v-for="toast in toasts" 
         :key="toast.id"
         class="flex items-center space-x-3 px-4 py-3 rounded-lg shadow-lg min-w-75 max-w-md shadow-slate-300 text-white"
         :class="styleByType(toast.type)">
      <component :is="toast.icon" class="w-5 h-5 shrink-0" />
      <div class="flex-1">
        <p class="text-sm font-medium">{{ toast.title }}</p>
        <p class="text-xs opacity-90">{{ toast.message }}</p>
      </div>
      <button @click="removeToast(toast.id)" 
              class="opacity-70 hover:opacity-100 transition-opacity">
        <X class="w-4 h-4" />
      </button>
    </div>
  </transition-group>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useToastStore } from '@/stores/toastStore'
import { X } from 'lucide-vue-next'

const toastStore = useToastStore()
const toasts = computed(() => toastStore.toasts)

const removeToast = (id: number) => {
  toastStore.removeToast(id)
}

const styleByType = (type: string) => {
  switch (type) {
    case 'success':
      return 'bg-green-600/90 border-green-600'
    case 'error':
      return 'bg-red-600/90 border-red-600'
    case 'info':
      return 'bg-blue-600/90 border-blue-600'
    case 'warning':
      return 'bg-yellow-600/90 border-yellow-600'
    default:
      return 'bg-gray-600/90 border-gray-600'
  }
}
</script>

<style scoped>
.toast-enter-active,
.toast-leave-active {
  transition: all 0.3s ease;
}

.toast-enter-from {
  opacity: 0;
  transform: translateX(100%);
}

.toast-leave-to {
  opacity: 0;
  transform: translateX(100%);
}
</style>