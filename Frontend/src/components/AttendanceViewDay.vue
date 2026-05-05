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
          <select v-model="filter.classId"
                  :disabled="loading"
                  class="w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent appearance-none cursor-pointer disabled:opacity-50">
            <option :value="null">Todas las secciones</option>
            <option v-for="section in sections.sections" :key="section.id" :value="section.id">
              {{ section.name }}
            </option>
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

      <!-- Botones de acción de filtros -->
      <div class="mt-4 flex flex-wrap items-center justify-between gap-3">
        <div class="flex items-center space-x-3">
          <button @click="applyFilters"
                  :disabled="loading"
                  class="inline-flex items-center space-x-2 px-4 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 transition-all duration-300 transform hover:scale-105 shadow-md disabled:opacity-50 disabled:cursor-not-allowed">
            <Filter class="w-4 h-4" />
            <span>Aplicar filtros</span>
          </button>
          <button @click="clearAllFilters"
                  :disabled="loading"
                  class="inline-flex items-center space-x-2 px-4 py-2 border border-slate-200 text-slate-600 rounded-lg hover:bg-slate-50 hover:border-slate-300 transition-all duration-300">
            <RotateCw class="w-4 h-4" />
            <span>Limpiar filtros</span>
          </button>
        </div>
        
        <button @click="refreshTable"
                :disabled="loading"
                class="inline-flex items-center space-x-2 px-4 py-2 text-slate-600 hover:bg-slate-100 rounded-lg transition-all duration-300"
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
        <p class="text-sm text-slate-500">Cargando asistencias...</p>
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
                  <div class="flex items-center space-x-2 duration-200">
                    <span :class="getAttendanceBadgeClass(attendance.attendanceType)" 
                          class="px-3 py-1 text-xs rounded-full font-medium">
                      {{ getAttendanceTypeLabel(attendance.attendanceType) }}
                    </span>
                    <button v-if="attendance.attendanceType.toLocaleLowerCase() === 'ausente'"
                            class="p-2 text-slate-300 hover:text-slate-600 hover:bg-slate-100 rounded-lg transition-all duration-200 hover:scale-110"
                            title="Justificar ausencia"
                            @click="selectStudentForJustification(attendance)">
                      <AlertCircle class="w-4 h-4" />
                    </button>
                    <button v-if="attendance.attendanceType.toLocaleLowerCase() === 'ausente'"
                            class="p-2 text-slate-300 hover:text-slate-600 hover:bg-slate-100 rounded-lg transition-all duration-200 hover:scale-110"
                            title="Notificar Padre"
                            @click="getContactInfo(Number(attendance.idAttendance), `${attendance.studentName} ${attendance.studentFirstLastName} ${attendance.studentSecondLastName}`)">
                      <Send class="w-4 h-4" />
                    </button>
                  </div>
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

  <!-- Modal de justificar alumno -->
    <transition name="fade">
      <div v-if="justificationModal" 
           class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4 z-50"
           @click.self="closeJustificationModal">
        <div class="bg-white rounded-2xl max-w-md w-full p-6 animate-slide-up">
          <div class="flex items-center justify-between mb-4">
            <div class="flex items-center space-x-3">
              <div class="w-10 h-10 bg-yellow-100 rounded-lg flex items-center justify-center">
                <PenBox class="w-5 h-5 text-amber-600" />
              </div>
              <h3 class="text-lg font-semibold text-slate-800">Justificar Alumno</h3>
            </div>
            <button @click="closeJustificationModal" 
                    class="p-2 hover:bg-slate-100 rounded-lg transition-colors">
              <X class="w-5 h-5 text-slate-500" />
            </button>
          </div>
          
          <div class="mb-4 p-3 bg-slate-50 rounded-lg">
            <p class="text-xs text-slate-500 mb-1">Alumno</p>
            <p class="text-sm font-semibold text-slate-800">{{ selectedName }}</p>
          </div>

          <div class="mb-6">
            <label for="justification-reason" class="block text-sm font-medium text-slate-700 mb-2">
              Motivo de justificación <span class="text-red-500">*</span>
            </label>
            <textarea id="justification-reason"
                      v-model="justificationReason"
                      rows="4"
                      class="w-full px-4 py-2 border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent resize-none"
                      placeholder="Escribe el motivo por el cual se justifica la ausencia del alumno..."></textarea>
            <p v-if="!justificationReason.trim() && showReasonError" 
               class="text-xs text-red-500 mt-1">El motivo es obligatorio</p>
          </div>
          
          <div class="flex justify-end space-x-3">
            <button @click="closeJustificationModal"
                    class="px-4 py-2 text-sm text-slate-600 hover:bg-slate-100 rounded-lg transition-colors">
              Cancelar
            </button>
            <button @click="executeJustification"
                    :disabled="isSubmitting"
                    class="relative px-4 py-2 bg-slate-800 text-white text-sm rounded-lg hover:bg-slate-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed">
              <span :class="{ 'opacity-0': isSubmitting }">Justificar</span>
              <div v-if="isSubmitting" 
                   class="absolute inset-0 flex items-center justify-center">
                <Loader2 class="w-4 h-4 animate-spin" />
              </div>
            </button>
          </div>
        </div>
      </div>
    </transition>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { contactStudent, getAttendances } from '@/services/attendancesService'
import type { AttendanceFilter, AttendanceResponse, ContactResponse } from '@/types/Attendance'
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
  MoveDownIcon,
  PenBox,
  Send
} from 'lucide-vue-next'
import type { JustificationProfessorRequest } from '@/types/Justification'
import { addProfessorJustification } from '@/services/justificationsService'
import { useToast } from '@/composables/useToast'
import { useSectionStore } from '@/stores/sectionStore'

