<template>
  <Header></Header>
  <div class="min-h-screen bg-slate-50 py-8 px-4 sm:px-6 lg:px-8">
    <div class="max-w-7xl mx-auto">
      <!-- Header de la página -->
      <div class="mb-8 animate-fade-in-down">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between">
          <div class="flex items-center space-x-3 mb-4 sm:mb-0">
            <div class="w-12 h-12 bg-slate-800 rounded-xl flex items-center justify-center shadow-lg">
              <FileCheck class="w-6 h-6 text-white" />
            </div>
            <div>
              <h1 class="text-2xl font-bold text-slate-800">Justificaciones Pendientes</h1>
              <p class="text-sm text-slate-500">Revisa y evalúa las justificaciones de inasistencia</p>
            </div>
          </div>

          <!-- Estadísticas -->
          <div class="flex items-center space-x-3">
            <div class="bg-amber-50 px-4 py-2 rounded-lg border border-amber-200">
              <p class="text-xs text-amber-600">Pendientes</p>
              <p class="text-xl font-bold text-amber-700">{{ filteredJustifications.length }}</p>
            </div>
            <button @click="refreshList" 
                    class="p-2 text-slate-600 hover:bg-white rounded-lg transition-colors"
                    :disabled="loading">
              <RotateCw class="w-5 h-5" :class="{ 'animate-spin': loading }" />
            </button>
          </div>
        </div>

        <!-- Filtros y búsqueda -->
        <div class="mt-6 grid grid-cols-1 md:grid-cols-3 gap-4">
          <div class="relative">
            <Search class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
            <input type="text"
                   v-model="searchQuery"
                   placeholder="Buscar por nombre del estudiante..."
                   class="w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent">
          </div>
          <div class="relative">
            <Calendar class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
            <select v-model="dateFilter"
                    class="w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 appearance-none cursor-pointer">
              <option value="all">Todas las fechas</option>
              <option value="today">Hoy</option>
              <option value="week">Esta semana</option>
              <option value="month">Este mes</option>
            </select>
          </div>
          <div class="relative">
            <Filter class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
            <select v-model="sortBy"
                    class="w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 appearance-none cursor-pointer">
              <option value="recent">Más recientes primero</option>
              <option value="oldest">Más antiguos primero</option>
              <option value="student">Ordenar por estudiante</option>
            </select>
          </div>
        </div>
      </div>

      <!-- Estados de carga y error -->
      <transition name="fade">
        <div v-if="loading" class="flex flex-col items-center justify-center py-12 bg-white rounded-xl border border-slate-200">
          <Loader2 class="w-8 h-8 text-slate-400 animate-spin mb-3" />
          <p class="text-sm text-slate-500">Cargando justificaciones...</p>
        </div>
      </transition>

      <transition name="fade">
        <div v-if="errorMessage" 
             class="mb-6 bg-red-50 border border-red-200 rounded-xl p-4 flex items-start space-x-3">
          <AlertCircle class="w-5 h-5 text-red-500 shrink-0 mt-0.5" />
          <div class="flex-1">
            <h3 class="text-sm font-medium text-red-800">Error al cargar los datos</h3>
            <p class="text-xs text-red-600 mt-1">{{ errorMessage }}</p>
          </div>
          <button @click="refreshList" class="text-red-400 hover:text-red-600">
            <RotateCw class="w-4 h-4" />
          </button>
        </div>
      </transition>

      <!-- Grid de tarjetas -->
      <div v-if="!loading && !errorMessage" class="grid grid-cols-1 lg:grid-cols-2 gap-6 animate-fade-in-up">
        <div v-for="item in paginatedJustifications" 
             :key="String(item.id)"
             class="bg-white rounded-xl border border-slate-200 shadow-sm hover:shadow-md transition-all duration-300 overflow-hidden">
          
          <!-- Cabecera de la tarjeta -->
          <div class="bg-linear-to-r from-slate-800 to-slate-700 px-6 py-4">
            <div class="flex items-center justify-between">
              <div class="flex items-center space-x-3">
                <div class="w-10 h-10 bg-white/10 rounded-lg flex items-center justify-center">
                  <User class="w-5 h-5 text-white" />
                </div>
                <div>
                  <h3 class="text-white font-semibold">{{ item.studentName }}</h3>
                  <div class="flex items-center space-x-2 text-xs text-white/70">
                    <Calendar class="w-3 h-3" />
                    <span>Justificado el {{ formatDate(item.justificationDate) }}</span>
                  </div>
                </div>
              </div>
              <div class="flex items-center space-x-2">
                <span class="px-2 py-1 bg-amber-500/20 text-amber-200 text-xs rounded-full">
                  Pendiente
                </span>
              </div>
            </div>
          </div>

          <!-- Contenido de la tarjeta -->
          <div class="p-6 space-y-4">
            <!-- Fecha de asistencia -->
            <div class="flex items-center space-x-2 text-sm">
              <div class="w-8 h-8 bg-amber-100 rounded-lg flex items-center justify-center">
                <CalendarX class="w-4 h-4 text-amber-600" />
              </div>
              <div>
                <p class="text-xs text-slate-500">Fecha de inasistencia</p>
                <p class="font-medium text-slate-800">{{ formatDate(item.attendanceDate) }}</p>
              </div>
            </div>

            <!-- Descripción -->
            <div class="bg-slate-50 rounded-lg p-4">
              <div class="flex items-start space-x-2 mb-2">
                <FileText class="w-4 h-4 text-slate-500 shrink-0 mt-0.5" />
                <h4 class="text-sm font-medium text-slate-700">Motivo de la justificación</h4>
              </div>
              <p class="text-sm text-slate-600 leading-relaxed whitespace-pre-wrap">
                {{ expandedDescriptions[String(item.id)] ? item.description : truncateText(item.description, 200) }}
              </p>
              <button v-if="item.description.length > 200" 
                      @click="toggleDescription(item.id)"
                      class="text-xs text-indigo-600 hover:text-indigo-800 mt-2 font-medium">
                {{ expandedDescriptions[String(item.id)] ? 'Ver menos' : 'Ver más' }}
              </button>
            </div>

            <!-- Evidencia -->
            <div v-if="item.urlEvidence" class="border border-slate-200 rounded-lg p-3">
              <div class="flex items-center justify-between">
                <div class="flex items-center space-x-2">
                  <div class="w-8 h-8 bg-indigo-100 rounded-lg flex items-center justify-center">
                    <FileText class="w-4 h-4 text-indigo-600" />
                  </div>
                  <div>
                    <p class="text-xs text-slate-500">Documento adjunto</p>
                    <p class="text-sm font-medium text-slate-700">Evidencia</p>
                  </div>
                </div>
                <a :href="item.urlEvidence" 
                   target="_blank" 
                   class="inline-flex items-center space-x-1 px-3 py-1.5 text-sm text-indigo-600 hover:bg-indigo-50 rounded-lg transition-colors">
                  <Eye class="w-4 h-4" />
                  <span>Ver documento</span>
                  <ExternalLink class="w-3 h-3" />
                </a>
              </div>
            </div>

            <!-- Acciones -->
            <div class="flex items-center justify-end space-x-3 pt-4 border-t border-slate-200">
              <button @click="rejectJustification(item)"
                      class="flex items-center space-x-2 px-4 py-2 text-red-600 hover:bg-red-50 rounded-lg transition-all duration-200 group">
                <X class="w-4 h-4 group-hover:scale-110 transition-transform" />
                <span>Denegar</span>
              </button>
              <button @click="approveJustification(item)"
                      class="flex items-center space-x-2 px-4 py-2 bg-emerald-600 text-white rounded-lg hover:bg-emerald-700 transition-all duration-200 transform hover:scale-105 shadow-md group">
                <Check class="w-4 h-4 group-hover:scale-110 transition-transform" />
                <span>Aprobar</span>
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Empty state -->
      <div v-if="!loading && !errorMessage && filteredJustifications.length === 0" 
           class="bg-white rounded-xl border border-slate-200 p-12 text-center animate-fade-in-up">
        <FileCheck class="w-16 h-16 text-slate-300 mx-auto mb-4" />
        <h3 class="text-lg font-semibold text-slate-800 mb-2">No hay justificaciones pendientes</h3>
        <p class="text-sm text-slate-500">
          {{ searchQuery || dateFilter !== 'all' ? 'No se encontraron resultados con los filtros aplicados' : 'Todas las justificaciones han sido procesadas' }}
        </p>
        <button v-if="searchQuery || dateFilter !== 'all'" 
                @click="clearFilters"
                class="mt-4 text-sm text-indigo-600 hover:text-indigo-800">
          Limpiar filtros
        </button>
      </div>

      <!-- Paginación -->
      <div v-if="filteredJustifications.length > 0 && totalPages > 1" 
           class="mt-8 flex items-center justify-between">
        <div class="text-xs text-slate-500">
          Mostrando {{ paginationStart }} - {{ paginationEnd }} de {{ filteredJustifications.length }} justificaciones
        </div>
        <div class="flex space-x-2">
          <button @click="currentPage--" 
                  :disabled="currentPage === 1"
                  class="px-3 py-1 border border-slate-200 rounded-lg text-sm disabled:opacity-50 disabled:cursor-not-allowed hover:bg-white transition-colors">
            Anterior
          </button>
          <div class="flex space-x-1">
            <button v-for="page in visiblePages" 
                    :key="page"
                    @click="currentPage = page"
                    class="px-3 py-1 rounded-lg text-sm transition-colors"
                    :class="currentPage === page 
                      ? 'bg-slate-800 text-white' 
                      : 'border border-slate-200 text-slate-600 hover:bg-white'">
              {{ page }}
            </button>
          </div>
          <button @click="currentPage++"
                  :disabled="currentPage >= totalPages"
                  class="px-3 py-1 border border-slate-200 rounded-lg text-sm disabled:opacity-50 disabled:cursor-not-allowed hover:bg-white transition-colors">
            Siguiente
          </button>
        </div>
      </div>
    </div>

    <!-- Sistema de Toasts -->
    <transition-group name="toast" tag="div" class="fixed bottom-4 right-4 z-50 space-y-2">
      <div v-for="toast in toasts" 
           :key="toast.id"
           :class="[
             'flex items-center space-x-3 px-4 py-3 rounded-lg shadow-lg min-w-75 max-w-md animate-slide-in-right',
             toast.type === 'success' ? 'bg-emerald-600 text-white' : '',
             toast.type === 'error' ? 'bg-red-600 text-white' : '',
           ]">
        <component :is="toast.icon" class="w-5 h-5 shrink-0" />
        <div class="flex-1">
          <p class="text-sm font-medium">{{ toast.title }}</p>
          <p class="text-xs opacity-90">{{ toast.message }}</p>
        </div>
        <button @click="removeToast(toast.id)" class="opacity-70 hover:opacity-100 transition-opacity">
          <X class="w-4 h-4" />
        </button>
      </div>
    </transition-group>
    
  </div>
