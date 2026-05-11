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
                    @click="confirmClose = true"
                    :disabled="loading">
              <CalendarClock class="w-4 h-4 mr-2 group-hover:scale-110 transition-transform" />
              <span class="text-sm font-medium">{{ loading ? 'Procesando...' : 'Cerrar registro' }}</span>
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
        <button class="flex items-center space-x-2 px-4 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 transition-all duration-300 transform hover:scale-105 shadow-md"
                @click="exportReport">
          <Download class="w-4 h-4" />
          <span>Exportar reporte</span>
        </button>
      </div>
    </div>
  </div>

  <!-- Modal pregunta de cerrado -->
  <transition name="fade">
    <div v-if="confirmClose" 
         class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4 z-50"
         @click.self="confirmClose = false">
      <div class="bg-white rounded-2xl max-w-md w-full p-6 animate-slide-up">
        <div class="flex items-center justify-between mb-4">
          <div class="flex items-center space-x-3">
            <div class="w-10 h-10 bg-yellow-100 rounded-lg flex items-center justify-center">
              <TriangleAlert class="w-5 h-5 text-yellow-600" />
            </div>
            <h3 class="text-lg font-semibold text-slate-800">¿Está seguro de cerrar el registro?</h3>
          </div>
          <button @click="confirmClose = false" 
                  class="p-2 hover:bg-slate-100 rounded-lg transition-colors">
            <X class="w-5 h-5 text-slate-500" />
          </button>
        </div>
        
        <p class="text-sm text-slate-600 mb-6">
          Esta acción registrará como inasistentes a todos los estudiantes que no hayan marcado su asistencia hoy. ¿Deseas continuar?
        </p>

        <!-- Botones -->
        <div class="flex items-center justify-end space-x-3 pt-4">
          <button type="button"
                  @click="confirmClose = false"
                  class="px-4 py-2 text-sm text-slate-600 hover:bg-slate-100 rounded-lg transition-colors">
            Cancelar
          </button>
          <button type="button" @click="confirmCloseAttendance"
                  class="relative px-6 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 transition-all duration-300 transform hover:scale-105 shadow-md disabled:opacity-50 disabled:cursor-not-allowed flex items-center space-x-2">
            <span>Confirmar</span>
          </button>
        </div>
        
      </div>
    </div>
  </transition>

  <!-- Modal de carga progresiva -->
  <transition name="fade">
    <div v-if="loading" 
         class="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center p-4 z-50"
         @click.self="false">
      <div class="bg-white rounded-2xl max-w-md w-full p-6 animate-slide-up">
        <div class="text-center">
          <div class="w-16 h-16 bg-slate-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <Loader2 class="w-8 h-8 animate-spin text-slate-800" />
          </div>
          
          <h3 class="text-lg font-semibold text-slate-800 mb-2">
            {{ loadingStatus.title }}
          </h3>
          
          <p class="text-sm text-slate-600 mb-4">
            {{ loadingStatus.message }}
          </p>
          
          <!-- Barra de progreso -->
          <div class="mb-4">
            <div class="flex justify-between text-sm mb-1">
              <span>Progreso</span>
              <span>{{ formattedProgress }}%</span>
            </div>
            <div class="w-full h-2 bg-slate-200 rounded-full overflow-hidden">
              <div class="h-full bg-slate-800 rounded-full transition-all duration-500"
                   :style="{ width: loadingProgress + '%' }"></div>
            </div>
          </div>
          
          <!-- Tiempo transcurrido -->
          <div class="flex items-center justify-center space-x-2 text-xs text-slate-400 mb-4">
            <Clock class="w-3 h-3" />
            <span>Tiempo transcurrido: {{ formatTime(elapsedTime) }}</span>
          </div>
          
          <!-- Advertencia si tarda demasiado -->
          <div v-if="showTimeWarning" class="mt-3 p-3 bg-amber-50 rounded-lg border border-amber-200">
            <div class="flex items-center space-x-2">
              <AlertCircle class="w-4 h-4 text-amber-600" />
              <p class="text-xs text-amber-700">
                El proceso está tomando más tiempo de lo esperado. Por favor continúa esperando.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { ref, computed, onUnmounted } from 'vue'
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
  CalendarClock,
  X,
  Loader2,
  AlertCircle,
  TriangleAlert
} from 'lucide-vue-next'
import { createMissedAttendance, getMonthlyExcel } from '@/services/attendancesService'
import { useToast } from '@/composables/useToast'

const monthView = ref(true)
const loading = ref(false)
const loadingProgress = ref(0)
const elapsedTime = ref(0)
const showTimeWarning = ref(false)
const confirmClose = ref(false)
const loadingStatus = ref({
  title: 'Cerrando registro de asistencia',
  message: 'Procesando estudiantes...'
})

//Toast
const toast = useToast()

