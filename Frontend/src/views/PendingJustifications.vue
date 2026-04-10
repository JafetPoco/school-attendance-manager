<template>
  <div class="min-h-screen bg-slate-50">
    <Header />
    
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
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
              <p class="text-xl font-bold text-amber-700">{{ totalJustifications }}</p>
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
          <!-- Filtro por fecha -->
          <div class="relative">
            <Calendar class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
            <select v-model="filter.dateFilter"
                    class="w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 appearance-none cursor-pointer">
              <option value="TODAY">Hoy</option>
              <option value="WEEK">Esta semana</option>
              <option value="MONTH">Este mes</option>
            </select>
          </div>

          <!-- Ordenamiento -->
          <div class="relative">
            <Filter class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
            <select v-model="sort.direction"
                    class="w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 appearance-none cursor-pointer">
              <option value="asc">Ordenar A-Z</option>
              <option value="desc">Ordenar Z-A</option>
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
      <div v-if="!loading && !errorMessage && pendingJustifications.length > 0" 
           class="grid grid-cols-1 lg:grid-cols-2 gap-6 animate-fade-in-up">
        <div v-for="item in pendingJustifications" 
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
                      :disabled="processingId === item.id"
                      class="flex items-center space-x-2 px-4 py-2 text-red-600 hover:bg-red-50 rounded-lg transition-all duration-200 group disabled:opacity-50 disabled:cursor-not-allowed">
                <Loader2 v-if="processingId === item.id" class="w-4 h-4 animate-spin" />
                <X v-else class="w-4 h-4 group-hover:scale-110 transition-transform" />
                <span>{{ processingId === item.id ? 'Procesando...' : 'Denegar' }}</span>
              </button>
              <button @click="approveJustification(item)"
                      :disabled="processingId === item.id"
                      class="flex items-center space-x-2 px-4 py-2 bg-emerald-600 text-white rounded-lg hover:bg-emerald-700 transition-all duration-200 transform hover:scale-105 shadow-md group disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:scale-100">
                <Loader2 v-if="processingId === item.id" class="w-4 h-4 animate-spin" />
                <Check v-else class="w-4 h-4 group-hover:scale-110 transition-transform" />
                <span>{{ processingId === item.id ? 'Procesando...' : 'Aprobar' }}</span>
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Empty state -->
      <div v-if="!loading && !errorMessage && pendingJustifications.length === 0" 
           class="bg-white rounded-xl border border-slate-200 p-12 text-center animate-fade-in-up">
        <FileCheck class="w-16 h-16 text-slate-300 mx-auto mb-4" />
        <h3 class="text-lg font-semibold text-slate-800 mb-2">No hay justificaciones pendientes</h3>
        <p class="text-sm text-slate-500">
          No se encontraron resultados para las justificaciones pendientes.
        </p>
      </div>

      <!-- Paginación -->
      <div v-if="totalJustifications > 0 && totalPages > 1" 
           class="mt-8 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div class="text-xs text-slate-500">
          Mostrando {{ paginationStart }} - {{ paginationEnd }} de {{ totalJustifications }} justificaciones
        </div>
        <div class="flex items-center space-x-2">
          <button @click="goToFirstPage" 
                  :disabled="currentPage === 0 || loading"
                  class="p-2 border border-slate-200 rounded-lg text-slate-600 hover:bg-white disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                  title="Primera página">
            <ChevronsLeft class="w-4 h-4" />
          </button>
          <button @click="prevPage" 
                  :disabled="currentPage === 0 || loading"
                  class="p-2 border border-slate-200 rounded-lg text-slate-600 hover:bg-white disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                  title="Página anterior">
            <ChevronLeft class="w-4 h-4" />
          </button>
          <div class="flex space-x-1">
            <button v-for="page in visiblePages" 
                    :key="page"
                    @click="goToPage(page - 1)"
                    class="min-w-8 px-2 py-1 rounded-lg text-sm transition-colors"
                    :class="currentPage === page - 1 
                      ? 'bg-slate-800 text-white' 
                      : 'border border-slate-200 text-slate-600 hover:bg-white'">
              {{ page }}
            </button>
          </div>
          <button @click="nextPage"
                  :disabled="currentPage >= totalPages - 1 || loading"
                  class="p-2 border border-slate-200 rounded-lg text-slate-600 hover:bg-white disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                  title="Página siguiente">
            <ChevronRight class="w-4 h-4" />
          </button>
          <button @click="goToLastPage"
                  :disabled="currentPage >= totalPages - 1 || loading"
                  class="p-2 border border-slate-200 rounded-lg text-slate-600 hover:bg-white disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                  title="Última página">
            <ChevronsRight class="w-4 h-4" />
          </button>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import Header from '@/components/Header.vue'
