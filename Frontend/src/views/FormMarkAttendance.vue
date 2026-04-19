<template>
  <Header />
  <div class=" bg-slate-50 py-8 px-4 sm:px-6 lg:px-8">
    <main class="max-w-7xl mx-auto">
      <!-- Header de la página -->
      <div class="mb-8 animate-fade-in-down">
        <div class="flex items-center space-x-3 mb-2">
          <div class="w-12 h-12 bg-slate-800 rounded-xl flex items-center justify-center shadow-lg">
            <ClipboardCheck class="w-6 h-6 text-white" />
          </div>
          <div>
            <h1 class="text-2xl font-bold text-slate-800">Registrar Asistencia</h1>
            <p class="text-sm text-slate-500">Registra la asistencia diaria de los estudiantes</p>
          </div>
        </div>
      </div>

      <!-- Formulario principal -->
      <div class="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden animate-fade-in-up">
        <form @submit.prevent="onSubmit" class="p-6 space-y-6">
          <!-- Campo búsqueda de estudiante -->
          <div class="space-y-2">
            <label for="student-search" class="block text-sm font-medium text-slate-700">
              Nombre del Estudiante <span class="text-red-500">*</span>
            </label>
            <div class="relative">
              <IdCard class="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-slate-400" />
              <input 
                type="text"
                id="student-search"
                v-model="searchQuery"
                @input="onInput"
                @keydown.enter.prevent="seleccionarPrimerSugerencia"
                class="w-full pl-12 pr-4 py-3 bg-white border rounded-xl text-base focus:ring-2 focus:ring-slate-800 focus:border-transparent transition-all"
                :class="[errors.student ? 'border-red-300 bg-red-50' : 'border-slate-200 hover:border-slate-300']"
                placeholder="Escribe el nombre del estudiante..."
                autocomplete="off"
              />

              <!-- Sugerencias de estudiantes -->
              <div v-if="sugerencias.length > 0" class="sugerencias-container absolute z-10 w-full mt-1 bg-white border border-slate-200 rounded-xl shadow-lg">
                <ul class="max-h-60 overflow-y-auto">
                  <li
                    v-for="(sug, index) in sugerencias"
                    :key="sug.dni"
                    @click="seleccionarEstudiante(sug)"
                    @mouseenter="hoveredIndex = index"
                    class="px-4 py-3 cursor-pointer transition-colors hover:bg-slate-50"
                    :class="{ 'bg-slate-50': hoveredIndex === index }"
                  >
                    <div class="flex items-center justify-between">
                      <div>
                        <p class="font-medium text-slate-800">{{ sug.fullName }}</p>
                        <p class="text-xs text-slate-500 mt-1">DNI: {{ sug.dni }}</p>
                      </div>
                      <ChevronRight class="w-4 h-4 text-slate-400" />
                    </div>
                  </li>
                </ul>
              </div>
            </div>
            <p v-if="errors.student" class="text-xs text-red-500 flex items-center">
              <AlertCircle class="w-3 h-3 mr-1" />
              {{ errors.student }}
            </p>
            <p class="text-xs text-slate-400">Ingresa el nombre y selecciona un estudiante de la lista</p>
          </div>

          <!-- Información del estudiante (se muestra después de seleccionar) -->
          <transition name="fade">
            <div v-if="selectedStudent" class="bg-slate-50 rounded-xl p-5 border border-slate-200">
              <div class="flex items-start space-x-4">
                <div class="flex-1">
                  <h3 class="text-lg font-semibold text-slate-800">{{ selectedStudent.fullName }}</h3>
                  <p class="text-sm text-slate-600 mt-2">
                    <span class="font-mono">DNI: {{ selectedStudent.dni }}</span>
                  </p>
                </div>
              </div>
            </div>
          </transition>

          <!-- Selector de tipo de asistencia -->
          <fieldset class="space-y-2">
            <legend class="block text-sm font-medium text-slate-700">
              Tipo de Asistencia <span class="text-red-500">*</span>
            </legend>
            <div class="grid grid-cols-2 gap-4">
              <button 
                type="button"
                @click="request.attendanceType = 'presente'"
                class="flex items-center justify-center space-x-2 p-4 rounded-xl border-2 transition-all duration-300"
                :class="request.attendanceType === 'presente' 
                  ? 'bg-emerald-50 border-emerald-500 text-emerald-700' 
                  : 'bg-white border-slate-200 text-slate-600 hover:border-emerald-300 hover:bg-emerald-50'"
              >
                <CheckCircle class="w-5 h-5" />
                <span class="font-medium">Presente</span>
              </button>
              <button 
                type="button"
                @click="request.attendanceType = 'tarde'"
                class="flex items-center justify-center space-x-2 p-4 rounded-xl border-2 transition-all duration-300"
                :class="request.attendanceType === 'tarde' 
                  ? 'bg-amber-50 border-amber-500 text-amber-700' 
                  : 'bg-white border-slate-200 text-slate-600 hover:border-amber-300 hover:bg-amber-50'"
              >
                <Clock class="w-5 h-5" />
                <span class="font-medium">Tarde</span>
              </button>
            </div>
          </fieldset>

          <!-- Fecha actual -->
          <div class="flex items-center justify-between text-sm text-slate-500 bg-slate-50 rounded-lg p-3">
            <div class="flex items-center space-x-2">
              <Calendar class="w-4 h-4" />
              <span>Fecha de registro</span>
            </div>
            <span class="font-medium text-slate-700">{{ currentDate }}</span>
          </div>

          <!-- Botones de acción -->
          <div class="flex items-center justify-end space-x-3 pt-4 border-t border-slate-200">
            <button 
              type="button"
              @click="resetForm"
              class="px-4 py-2 text-sm text-slate-600 hover:bg-slate-100 rounded-lg border border-slate-200 transition-all duration-300"
            >
              Limpiar
            </button>
            <button 
              type="submit"
              :disabled="loading || !isFormValid"
              class="relative px-6 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 transition-all duration-300 transform hover:scale-105 shadow-md disabled:opacity-50 disabled:cursor-not-allowed flex items-center space-x-2"
            >
              <Loader2 v-if="loading" class="w-4 h-4 animate-spin" />
              <Save v-else class="w-4 h-4" />
              <span>{{ loading ? 'Registrando...' : 'Registrar Asistencia' }}</span>
            </button>
          </div>
        </form>
      </div>

      <!-- Mensaje de éxito -->
      <transition name="slide-up">
        <div v-if="successMessage" 
          class="mt-6 bg-emerald-50 border border-emerald-200 rounded-xl p-5 flex items-start space-x-4 animate-fade-in-up"
        >
          <div class="w-10 h-10 bg-emerald-100 rounded-lg flex items-center justify-center">
            <CheckCircle class="w-5 h-5 text-emerald-600" />
          </div>
          <div class="flex-1">
            <h3 class="text-sm font-semibold text-emerald-800">¡Asistencia registrada exitosamente!</h3>
            <div class="mt-2 text-sm text-emerald-700">
              <p><strong>Estudiante:</strong> {{ successMessage.studentName }}</p>
              <p><strong>Fecha:</strong> {{ formatDateTime(successMessage.date) }}</p>
              <p><strong>Estado:</strong> {{ getAttendanceTypeLabel(request.attendanceType) }}</p>
            </div>
          </div>
          <button @click="successMessage = null" class="text-emerald-400 hover:text-emerald-600">
            <X class="w-4 h-4" />
          </button>
        </div>
      </transition>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import Header from '@/components/Header.vue'