// Timers
let progressInterval: number | null = null
let elapsedTimeInterval: number | null = null
let warningTimeout: number | null = null
let uploadStartTime = 0
let cancelOperationFlag = ref(false)

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

// Porcentaje formateado con dos decimales
const formattedProgress = computed(() => {
  return loadingProgress.value.toFixed(2)
})

const formatTime = (seconds: number) => {
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  if (mins > 0) {
    return `${mins} minuto${mins === 1 ? '' : 's'} ${secs} segundo${secs === 1 ? '' : 's'}`
  }
  return `${secs} segundo${secs === 1 ? '' : 's'}`
}

const updateProgressSimulation = () => {
  if (!loading.value) return
  
  if (loadingProgress.value < 95) {
    const increment = Math.random() * 3
    loadingProgress.value = Math.min(loadingProgress.value + increment, 95)
    
    // Actualizar mensaje según progreso
    if (loadingProgress.value < 30) {
      loadingStatus.value = {
        title: 'Cerrando registro de asistencia',
        message: 'Verificando estudiantes activos...'
      }
    } else if (loadingProgress.value < 60) {
      loadingStatus.value = {
        title: 'Procesando asistencias',
        message: 'Registrando inasistencias...'
      }
    } else if (loadingProgress.value < 90) {
      loadingStatus.value = {
        title: 'Guardando información',
        message: 'Actualizando base de datos...'
      }
    } else {
      loadingStatus.value = {
        title: 'Finalizando',
        message: 'Completando proceso...'
      }
    }
  }
}

const startTimers = () => {
  uploadStartTime = Date.now()

  progressInterval = globalThis.setInterval(updateProgressSimulation, 2000)
  
  elapsedTimeInterval = globalThis.setInterval(() => {
    if (loading.value) {
      elapsedTime.value = Math.floor((Date.now() - uploadStartTime) / 1000)
    }
  }, 1000)
  
  warningTimeout = globalThis.setTimeout(() => {
    if (loading.value) {
      showTimeWarning.value = true
    }
  }, 30000)
}

const stopTimers = () => {
  if (progressInterval) {
    clearInterval(progressInterval)
    progressInterval = null
  }
  if (elapsedTimeInterval) {
    clearInterval(elapsedTimeInterval)
    elapsedTimeInterval = null
  }
  if (warningTimeout) {
    clearTimeout(warningTimeout)
    warningTimeout = null
  }
}

const confirmCloseAttendance = async () => {
  confirmClose.value = false
  await closeAttendance()
}

const exportReport = async () => {
  try {
    const now = new Date()
    const month = now.getMonth() + 1
    const year = now.getFullYear()

    const response = await getMonthlyExcel(month, year)

    if (response.success) {
      const downloadUrl = URL.createObjectURL(response.data)
      const link = document.createElement('a')
      link.href = downloadUrl
      link.download = `reporte-asistencias-${year}-${String(month).padStart(2, '0')}.xlsx`
      document.body.appendChild(link)
      link.click()
      link.remove()
      URL.revokeObjectURL(downloadUrl)
      return
    }

    if (!response.success) {
      toast.showError('Error', response.error.message)
    }
  } catch (error) {
    toast.showError('Error', error instanceof Error ? error.message : 'Error de conexión')
  }
}

const closeAttendance = async () => {
  loading.value = true
  loadingProgress.value = 0
  elapsedTime.value = 0
  showTimeWarning.value = false
  loadingStatus.value = {
    title: 'Cerrando registro de asistencia',
    message: 'Preparando proceso...'
  }
  
  startTimers()
  
  try {
    // Llamar al servicio con timeout aumentado
    const response = await createMissedAttendance()

    // Detener timers y completar progreso
    stopTimers()
    
    if (cancelOperationFlag.value) {
      return
    }

    if (response.success) {
      loadingProgress.value = 100
      loadingStatus.value = {
        title: '¡Proceso completado!',
        message: 'Registro de asistencia cerrado exitosamente'
      }
      
      // Pequeña demora para mostrar el 100%
      setTimeout(() => {
        loading.value = false
        toast.showSuccess('Éxito', `Se han registrado ${response.data.count} alumnos que no han marcado su asistencia hoy. Puedes revisar el reporte para más detalles.`)
      }, 500)
    } else {
      loading.value = false
      toast.showError('Error', response.error.message)
    }
  } catch (error: any) {
    stopTimers()
    loading.value = false
    
    let errorMsg = error instanceof Error ? error.message : 'Error de conexión'
    
    // Manejar error de timeout específicamente
    if (error.name === 'AbortError' || error.message?.includes('timeout')) {
      errorMsg = 'El proceso está tomando demasiado tiempo. Por favor, intenta nuevamente o contacta a soporte.'
    }
    
    toast.showError('Error', errorMsg)
  }
}

// Limpiar timers al desmontar componente
onUnmounted(() => {
  stopTimers()
})
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
    transform: translateY(30px);
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

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.animate-spin {
  animation: spin 1s linear infinite;
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

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>