</template>

<script setup lang="ts">
import Header from '@/components/Header.vue'
import { ref, computed, onMounted } from 'vue'
import { getPendingJustifications, approveJustification as approve, rejectJustification as reject } from '@/services/justificationsService'
import type { JustificationResponse } from '@/types/Justification'
import {
  FileCheck,
  RotateCw,
  Search,
  Loader2,
  AlertCircle,
  Calendar,
  Filter,
  User,
  CalendarX,
  FileText,
  Eye,
  ExternalLink,
  Check,
  X,
  CheckCircle,
  XCircle
} from 'lucide-vue-next'

// Interfaces
interface Toast {
  id: number
  type: 'success' | 'error'
  title: string
  message: string
  icon: any
  duration?: number
}

// Estado
const loading = ref(false)
const errorMessage = ref('')
const pendingJustifications = ref<JustificationResponse[]>([])
const searchQuery = ref('')
const dateFilter = ref('all')
const sortBy = ref('recent')
const currentPage = ref(1)
const itemsPerPage = ref(6) // 6 tarjetas por página (2 columnas x 3 filas)
const expandedDescriptions = ref<Record<string, boolean>>({})
const toasts = ref<Toast[]>([])
let nextToastId = 1

// Computed
const filteredJustifications = computed(() => {
  let filtered = [...pendingJustifications.value]

  // Búsqueda por nombre
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(j => 
      j.studentName.toLowerCase().includes(query)
    )
  }

  // Filtro por fecha
  const today = new Date()
  today.setHours(0, 0, 0, 0)

  filtered = filtered.filter(j => {
    const attendanceDate = new Date(j.attendanceDate)
    attendanceDate.setHours(0, 0, 0, 0)
    const diffDays = Math.floor((today.getTime() - attendanceDate.getTime()) / (1000 * 60 * 60 * 24))

    switch (dateFilter.value) {
      case 'today':
        return diffDays === 0
      case 'week':
        return diffDays <= 7
      case 'month':
        return diffDays <= 30
      default:
        return true
    }
  })

  // Ordenamiento
  switch (sortBy.value) {
    case 'recent':
      filtered.sort((a, b) => new Date(b.justificationDate).getTime() - new Date(a.justificationDate).getTime())
      break
    case 'oldest':
      filtered.sort((a, b) => new Date(a.justificationDate).getTime() - new Date(b.justificationDate).getTime())
      break
    case 'student':
      filtered.sort((a, b) => a.studentName.localeCompare(b.studentName))
      break
  }

  return filtered
})

