<template>
  <div class="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden">

    <!-- Filtros -->
    <div class="p-6 border-b border-slate-200">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4 items-stretch">
        <!-- Búsqueda por nombre -->
        <div class="relative min-w-0">
          <Search class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
          <input type="text"
                 v-model="filter.name"
                 placeholder="Buscar por nombre o apellido..."
                 :disabled="loading"
                 class="w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent transition-all">
        </div>

        <!-- Filtro por sección -->
        <div class="relative min-w-0">
          <Layers class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
          <select v-model="filter.section"
                  :disabled="loading"
                  class="w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent appearance-none cursor-pointer disabled:opacity-50">
            <option value="">Todas las secciones</option>
            <option value="A">Sección A</option>
            <option value="B">Sección B</option>
            <option value="C">Sección C</option>
          </select>
        </div>

        <!-- Filtro por tipo de asistencia -->
        <div class="relative min-w-0">
          <Filter class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
          <select v-model="filter.attendanceType"
                  :disabled="loading"
                  class="w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent appearance-none cursor-pointer disabled:opacity-50">
            <option value="">Todos los tipos</option>
            <option value="presente">Presente</option>
            <option value="tarde">Tarde</option>
            <option value="ausente">Ausente</option>
            <option value="justificado">Justificado</option>
          </select>
        </div>

        <!-- Filtro Fecha -->
        <div class="relative min-w-0">
          <Calendar class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
          <input type="date" 
                 v-model="filter.date"
                 :disabled="loading"
               class="w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent">
        </div>
      </div>

      <!-- Filtros activos -->
      <div v-if="hasActiveFilters" class="mt-4 flex items-center space-x-2">
        <span class="text-xs text-slate-500">Filtros activos:</span>
        <div class="flex flex-wrap gap-2">
          <span v-if="filter.name" 
                class="inline-flex items-center space-x-1 px-2 py-1 bg-slate-100 text-slate-700 text-xs rounded-full">
            <Search class="w-3 h-3" />
            <span>"{{ filter.name }}"</span>
            <button @click="filter.name = ''" class="hover:text-slate-900" :disabled="loading">
              <X class="w-3 h-3" />
            </button>
          </span>
          <span v-if="filter.section" 
                class="inline-flex items-center space-x-1 px-2 py-1 bg-slate-100 text-slate-700 text-xs rounded-full">
            <Layers class="w-3 h-3" />
            <span>Sección {{ filter.section }}</span>
            <button @click="filter.section = ''" class="hover:text-slate-900" :disabled="loading">
              <X class="w-3 h-3" />
            </button>
          </span>
          <span v-if="filter.attendanceType" 
                class="inline-flex items-center space-x-1 px-2 py-1 bg-slate-100 text-slate-700 text-xs rounded-full">
            <Filter class="w-3 h-3" />
            <span>{{ getAttendanceTypeLabel(filter.attendanceType) }}</span>
            <button @click="filter.attendanceType = ''" class="hover:text-slate-900" :disabled="loading">
              <X class="w-3 h-3" />
            </button>
          </span>
          <button @click="clearAllFilters" 
                  class="text-xs text-slate-500 hover:text-slate-700 underline"
                  :disabled="loading">
            Limpiar todo
          </button>
        </div>
      </div>
    </div>

    <!-- Estados de carga y error -->
    <transition name="fade">
      <div v-if="loading" class="flex flex-col items-center justify-center py-12">
        <Loader2 class="w-8 h-8 text-slate-400 animate-spin mb-3" />
        <p class="text-sm text-slate-500">Cargando asistencias...</p>
      </div>
    </transition>

    <transition name="fade">
      <div v-if="errorMessage" 
           class="mx-6 my-4 bg-red-50 border border-red-200 rounded-xl p-4 flex items-start space-x-3">
        <AlertCircle class="w-5 h-5 text-red-500 flex-shrink-0 mt-0.5" />
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
        <p class="text-sm text-slate-500 mb-6">
          {{ hasActiveFilters ? 'No se encontraron asistencias con los filtros aplicados' : 'No hay asistencias registradas para esta fecha' }}
        </p>
        <button v-if="hasActiveFilters" 
                @click="clearAllFilters"
                class="inline-flex items-center space-x-2 text-slate-600 hover:text-slate-800">
          <X class="w-4 h-4" />
          <span>Limpiar filtros</span>
        </button>
      </div>
    </transition>

    <!-- Tabla de asistencias -->
    <transition name="fade">
      <div v-if="!loading && !errorMessage && attendances.length > 0">
        <div class="overflow-x-auto">
          <table class="w-full">
            <thead>
              <tr class="bg-slate-50 border-b border-slate-200">
                <th v-for="column in columns" :key="column.key"
                    class="px-6 py-4 text-left text-xs font-semibold text-slate-600 uppercase tracking-wider">
                  <button @click="toggleSort(column.key)"
                          class="flex items-center space-x-1 hover:text-slate-800 group"
                          :disabled="loading">
                    <span>{{ column.label }}</span>
                    <span class="text-slate-400 group-hover:text-slate-600">
                      <MoveVerticalIcon v-if="sort.field !== column.key" class="w-3 h-3" />
                      <MoveUpIcon v-else-if="sort.field === column.key && sort.direction === 'asc'" class="w-3 h-3" />
                      <MoveDownIcon v-else-if="sort.field === column.key && sort.direction === 'desc'" class="w-3 h-3" />
                    </span>
                  </button>
                </th>
                <th class="px-6 py-4 text-left text-xs font-semibold text-slate-600 uppercase tracking-wider">
                  Tipo de Asistencia
                </th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-200">
              <tr v-for="attendance in attendances" 
                  :key="attendance.dni"
                  class="hover:bg-slate-50 transition-colors duration-200">
                <td class="px-6 py-4 whitespace-nowrap">
                  <span class="text-sm font-mono text-slate-600">{{ attendance.dni }}</span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center">
                    <span class="text-sm font-medium text-slate-800">{{ attendance.studentName }}</span>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-600">{{ attendance.studentFirstLastName }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-600">{{ attendance.studentSecondLastName }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span :class="getAttendanceBadgeClass(attendance.attendanceType)" 
                        class="px-3 py-1 text-xs rounded-full font-medium">
                    {{ getAttendanceTypeLabel(attendance.attendanceType) }}
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Footer con resumen y paginación -->
        <div class="bg-slate-50 px-6 py-4 border-t border-slate-200">
          <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between">
            <!-- Paginación compacta -->
            <div class="flex items-center justify-between sm:justify-end space-x-4">
              <div class="text-xs text-slate-500">
                {{ paginationStart }}-{{ paginationEnd }} de {{ totalElements }}
              </div>
              
              <div class="flex space-x-1">
                <button @click="goToFirstPage"
                        :disabled="currentPage === 0 || loading"
                        class="p-1.5 border border-slate-200 rounded-lg text-slate-600 hover:bg-white disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                        title="Primera página">
                  <ChevronsLeft class="w-4 h-4" />
                </button>
                <button @click="prevPage"
                        :disabled="currentPage === 0 || loading"
                        class="p-1.5 border border-slate-200 rounded-lg text-slate-600 hover:bg-white disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                        title="Página anterior">
                  <ChevronLeft class="w-4 h-4" />
                </button>
                <span class="px-3 py-1.5 text-sm text-slate-600">
                  {{ currentPage + 1 }}/{{ totalPages }}
                </span>
                <button @click="nextPage"
                        :disabled="currentPage >= totalPages - 1 || loading"
                        class="p-1.5 border border-slate-200 rounded-lg text-slate-600 hover:bg-white disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                        title="Página siguiente">
                  <ChevronRight class="w-4 h-4" />
                </button>
                <button @click="goToLastPage"
                        :disabled="currentPage >= totalPages - 1 || loading"
                        class="p-1.5 border border-slate-200 rounded-lg text-slate-600 hover:bg-white disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                        title="Última página">
                  <ChevronsRight class="w-4 h-4" />
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'
import { getAttendances } from '@/services/attendancesService'
import type { AttendanceFilter, AttendanceResponse } from '@/types/Attendance'
import type { PageRequest, Sort } from '@/types/Pages'
import {
  Calendar,
  ChevronLeft,
  ChevronRight,
  Search,
  Layers,
  Filter,
  Loader2,
  AlertCircle,
  RotateCw,
  X,
  ChevronsLeft,
  ChevronsRight,
  ClipboardList,
  MoveVerticalIcon,
  MoveUpIcon,
  MoveDownIcon
} from 'lucide-vue-next'

// Constantes
const PAGE_SIZE = 10

// Definición de columnas
const columns = [
  { key: 'studentDni', label: 'DNI' },
  { key: 'studentName', label: 'Nombre' },
  { key: 'studentFirstLastName', label: 'Apellido Paterno' },
  { key: 'studentSecondLastName', label: 'Apellido Materno' }
] as const

// Helper para obtener fecha actual
const getTodayDate = () => {
  const today = new Date()
  const timezoneOffset = today.getTimezoneOffset() * 60000
  return new Date(today.getTime() - timezoneOffset).toISOString().split('T')[0]
}

// Estado
const totalPages = ref(0)
const totalElements = ref(0)
const currentPage = ref(0)
const loading = ref(false)
const errorMessage = ref<string>('')
const attendances = ref<AttendanceResponse[]>([])

const filter = ref<AttendanceFilter>({
  date: getTodayDate(),
  name: '',
  section: '',
  attendanceType: ''
})

const sort = ref<Sort>({
  field: '',
  direction: 'asc'
})

// Computed properties
const hasActiveFilters = computed(() => {
  return filter.value.name !== '' || 
         filter.value.section !== '' || 
         filter.value.attendanceType !== ''
})

const paginationStart = computed(() => {
  if (attendances.value.length === 0) return 0
  return currentPage.value * PAGE_SIZE + 1
})

const paginationEnd = computed(() => {
  if (attendances.value.length === 0) return 0
  return Math.min((currentPage.value + 1) * PAGE_SIZE, totalElements.value)
})

const getAttendanceTypeLabel = (type: string) => {
  const labels: Record<string, string> = {
    'presente': 'Presente',
    'tarde': 'Tarde',
    'ausente': 'Ausente',
    'justificado': 'Justificado'
  }
  return labels[type] || type
}

const getAttendanceBadgeClass = (type: string) => {
  const classes: Record<string, string> = {
    'Presente': 'bg-emerald-50 text-emerald-700',
    'Tarde': 'bg-amber-50 text-amber-700',
    'Ausente': 'bg-red-50 text-red-700',
    'Justificado': 'bg-blue-50 text-blue-700'
  }
  return classes[type] || 'bg-slate-50 text-slate-700'
}

// Funciones de ordenamiento
const toggleSort = (field: string) => {
  if (sort.value.field === field) {
    sort.value.direction = sort.value.direction === 'asc' ? 'desc' : 'asc'
  } else {
    sort.value.field = field
    sort.value.direction = 'asc'
  }
  currentPage.value = 0
  loadAttendances()
}

// Funciones de filtro
const clearAllFilters = () => {
  filter.value.name = ''
  filter.value.section = ''
  filter.value.attendanceType = ''
}

// Funciones de paginación
const goToFirstPage = () => {
  if (currentPage.value > 0) {
    currentPage.value = 0
    loadAttendances()
  }
}

const goToLastPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value = totalPages.value - 1
    loadAttendances()
  }
}

const prevPage = () => {
  if (currentPage.value > 0) {
    currentPage.value -= 1
    loadAttendances()
  }
}

const nextPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value += 1
    loadAttendances()
  }
}

// Carga de datos
const buildPayloadPage = (): PageRequest => ({
  page: currentPage.value,
  size: PAGE_SIZE
})

const loadAttendances = async () => {
  loading.value = true
  errorMessage.value = ''
  
  try {
    const response = await getAttendances(filter.value, buildPayloadPage(), sort.value)

    if (response.success) {
      attendances.value = response.data.content
      totalPages.value = response.data.totalPages
      totalElements.value = response.data.totalElements
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
let debounceTimer: ReturnType<typeof setTimeout>
watch(
  filter,
  () => {
    clearTimeout(debounceTimer)
    debounceTimer = setTimeout(() => {
      currentPage.value = 0
      loadAttendances()
    }, 300)
  },
  { deep: true }
)

// Lifecycle
onMounted(loadAttendances)
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