<template>
  <Header />
  <div class="min-h-screen bg-slate-50 py-8 px-4 sm:px-6 lg:px-8">
    <main class="max-w-7xl mx-auto">
      <!-- Header con título y selector de vista -->
      <div class="mb-8 animate-fade-in-down">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between">
          <div class="flex items-center space-x-3 mb-6 sm:mb-0">
            <div class="w-12 h-12 bg-slate-800 rounded-xl flex items-center justify-center shadow-lg">
              <ClipboardCheck class="w-6 h-6 text-white" />
            </div>
            <div>
              <h1 class="text-2xl font-bold text-slate-800">Justificaciones</h1>
              <p class="text-sm text-slate-500">Gestiona y revisa las justificaciones de los estudiantes</p>
            </div>
          </div>
          
          <!-- Selector de vista moderno -->
          <div class="flex flex-col sm:flex-row items-stretch sm:items-center gap-3">
            <div class="bg-white p-1 rounded-xl border border-slate-200 shadow-sm inline-flex">
              <button @click="monthView = false"
                      class="px-6 py-2.5 rounded-lg text-sm font-medium transition-all duration-300 relative"
                      :class="[!monthView ? 'bg-slate-800 text-white shadow-md' : 'text-slate-600 hover:text-slate-800 hover:bg-slate-50']">
                <div class="flex items-center space-x-2">
                  <CalendarDays :class="[!monthView ? 'text-white' : 'text-slate-400']" class="w-4 h-4" />
                  <span>Aceptadas</span>
                </div>
              </button>
              
              <button @click="monthView = true"
                      class="px-6 py-2.5 rounded-lg text-sm font-medium transition-all duration-300 relative"
                      :class="[monthView ? 'bg-slate-800 text-white shadow-md' : 'text-slate-600 hover:text-slate-800 hover:bg-slate-50']">
                <div class="flex items-center space-x-2">
                  <Clock class="w-4 h-4" />
                  <span>Pendientes</span>
                </div>
              </button>
            </div>
          </div>
        </div>

        <!-- Información adicional contextual -->
        <div class="mt-4 flex flex-wrap items-center gap-4 text-sm">
          <div class="flex items-center space-x-2 text-slate-500">
            <Info class="w-4 h-4" />
            <span>{{ monthView ? 'Justificaciones pendientes de revisión' : 'Justificaciones ya aceptadas' }}</span>
          </div>
          
          <div class="flex items-center space-x-2 text-slate-500 border-l border-slate-200 pl-4">
            <Clock class="w-4 h-4" />
            <span>{{ currentDate }}</span>
          </div>
        </div>
      </div>

      <!-- Contenedor de la vista con animación de transición -->
      <transition name="view-transition" mode="out-in">
        <div :key="monthView ? 'pending' : 'accepted'" class="animate-fade-in-up">
          <AceptedJustifications v-if="!monthView"/>
          <PendingJustifications v-else/>
        </div>
      </transition>
    </main>

    <!-- Modal de éxito -->
    <transition name="fade">
      <div v-if="successModal" 
           class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4 z-50"
           @click.self="closeSuccessModal">
        <div class="bg-white rounded-2xl max-w-md w-full p-6 animate-slide-up">
          <div class="flex items-center justify-between mb-4">
            <div class="flex items-center space-x-3">
              <div class="w-10 h-10 bg-emerald-100 rounded-lg flex items-center justify-center">
                <Check class="w-5 h-5 text-emerald-600" />
              </div>
              <h3 class="text-lg font-semibold text-slate-800">¡Éxito!</h3>
            </div>
            <button @click="closeSuccessModal" 
                    class="p-2 hover:bg-slate-100 rounded-lg transition-colors">
              <X class="w-5 h-5 text-slate-500" />
            </button>
          </div>
          
          <p class="text-sm text-slate-600 mb-6">
            {{ successMessage }}
          </p>
          
          <div class="flex justify-end">
            <button @click="closeSuccessModal"
                    class="px-4 py-2 bg-slate-800 text-white text-sm rounded-lg hover:bg-slate-700 transition-colors">
              Cerrar
            </button>
          </div>
        </div>
      </div>
    </transition>

    <!-- Modal de error -->
    <transition name="fade">
      <div v-if="errorModal" 
           class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4 z-50"
           @click.self="closeErrorModal">
        <div class="bg-white rounded-2xl max-w-md w-full p-6 animate-slide-up">
          <div class="flex items-center justify-between mb-4">
            <div class="flex items-center space-x-3">
              <div class="w-10 h-10 bg-red-100 rounded-lg flex items-center justify-center">
                <AlertCircle class="w-5 h-5 text-red-600" />
              </div>
              <h3 class="text-lg font-semibold text-slate-800">Error</h3>
            </div>
            <button @click="closeErrorModal" 
                    class="p-2 hover:bg-slate-100 rounded-lg transition-colors">
              <X class="w-5 h-5 text-slate-500" />
            </button>
          </div>
          
          <p class="text-sm text-slate-600 mb-6">
            {{ errorMessageText }}
          </p>
          
          <div class="flex justify-end">
            <button @click="closeErrorModal"
                    class="px-4 py-2 bg-slate-800 text-white text-sm rounded-lg hover:bg-slate-700 transition-colors">
              Cerrar
            </button>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import PendingJustifications from '@/components/PendingJustifications.vue'
import AceptedJustifications from '@/components/AceptedJustifications.vue'
import Header from '@/components/Header.vue'
import {
  ClipboardCheck,
  CalendarDays,
  Info,
  Clock,
  Check,
  X,
  AlertCircle
} from 'lucide-vue-next'

// Estado
const monthView = ref(true) // true = pendientes, false = aceptadas
const errorModal = ref(false)
const successModal = ref(false)
const errorMessageText = ref('')
const successMessage = ref('')

// Fecha actual formateada
const currentDate = computed(() => {
  const date = new Date()
  return date.toLocaleDateString('es-ES', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
})

const closeSuccessModal = () => {
  successModal.value = false
  successMessage.value = ''
}

const closeErrorModal = () => {
  errorModal.value = false
  errorMessageText.value = ''
}
</script>

<style scoped>
/* Animación de transición entre vistas */
.view-transition-enter-active,
.view-transition-leave-active {
  transition: all 0.3s ease;
}

.view-transition-enter-from {
  opacity: 0;
  transform: translateX(20px);
}

.view-transition-leave-to {
  opacity: 0;
  transform: translateX(-20px);
}

/* Animación de entrada del header */
@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInUp {
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
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fade-in-down {
  animation: fadeInDown 0.6s ease-out;
}

.animate-fade-in-up {
  animation: fadeInUp 0.5s ease-out;
}

.animate-slide-up {
  animation: slideUp 0.3s ease-out;
}

/* Transiciones suaves para todos los elementos */
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