const totalPages = computed(() => Math.ceil(filteredJustifications.value.length / itemsPerPage.value))

const paginatedJustifications = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value
  const end = start + itemsPerPage.value
  return filteredJustifications.value.slice(start, end)
})

const paginationStart = computed(() => {
  if (filteredJustifications.value.length === 0) return 0
  return (currentPage.value - 1) * itemsPerPage.value + 1
})

const paginationEnd = computed(() => {
  return Math.min(currentPage.value * itemsPerPage.value, filteredJustifications.value.length)
})

const visiblePages = computed(() => {
  const pages = []
  const maxVisible = 5
  let start = Math.max(1, currentPage.value - Math.floor(maxVisible / 2))
  let end = Math.min(totalPages.value, start + maxVisible - 1)
  
  if (end - start + 1 < maxVisible) {
    start = Math.max(1, end - maxVisible + 1)
  }
  
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  return pages
})

// Funciones de utilidad
const formatDate = (date: string) => {
  // Evita desfase por zona horaria cuando el backend envía fechas como YYYY-MM-DD.
  const dateOnlyMatch = /^(\d{4})-(\d{2})-(\d{2})$/.exec(date)
  const parsedDate = dateOnlyMatch
    ? new Date(
        Number(dateOnlyMatch[1]),
        Number(dateOnlyMatch[2]) - 1,
        Number(dateOnlyMatch[3])
      )
    : new Date(date)

  if (Number.isNaN(parsedDate.getTime())) return date

  return parsedDate.toLocaleDateString('es-ES', {
    day: 'numeric',
    month: 'long',
    year: 'numeric'
  })
}