import { addAttendance } from '@/services/addAttendanceService'
import { autocompleteStudents } from '@/services/studentsService'
import { useToast } from '@/composables/useToast'
import type { AttendanceResponse, AttendanceRequest } from '@/types/Attendance'
import type { StudentSuggestionResponse } from '@/types/Student'
import {
  ClipboardCheck,
  ChevronRight,
  IdCard,
  AlertCircle,
  CheckCircle,
  Clock,
  Calendar,
  Save,
  Loader2,
  X
} from 'lucide-vue-next'

const toast = useToast()

// Estado del buscador
const searchQuery = ref('')
const sugerencias = ref<StudentSuggestionResponse[]>([])
const selectedStudent = ref<StudentSuggestionResponse | null>(null)
const hoveredIndex = ref(-1)
let debounceTimeout: ReturnType<typeof setTimeout> | null = null

// Estado del formulario
const loading = ref(false)
const errorMessage = ref('')
const successMessage = ref<AttendanceResponse | null>(null)

// Request de asistencia
const request = ref<AttendanceRequest>({
  dni: '',
  attendanceType: 'presente'
})

// Errores de validación
const errors = ref({
  student: ''
})

// Computed
const currentDate = computed(() => {
  const date = new Date()
  return date.toLocaleDateString('es-ES', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
})

const isFormValid = computed(() => {
  return selectedStudent.value !== null
})

// Métodos del buscador
const onInput = () => {
  if (debounceTimeout) clearTimeout(debounceTimeout)
  
  // Limpiar selección si el usuario modifica el texto
  if (selectedStudent.value) {
    selectedStudent.value = null
    request.value.dni = ''
  }
  
  // Limpiar errores
  errors.value.student = ''
  
  debounceTimeout = setTimeout(async () => {
    const query = searchQuery.value.trim()
    
    if (query.length >= 2) {
      try {
        const response = await autocompleteStudents(query)
        if (response.success) {
          sugerencias.value = response.data || []
        } else {
          sugerencias.value = []
        }
        
        // Reset hover index
        hoveredIndex.value = -1
      } catch (error) {
        console.error('Error fetching suggestions:', error)
        sugerencias.value = []
      }
    } else {
      sugerencias.value = []
    }
  }, 300)
}

const seleccionarEstudiante = (estudiante: StudentSuggestionResponse) => {
  selectedStudent.value = estudiante
  request.value.dni = estudiante.dni
  
  // Actualizar el input con el nombre del estudiante seleccionado
  searchQuery.value = estudiante.fullName
  
  // Limpiar sugerencias
  sugerencias.value = []
  
  // Limpiar errores
  errors.value.student = ''
}

const seleccionarPrimerSugerencia = () => {
  if (sugerencias.value.length > 0) {
    const primera = sugerencias.value[0]
    if (primera) {
      seleccionarEstudiante(primera)
    }
  }
}

// Cerrar sugerencias al hacer click fuera
const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as HTMLElement
  const inputContainer = document.getElementById('student-search')?.closest('.relative')
  if (inputContainer && !inputContainer.contains(target)) {
    sugerencias.value = []
  }
}