// Constantes
const PAGE_SIZE = 15

const columns = [
  { key: 'studentDni', label: 'DNI' },
  { key: 'studentName', label: 'Nombre' },
  { key: 'studentFirstLastName', label: 'Apellido Paterno' },
  { key: 'studentSecondLastName', label: 'Apellido Materno' }
] as const

// Helper para obtener fecha actual
const getTodayDate = (): string => {
  const today = new Date()
  const year = today.getFullYear()
  const month = String(today.getMonth() + 1).padStart(2, '0')
  const day = String(today.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

// Estado
const loading = ref(false)
const errorMessage = ref<string>('')
const attendances = ref<AttendanceResponse[]>([])
const totalPages = ref(0)
const totalElements = ref(0)
const currentPage = ref(0)

const isSubmitting = ref(false)
const showReasonError = ref(false)
const justificationModal = ref(false)
const justificationReason = ref('')
const selectedStudent = ref<JustificationProfessorRequest | null>(null)
const selectedName = ref('')

const filter = ref<AttendanceFilter>({
  date: getTodayDate(),
  name: '',
  classId: null,
  attendanceType: ''
})

const sort = ref<Sort>({
  field: 'studentFirstLastName',
  direction: 'asc'
})

// Inicializar toast
const toast = useToast()
const sections = useSectionStore()

// Computed properties
const hasActiveFilters = computed(() => {
  return filter.value.name !== '' || 
         filter.value.classId !== null || 
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
const applyFilters = () => {
  currentPage.value = 0
  loadAttendances()
  toast.showInfo('Filtros aplicados', 'Los filtros se han aplicado correctamente', 3000)
}

const clearAllFilters = () => {
  filter.value = {
    date: getTodayDate(),
    name: '',
    classId: null,
    attendanceType: ''
  }
  currentPage.value = 0
  loadAttendances()
  toast.showInfo('Filtros limpiados', 'Todos los filtros han sido restablecidos', 3000)
}

const refreshTable = () => {
  loadAttendances()
  toast.showInfo('Actualizando', 'Recargando datos de la tabla...', 2000)
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
      totalElements.value = Number(response.data.totalElements)
    } else {
      errorMessage.value = response.error.message
    }
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Error de conexión'
  } finally {
    loading.value = false
  }
}

// Formulario de justificación
const selectStudentForJustification = (attendance: AttendanceResponse) => {
  selectedStudent.value = {
    idAttendance: attendance.idAttendance,
    description: ''
  }
  selectedName.value = `${attendance.studentName} ${attendance.studentFirstLastName} ${attendance.studentSecondLastName}`
  showReasonError.value = false
  justificationModal.value = true
  justificationReason.value = ''
}

const closeJustificationModal = () => {
  justificationModal.value = false
  selectedStudent.value = null
  selectedName.value = ''
  justificationReason.value = ''
  showReasonError.value = false
}

const executeJustification = async () => {
  if (!justificationReason.value.trim()) {
    showReasonError.value = true
    toast.showWarning('Campo requerido', 'Debe ingresar un motivo de justificación')
    return
  }  
  isSubmitting.value = true

  try {
    selectedStudent.value!.description = justificationReason.value
    
    const response = await addProfessorJustification(selectedStudent.value as JustificationProfessorRequest)

    if (response.success) {
      toast.showSuccess('Ausencia justificada', 'Se ha justificado la ausencia del estudiante correctamente')
      await loadAttendances()
      closeJustificationModal()
    } else {
      toast.showError('Error', response.error.message)
    }
  } catch (error) {
    toast.showError('Error de conexión', error instanceof Error ? error.message : 'Error desconocido')
  } finally {
    isSubmitting.value = false
  }
}

// Notificar padre
const getContactInfo = async (id: number, studentName: string) => {
  try {
    const response = await contactStudent(id)

    if(response.success) {
      response.data.studentName = studentName
      const contactInfo = response.data
      sendMensageToParent(contactInfo)
      toast.showInfo('Notificación', 'Se abrirá WhatsApp para enviar el mensaje')
    } else {
      toast.showError('Error', response.error.message)
    }
  } catch (error) {
    toast.showError('Error de conexión', error instanceof Error ? error.message : 'Error desconocido')
  }
}

const sendMensageToParent = async (contactInfo: ContactResponse) => {
  try {
    const justificationUrl = `https://school-attendance-manager-dun.vercel.app/justifications/${contactInfo.token}`
    const mensage = encodeURIComponent(
      `🚨 Aviso de Asistencia Escolar
Estimado(a) Padre/Madre de Familia 👨‍👩‍👧‍👦:
Reciba un cordial saludo 🤝.
Le informamos que su hijo(a) *${contactInfo.studentName}* 📌 no registra ingreso al colegio el día de hoy. 
Le solicitamos, por favor, realizar la justificación de la inasistencia mediante el siguiente enlace:

👇 Acceda al formulario:
${justificationUrl}

Agradecemos su apoyo para mantener actualizada la asistencia del estudiante ✅.
📍 I.E. Gral. José de San Martin`)

    const url = `https://api.whatsapp.com/send?phone=${contactInfo.number}&text=${mensage}`
    window.open(url, '_blank')
  } catch (error) {
    alert('Error al enviar el mensaje: ' + (error instanceof Error ? error.message : 'Error desconocido'))
  }
}

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