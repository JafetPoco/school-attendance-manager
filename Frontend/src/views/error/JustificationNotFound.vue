<template>
  <div class="min-h-screen bg-linear-to-br from-gray-900 via-gray-800 to-gray-900 flex items-center justify-center p-4">
    <div class="max-w-2xl w-full animate-fade-in">
      <!-- Tarjeta de error principal -->
      <div class="bg-linear-to-br from-slate-200 to-gray-200 backdrop-blur-sm rounded-2xl border border-gray-700 shadow-2xl overflow-hidden">
        <!-- Cabecera con gradiente sutil -->
        <div class="bg-linear-to-r from-gray-700 to-gray-800 px-8 py-8 text-center border-b border-gray-700">
          <div class="inline-flex p-4 bg-red-500/10 rounded-2xl mb-4">
            <AlertOctagon class="w-14 h-14 text-red-400" />
          </div>
          <h1 class="text-3xl font-bold text-white mb-2">¡Ups! Algo salió mal</h1>
          <p class="text-gray-400 text-sm font-medium">Hemos detectado un problema al procesar tu solicitud</p>
        </div>

        <!-- Contenido del error -->
        <div class="p-8">
          <!-- Mensaje de error -->
          <div class="space-y-4">
            <div class="flex items-start space-x-3 p-5 bg-red-500/90 rounded-xl">
              <AlertCircle class="w-5 h-5 text-white shrink-0 mt-0.5" />
              <div class="flex-1">
                <h3 class="text-sm font-semibold text-white mb-2 uppercase tracking-wider">Detalle del error</h3>
                <p class="text-sm text-white leading-relaxed">{{ errorMessage }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import {
  AlertOctagon,
  AlertCircle
} from 'lucide-vue-next'

const route = useRoute()

const fallbackMessage = 'Lo sentimos, no se pudo encontrar la justificación que estás buscando. Por favor, verifica el enlace o intenta nuevamente.'

const errorMessage = computed(() => {
  const message = route.query.message
  if (typeof message === 'string' && message.trim().length > 0) {
    return message
  }
  return fallbackMessage
})
</script>

<style scoped>
/* Animaciones */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translate(-50%, 20px);
  }
  to {
    opacity: 1;
    transform: translate(-50%, 0);
  }
}

.animate-fade-in {
  animation: fadeIn 0.6s ease-out;
}

.animate-slide-up {
  animation: slideUp 0.3s ease-out;
}

/* Transiciones suaves */
* {
  transition-property: background-color, border-color, color, fill, stroke, opacity, box-shadow, transform;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 200ms;
}

/* Mejoras de accesibilidad */
@media (prefers-reduced-motion: reduce) {
  * {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
  }
}
</style>