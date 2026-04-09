<template>
  <Header></Header>
  <div class="min-h-screen bg-slate-50 py-8 px-4 sm:px-6 lg:px-8">
    <div class="max-w-7xl mx-auto">
      <!-- Header con título y selector de vista -->
      <div class="mb-8 animate-fade-in-down">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between">
          <div class="flex items-center space-x-3 mb-6 sm:mb-0">
            <div class="w-12 h-12 bg-slate-800 rounded-xl flex items-center justify-center shadow-lg">
              <ClipboardCheck class="w-6 h-6 text-white" />
            </div>
            <div>
              <h1 class="text-2xl font-bold text-slate-800">Asistencias</h1>
              <p class="text-sm text-slate-500">Gestiona el registro de asistencia de estudiantes</p>
            </div>
          </div>
          
          <!-- Selector de vista moderno -->
          <div class="flex flex-col sm:flex-row items-stretch sm:items-center gap-3">
            <button class="inline-flex items-center justify-center px-4 py-2.5 bg-amber-50 text-amber-700 rounded-xl hover:bg-amber-100 transition-all duration-300 border border-amber-200 shadow-sm group"
                    @click="closeAttendance">
              <CalendarClock class="w-4 h-4 mr-2 group-hover:scale-110 transition-transform" />
              <span class="text-sm font-medium">Cerrar registro</span>
            </button>
            <div class="bg-white p-1 rounded-xl border border-slate-200 shadow-sm inline-flex">
              <button @click="monthView = false"
                      class="px-6 py-2.5 rounded-lg text-sm font-medium transition-all duration-300 relative"
                      :class="[!monthView ? 'bg-slate-800 text-white shadow-md' : 'text-slate-600 hover:text-slate-800 hover:bg-slate-50']">
                <div class="flex items-center space-x-2">
                  <CalendarDays :class="[!monthView ? 'text-white' : 'text-slate-400']" class="w-4 h-4" />
                  <span>Vista por Día</span>
                </div>
              </button>
              
              <button @click="monthView = true"
                      class="px-6 py-2.5 rounded-lg text-sm font-medium transition-all duration-300 relative"
                      :class="[monthView ? 'bg-slate-800 text-white shadow-md' : 'text-slate-600 hover:text-slate-800 hover:bg-slate-50']">
                <div class="flex items-center space-x-2">
                  <CalendarRange :class="[monthView ? 'text-white' : 'text-slate-400']" class="w-4 h-4" />
                  <span>Vista Mensual</span>
                </div>
              </button>
            </div>
          </div>
        </div>

        <!-- Información adicional contextual -->
        <div class="mt-4 flex items-center space-x-4 text-sm">
          <div class="flex items-center space-x-2 text-slate-500">
            <Info class="w-4 h-4" />
            <span>{{ monthView ? 'Resumen mensual de asistencia' : 'Registro diario de asistencia por curso' }}</span>
          </div>
          
          <!-- Fecha actual -->
          <div class="flex items-center space-x-2 text-slate-500 border-l border-slate-200 pl-4">
            <Clock class="w-4 h-4" />
            <span>{{ currentDate }}</span>
          </div>
        </div>
      </div>

      <!-- Contenedor de la vista con animación de transición -->
      <transition name="view-transition" mode="out-in">
        <div :key="monthView ? 'month' : 'day'" class="animate-fade-in-up">
          <AttendanceViewMensual v-if="monthView" />
          <AttendanceViewDay v-else/>
        </div>
      </transition>

      <!-- Footer con acciones rápidas (opcional) -->
      <div class="mt-6 flex justify-end space-x-3">
        <button class="flex items-center space-x-2 px-4 py-2 text-sm text-slate-600 hover:text-slate-800 hover:bg-white rounded-lg transition-all duration-300">
          <Download class="w-4 h-4" />
          <span>Exportar reporte</span>
        </button>
        <button class="flex items-center space-x-2 px-4 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 transition-all duration-300 transform hover:scale-105 shadow-md">
          <Printer class="w-4 h-4" />
          <span>Imprimir</span>
        </button>
      </div>
    </div>
  </div>

<!-- Modal de ección realizada -->
    <transition name="fade">
      <div v-if="success" 
           class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4 z-50"
           @click.self="success = false">
        <div class="bg-white rounded-2xl max-w-md w-full p-6 animate-slide-up">
          <div class="flex items-center justify-between mb-4">
            <div class="flex items-center space-x-3">
              <div class="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center">
                <Check class="w-5 h-5 text-green-600" />
              </div>
              <h3 class="text-lg font-semibold text-slate-800">Registro Cerrado</h3>
            </div>
            <button @click="success = false" 
                    class="p-2 hover:bg-slate-100 rounded-lg transition-colors">
              <X class="w-5 h-5 text-slate-500" />
            </button>
          </div>
          
          <p class="text-sm text-slate-600 mb-6">
            Se han registrado {{ countMissed }} alumnos que no han marcado su asistencia hoy. Puedes revisar el reporte para más detalles.
          </p>
        </div>
      </div>
    </transition>

    <!-- Modal de error -->
    <transition name="fade">
      <div v-if="errorMessage" 
           class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4 z-50"
           @click.self="errorMessage = ''">
        <div class="bg-white rounded-2xl max-w-md w-full p-6 animate-slide-up">
          <div class="flex items-center justify-between mb-4">
            <div class="flex items-center space-x-3">
              <div class="w-10 h-10 bg-red-100 rounded-lg flex items-center justify-center">
                <MessageCircleX class="w-5 h-5 text-red-600" />
              </div>
              <h3 class="text-lg font-semibold text-slate-800">Error</h3>
            </div>
            <button @click="errorMessage = ''" 
                    class="p-2 hover:bg-slate-100 rounded-lg transition-colors">
              <X class="w-5 h-5 text-slate-500" />
            </button>
          </div>
          
          <p class="text-sm text-slate-600 mb-6">
            {{ errorMessage }}
          </p>
        </div>
      </div>
    </transition>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import AttendanceViewDay from '@/components/AttendanceViewDay.vue'
import AttendanceViewMensual from '@/components/AttendanceViewMensual.vue'
import Header from '@/components/Header.vue'
import {
  ClipboardCheck,
  CalendarDays,
  CalendarRange,
  Info,
  Clock,
  Download,
  Printer,
  CalendarClock,
  Check,
  X,
  MessageCircleX
} from 'lucide-vue-next'
import { createMissedAttendance } from '@/services/attendancesService'

const monthView = ref(true)
const errorMessage = ref('')
const success = ref(false)
const countMissed = ref(0)

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

const closeAttendance = async () => {
  errorMessage.value = ''
  try {
    const response = await createMissedAttendance()

    if (response.success) {
      success.value = true
      countMissed.value = response.data.count
    } else {
      errorMessage.value = response.error.message
    }
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Error de conexión'
  }
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

.animate-fade-in-down {
  animation: fadeInDown 0.6s ease-out;
}

.animate-fade-in-up {
  animation: fadeInUp 0.5s ease-out;
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