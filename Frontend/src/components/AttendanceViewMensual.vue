<template>
  <div class="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden">
    <!-- Header con navegación mensual -->
    <div class="bg-slate-50 px-6 py-4 border-b border-slate-200">
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between">
        <div class="flex items-center space-x-4 mb-4 sm:mb-0">
          <div class="flex items-center space-x-2">
            <CalendarRange class="w-5 h-5 text-slate-600" />
            <span class="text-lg font-semibold text-slate-800 capitalize">{{ month }}</span>
          </div>
          <div class="flex items-center space-x-2">
            <button @click="prevMonth" 
                    class="p-1.5 hover:bg-white rounded-lg transition-colors"
                    :disabled="loading">
              <ChevronLeft class="w-4 h-4 text-slate-600" />
            </button>
            <button @click="nextMonth"
                    class="p-1.5 hover:bg-white rounded-lg transition-colors"
                    :disabled="loading">
              <ChevronRight class="w-4 h-4 text-slate-600" />
            </button>
            <button @click="currentMonth" 
                    class="px-3 py-1.5 text-xs bg-white text-slate-600 rounded-lg hover:bg-slate-50 border border-slate-200 transition-colors"
                    :disabled="loading">
              Mes Actual
            </button>
          </div>
        </div>

        <!-- Selector de sección -->
        <div class="relative">
          <Layers class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
          <select v-model="filter.classId"
                  :disabled="loading"
                  class="pl-10 pr-8 py-2 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent appearance-none cursor-pointer disabled:opacity-50 min-w-40">
            <option v-for="section in sections.sections" :key="section.id" :value="section.id">
              {{ section.name }}
            </option>
          </select>
        </div>
      </div>

      <!-- Información del mes -->
      <div class="mt-4 flex justify-between space-x-4 text-sm">
        <div class="flex items-center space-x-2 text-slate-500">
          <Users class="w-4 h-4" />
          <span>{{ attendances.length }} estudiantes</span>
        </div>
        <button @click="refreshTable"
                  :disabled="loading"
                  class="inline-flex items-center space-x-2 px-3 py-1 text-slate-600 hover:bg-slate-100 rounded-lg transition-all duration-300"
                  title="Recargar tabla">
          <RotateCw class="w-4 h-4" :class="{ 'animate-spin': loading }" />
          <span>Recargar</span>
        </button>
      </div>
    </div>

    <!-- Estados de carga y error -->
    <transition name="fade">
      <div v-if="loading" class="flex flex-col items-center justify-center py-12">
        <Loader2 class="w-8 h-8 text-slate-400 animate-spin mb-3" />
        <p class="text-sm text-slate-500">Cargando asistencias mensuales...</p>
      </div>
    </transition>

    <transition name="fade">
      <div v-if="errorMessage" 
           class="mx-6 my-4 bg-red-50 border border-red-200 rounded-xl p-4 flex items-start space-x-3">
        <AlertCircle class="w-5 h-5 text-red-500 shrink-0 mt-0.5" />
        <div class="flex-1">
          <h3 class="text-sm font-medium text-red-800">Error al cargar los datos</h3>
          <p class="text-xs text-red-600 mt-1">{{ errorMessage }}</p>
        </div>
        <button @click="loadAttendances" class="text-red-400 hover:text-red-600">
          <RotateCw class="w-4 h-4" />
        </button>
      </div>
    </transition>

    <!-- Empty State -->
    <transition name="fade">
      <div v-if="!loading && !errorMessage && attendances.length === 0" 
           class="flex flex-col items-center justify-center py-16">
        <ClipboardList class="w-16 h-16 text-slate-300 mb-4" />
        <h3 class="text-lg font-semibold text-slate-800 mb-2">No hay registros</h3>
        <p class="text-sm text-slate-500">No se encontraron asistencias para {{ month }}</p>
      </div>
    </transition>

    <!-- Tabla de asistencias mensuales -->
    <transition name="fade">
      <div v-if="!loading && !errorMessage && attendances.length > 0">
        <div class="overflow-x-auto">
          <table class="w-full min-w-200">
            <thead>
              <tr class="bg-slate-50 border-b border-slate-200">
                <th class="px-4 py-3 text-left text-xs font-semibold text-slate-600 uppercase tracking-wider sticky left-0 bg-slate-50 z-10">
                  DNI
                </th>
                <th class="px-4 py-3 text-left text-xs font-semibold text-slate-600 uppercase tracking-wider sticky left-20 bg-slate-50 z-10">
                  Estudiante
                </th>
                <th v-for="day in daysArray" :key="day"
                    class="px-2 py-3 text-center text-xs font-semibold text-slate-600 uppercase tracking-wider min-w-10">
                  <div class="flex flex-col">
                    <span>{{ day }}</span>
                    <span class="text-[10px] text-slate-400">{{ getDayOfWeek(day) }}</span>
                  </div>
                </th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-200">
              <tr v-for="attendance in attendances" 
                  :key="attendance.dni"
                  class="hover:bg-slate-50 transition-colors duration-200">
                <!-- Columnas fijas -->
                <td class="px-4 py-3 whitespace-nowrap text-sm font-mono text-slate-600 sticky left-0 bg-white hover:bg-slate-50 z-10">
                  {{ attendance.dni }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap sticky left-20 bg-white hover:bg-slate-50 z-10">
                  <div class="flex items-center">
                    <div>
                      <p class="text-sm font-medium text-slate-800">{{ attendance.name }}</p>
                      <p class="text-xs text-slate-500">{{ attendance.lastName }}</p>
                    </div>
                  </div>
                </td>
                
                <!-- Días del mes -->
                <td v-for="day in daysArray" :key="day"
                    class="px-2 py-3 text-center">
                  <span :class="getAttendanceDayClass(attendance.dailyAttendance[day])">
                    {{ getAttendanceSymbol(attendance.dailyAttendance[day]) }}
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </transition>

    <!-- Leyenda -->
    <div class="my-4 ml-4 flex items-center text-sm text-slate-500">
          <span class="inline-flex items-center space-x-1 ml-4">
            <span class="inline-flex items-center justify-center w-5 h-5 bg-emerald-100 text-emerald-600 font-bold text-xs rounded">P</span>
            <span class="text-xs text-slate-500">Presente</span>
          </span>
          <span class="inline-flex items-center space-x-1 ml-4">
            <span class="inline-flex items-center justify-center w-5 h-5 bg-amber-100 text-amber-600 font-bold text-xs rounded">T</span>
            <span class="text-xs text-slate-500">Tarde</span>
          </span>
          <span class="inline-flex items-center space-x-1 ml-4">
            <span class="inline-flex items-center justify-center w-5 h-5 bg-red-100 text-red-600 font-bold text-xs rounded">A</span>
            <span class="text-xs text-slate-500">Ausente</span>
          </span>
          <span class="inline-flex items-center space-x-1 ml-4">
            <span class="inline-flex items-center justify-center w-5 h-5 bg-blue-100 text-blue-600 font-bold text-xs rounded">J</span>
            <span class="text-xs text-slate-500">Justificado</span>
          </span>
        </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'
import { getMonthlyAttendance } from '@/services/monthlyAttendancesService'
import type { MonthlyAttendance, MonthlyAttendanceFilter } from '@/types/MonthlyAttendance'
import {
  CalendarRange,
  ChevronLeft,
  ChevronRight,
  Layers,
  Users,
  Loader2,
  AlertCircle,
  RotateCw,
  ClipboardList
} from 'lucide-vue-next'
import { useSectionStore } from '@/stores/sectionStore'

// Estado
const loading = ref(false)
const errorMessage = ref('')
const attendances = ref<MonthlyAttendance[]>([])
const selectedDate = ref(new Date())

const sections = useSectionStore()

const filter = ref<MonthlyAttendanceFilter>({
  month: new Date().getMonth() + 1,
  classId: sections.sections?.length > 0 ? sections.sections[0].id : 0
})

// Computed properties
const daysOfMonth = computed(() => {
  const year = selectedDate.value.getFullYear()
  const month = selectedDate.value.getMonth()
  return new Date(year, month + 1, 0).getDate()
})

const daysArray = computed(() => {
  const year = selectedDate.value.getFullYear()
  const month = selectedDate.value.getMonth()

  return Array.from({ length: daysOfMonth.value }, (_, i) => i + 1).filter((day) => {
    const date = new Date(year, month, day)
    const dayOfWeek = date.getDay()
    return dayOfWeek !== 0 && dayOfWeek !== 6
  })
})

const month = computed(() =>
  selectedDate.value.toLocaleString('es-ES', { month: 'long' })
)

const monthNumber = computed(() => selectedDate.value.getMonth() + 1)


const getDayOfWeek = (day: number) => {
  const date = new Date(selectedDate.value.getFullYear(), selectedDate.value.getMonth(), day)
  return date.toLocaleDateString('es-ES', { weekday: 'short' }).slice(0, 3)
}

const getAttendanceSymbol = (type: string | null | undefined) => {
  if (!type) return '-'

  const status = type.toLowerCase()
  const symbols: Record<string, string> = {
    'presente': 'P',
    'tarde': 'T',
    'ausente': 'A',
    'justificado': 'J'
  }
  return symbols[status] || '?'
}

const getAttendanceDayClass = (type: string | null | undefined) => {
  if (!type) return 'text-slate-300'

  const status = type.toLowerCase()
  const classes: Record<string, string> = {
    'presente': 'inline-flex items-center justify-center w-7 h-7 bg-emerald-100 text-emerald-600 font-bold text-sm',
    'tarde': 'inline-flex items-center justify-center w-7 h-7 bg-amber-100 text-amber-600 font-bold text-sm',
    'ausente': 'inline-flex items-center justify-center w-7 h-7 bg-red-100 text-red-600 font-bold text-sm',
    'justificado': 'inline-flex items-center justify-center w-7 h-7 bg-blue-100 text-blue-600 font-bold text-sm'
  }
  return classes[status] || 'text-slate-400'
}

// Navegación de meses
const nextMonth = () => {
  const next = new Date(selectedDate.value)
  next.setMonth(next.getMonth() + 1)
  selectedDate.value = next
}

const prevMonth = () => {
  const prev = new Date(selectedDate.value)
  prev.setMonth(prev.getMonth() - 1)
  selectedDate.value = prev
}

const currentMonth = () => {
  selectedDate.value = new Date()
}

// Carga de datos
const loadAttendances = async () => {
  loading.value = true
  errorMessage.value = ''
  
  try {
    const response = await getMonthlyAttendance(filter.value)

    if (response.success) {
      attendances.value = response.data
    } else {
      errorMessage.value = response.error.message
    }
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Error de conexión'
  } finally {
    loading.value = false
  }
}

// Watchers
watch(monthNumber, (newMonth) => {
  filter.value.month = newMonth
  loadAttendances()
})

watch(() => filter.value.classId, () => {
  loadAttendances()
})

// Lifecycle
onMounted(() => {
  filter.value.month = monthNumber.value
  loadAttendances()
})

const refreshTable = () => {
  loadAttendances()
}
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

/* Sticky columns */
.sticky {
  position: sticky;
  background-color: white;
  transition: background-color 0.2s ease;
}

tr:hover .sticky {
  background-color: #f8fafc;
}

/* Estilos para scroll horizontal suave */
.overflow-x-auto {
  scrollbar-width: thin;
  scrollbar-color: #94a3b8 #f1f5f9;
}

.overflow-x-auto::-webkit-scrollbar {
  height: 8px;
}

.overflow-x-auto::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 4px;
}

.overflow-x-auto::-webkit-scrollbar-thumb {
  background: #94a3b8;
  border-radius: 4px;
}

.overflow-x-auto::-webkit-scrollbar-thumb:hover {
  background: #64748b;
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