import { ref, computed, onMounted, watch } from 'vue'
import { getPendingJustifications, approveJustification as approve, rejectJustification as reject } from '@/services/justificationsService'
import type { JustificationFilter, JustificationResponse } from '@/types/Justification'
import type { PageRequest, Sort } from '@/types/Pages'
import {
  FileCheck,
  RotateCw,
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
  ChevronLeft,
  ChevronRight,
  ChevronsLeft,
  ChevronsRight
} from 'lucide-vue-next'
import { useToast } from '@/composables/useToast'

// Constantes
const PAGE_SIZE = 10

// Estado
const loading = ref(false)
const errorMessage = ref('')
const pendingJustifications = ref<JustificationResponse[]>([])
const currentPage = ref(0)
const totalPages = ref(0)
const totalJustifications = ref(0)
const expandedDescriptions = ref<Record<string, boolean>>({})
const processingId = ref<bigint | null>(null)
const searchQuery = ref('')

const filter = ref<JustificationFilter>({
  dateFilter: 'TODAY'
})

const sort = ref<Sort>({
  field: 'attendanceStudentName',
  direction: 'asc'
})

const paginationStart = computed(() => {
  if (totalJustifications.value === 0) return 0
  return currentPage.value * PAGE_SIZE + 1
})

const paginationEnd = computed(() => {
  if (totalJustifications.value === 0) return 0
  return Math.min((currentPage.value + 1) * PAGE_SIZE, totalJustifications.value)
})

const visiblePages = computed(() => {
  const pages = []
  const maxVisible = 5
  let start = Math.max(1, currentPage.value + 1 - Math.floor(maxVisible / 2))
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

// Inicializar toast
const toast = useToast()

// Acciones de paginación
const goToFirstPage = () => {
  if (currentPage.value > 0) {
    currentPage.value = 0
    loadJustifications()
  }
}

const goToLastPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value = totalPages.value - 1
    loadJustifications()
  }
}

const prevPage = () => {
  if (currentPage.value > 0) {
    currentPage.value -= 1
    loadJustifications()
  }
}

const nextPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value += 1
    loadJustifications()
  }
}

const goToPage = (page: number) => {
  if (page >= 0 && page < totalPages.value && page !== currentPage.value) {
    currentPage.value = page
    loadJustifications()
  }
}

// Acciones de justificaciones
const refreshList = async () => {
  currentPage.value = 0
  await loadJustifications()
}

const approveJustification = async (item: JustificationResponse) => {
  if (processingId.value) return
  
  processingId.value = item.id
  
  try {
    const response = await approve(item.id)

    if (response.success) {
      toast.showSuccess('Justificación aprobada', `La justificación de ${item.studentName} ha sido aprobada`)
      await loadJustifications()
    } else {
      toast.showError('Error', response.error.message)
    }
  } catch (error) {
    toast.showError('Error', error instanceof Error ? error.message : 'Error al aprobar la justificación')
  } finally {
    processingId.value = null
  }
}

const rejectJustification = async (item: JustificationResponse) => {
  if (processingId.value) return
  
  processingId.value = item.id
  
  try {
    const response = await reject(item.id)

    if (response.success) {
      toast.showSuccess('Justificación denegada', `La justificación de ${item.studentName} ha sido denegada`)
      await loadJustifications()
    } else {
      toast.showError('Error', response.error.message)
    }
  } catch (error) {
    toast.showError('Error', error instanceof Error ? error.message : 'Error al denegar la justificación')
  } finally {
    processingId.value = null
  }
}

// Carga de datos
const buildPayloadFilter = (): JustificationFilter => ({
  dateFilter: filter.value.dateFilter
})

const buildPayloadPage = (): PageRequest => ({
  page: currentPage.value,
  size: PAGE_SIZE
})

const buildPayloadSort = (): Sort => ({
  field: sort.value.field,
  direction: sort.value.direction
})

const loadJustifications = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    const response = await getPendingJustifications(
      buildPayloadFilter(),
      buildPayloadPage(),
      buildPayloadSort()
    )

    if (response.success) {
      pendingJustifications.value = response.data.content
      totalPages.value = response.data.totalPages
      currentPage.value = response.data.page
      totalJustifications.value = Number(response.data.totalElements)
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
watch([() => filter.value.dateFilter, () => searchQuery.value, () => sort.value.direction], () => {
  currentPage.value = 0
  loadJustifications()
})

// Lifecycle
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

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(100%);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.animate-fade-in-down {
  animation: fadeInDown 0.6s ease-out;
}

.animate-fade-in-up {
  animation: fadeInUp 0.5s ease-out;
}

.animate-slide-in-right {
  animation: slideInRight 0.3s ease-out;
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