const truncateText = (text: string, maxLength: number) => {
  if (text.length <= maxLength) return text
  return text.substring(0, maxLength) + '...'
}

const toggleDescription = (id: bigint) => {
  const key = String(id)
  expandedDescriptions.value[key] = !expandedDescriptions.value[key]
}

const clearFilters = () => {
  searchQuery.value = ''
  dateFilter.value = 'all'
  sortBy.value = 'recent'
  currentPage.value = 1
}

// Funciones de Toast
const addToast = (type: Toast['type'], title: string, message: string, duration: number = 3000) => {
  const icon = type === 'success' ? CheckCircle : XCircle
  const toast: Toast = {
    id: nextToastId++,
    type,
    title,
    message,
    icon,
    duration
  }
  toasts.value.push(toast)
  
  setTimeout(() => {
    removeToast(toast.id)
  }, duration)
}

const removeToast = (id: number) => {
  toasts.value = toasts.value.filter(t => t.id !== id)
}

// Acciones
const refreshList = async () => {
  await loadJustifications()
}

const approveJustification = async (item: JustificationResponse) => {
  try {
    const response = await approve(item.id)

    if (response.success) {
      // Eliminar de la lista local
      //pendingJustifications.value = pendingJustifications.value.filter(
      //  j => j.id !== item.id
      //)
      loadJustifications() // Recargar para obtener la lista actualizada
      
      // Mostrar toast de éxito
      addToast('success', 'Justificación aprobada', `La justificación de ${item.studentName} ha sido aprobada correctamente`)
    } else {
      addToast('error', 'Error', response.error.message)
    }
  } catch (error) {
    addToast('error', 'Error', error instanceof Error ? error.message : 'Error al aprobar la justificación')
  }
}

const rejectJustification = async (item: JustificationResponse) => {
  try {
    const response = await reject(item.id)

    if (response.success) {
      // Eliminar de la lista local
      //pendingJustifications.value = pendingJustifications.value.filter(
      //  j => j.id !== item.id
      //)
      loadJustifications() // Recargar para obtener la lista actualizada
      
      // Mostrar toast de éxito
      addToast('success', 'Justificación denegada', `La justificación de ${item.studentName} ha sido denegada`)
      
    } else {
      addToast('error', 'Error', response.error.message)
    }
  } catch (error) {
    addToast('error', 'Error', error instanceof Error ? error.message : 'Error al denegar la justificación')
  }
}

// Carga de datos
const loadJustifications = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    const response = await getPendingJustifications()

    if (response.success) {
      pendingJustifications.value = response.data
    } else {
      errorMessage.value = response.error.message
    }
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Error de conexión'
  } finally {
    loading.value = false
  }
}

onMounted(loadJustifications)
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