// Envío del formulario
const onSubmit = async () => {
  // Validaciones
  if (!selectedStudent.value) {
    errors.value.student = 'Debes seleccionar un estudiante'
    return
  }
  
  if (!request.value.attendanceType) {
    errorMessage.value = 'Debes seleccionar un tipo de asistencia'
    return
  }
  
  loading.value = true
  errorMessage.value = ''
  
  try {
    const response = await addAttendance(request.value)
    
    if (response.success) {
      successMessage.value = response.data
      toast.showSuccess('Éxito', `Asistencia de ${selectedStudent.value?.fullName} registrada`)
      
      // Resetear formulario después de éxito
      setTimeout(() => {
        resetForm()
      }, 2000)
    } else {
      errorMessage.value = response.error?.message || 'Error al registrar la asistencia'
      toast.showError('Error', errorMessage.value)
    }
  } catch (error: any) {
    errorMessage.value = error?.message || 'Error de conexión'
    toast.showError('Error', errorMessage.value)
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  searchQuery.value = ''
  sugerencias.value = []
  selectedStudent.value = null
  request.value = {
    dni: '',
    attendanceType: 'presente'
  }
  errors.value = {
    student: ''
  }
  errorMessage.value = ''
  successMessage.value = null
}

const getAttendanceTypeLabel = (type: string) => {
  const labels: Record<string, string> = {
    'presente': 'Presente',
    'tarde': 'Tarde',
    'ausente': 'Ausente',
    'justificado': 'Justificado'
  }
  return labels[type] || type
}

const formatDateTime = (date: string) => {
  if (!date) return ''
  return new Date(date).toLocaleString('es-ES', {
    day: 'numeric',
    month: 'long',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
  if (debounceTimeout) clearTimeout(debounceTimeout)
})
</script>

<style scoped>
/* Animaciones */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.slide-up-enter-active,
.slide-up-leave-active {
  transition: all 0.3s ease;
}

.slide-up-enter-from,
.slide-up-leave-to {
  opacity: 0;
  transform: translateY(20px);
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

.animate-fade-in-down {
  animation: fadeInDown 0.6s ease-out;
}

.animate-fade-in-up {
  animation: fadeInUp 0.5s ease-out;
}

/* Scroll personalizado para sugerencias */
.sugerencias-container ul::-webkit-scrollbar {
  width: 6px;
}

.sugerencias-container ul::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.sugerencias-container ul::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 10px;
}

.sugerencias-container ul::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
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