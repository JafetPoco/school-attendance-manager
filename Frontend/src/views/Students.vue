<template>
  <Header></Header>
  <div class="min-h-screen bg-slate-50 py-8 px-4 sm:px-6 lg:px-8">
    <div class="max-w-7xl mx-auto">
      <!-- Header -->
      <div class="mb-8 animate-fade-in-down">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between">
          <div class="flex items-center space-x-3 mb-4 sm:mb-0">
            <div class="w-12 h-12 bg-slate-800 rounded-xl flex items-center justify-center shadow-lg">
              <GraduationCap class="w-6 h-6 text-white" />
            </div>
            <div>
              <h1 class="text-2xl font-bold text-slate-800">Estudiantes</h1>
              <p class="text-sm text-slate-500">Gestión y visualización de estudiantes</p>
            </div>
          </div>
          
          <!-- Estadísticas -->
          <div class="flex items-center space-x-4">
            <div class="bg-white px-4 py-2 rounded-lg border border-slate-200">
              <p class="text-xs text-slate-500">Total estudiantes</p>
              <p class="text-xl font-bold text-slate-800">{{ totalStudents }}</p>
            </div>
            <button @click="goToNewStudent" 
            class="bg-slate-800 text-white px-4 py-2 rounded-lg hover:bg-slate-700 transition-all duration-300 transform hover:scale-105 flex items-center space-x-2">
              <Plus class="w-4 h-4" />
              <span>Nuevo Estudiante</span>
            </button>
          </div>
        </div>

        <!-- Filtros -->
        <div class="mt-6 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <!-- Búsqueda por nombre -->
          <div class="relative">
            <Search class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
            <input type="text"
                   v-model="filter.name"
                   placeholder="Buscar por nombre o apellido..."
                   class="w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent transition-all">
          </div>

          <!-- Filtro Nivel -->
          <div class="relative">
            <BarChart3 class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
            <select v-model="filter.level"
                    class="w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent appearance-none cursor-pointer">
              <option value="">Todos los niveles</option>
              <option value="PRIMARIA">Primaria</option>
              <option value="SECUNDARIA">Secundaria</option>
            </select>
          </div>

          <!-- Filtro Grado -->
          <div class="relative">
            <Layers class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
            <select v-model="filter.grade"
                    class="w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent appearance-none cursor-pointer">
              <option value="">Todos los grados</option>
              <option value="PRIMERO">Primero</option>
              <option value="SEGUNDO">Segundo</option>
              <option value="TERCERO">Tercero</option>
              <option value="CUARTO">Cuarto</option>
              <option value="QUINTO">Quinto</option>
            </select>
          </div>

          <!-- Filtro Sección -->
          <div class="relative">
            <Grid3x3 class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
            <select v-model="filter.section"
                    class="w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent appearance-none cursor-pointer">
              <option value="">Todas las secciones</option>
              <option value="BENJAMIN">Benjamin</option>
              <option value="NOE">Noé</option>
              <option value="MOISES">Moisés</option>
              <option value="DAVID">David</option>
              <option value="SALOMON">Salomón</option>
              <option value="JACOB">Jacob</option>
              <option value="ENOC">Enoc</option>
              <option value="JOSE">José</option>
              <option value="GEDEON">Gedeón</option>
              <option value="JOSUE">Josué</option>
              <option value="ELIAS">Elías</option>
              <option value="ELISEO">Eliseo</option>
              <option value="DANIEL">Daniel</option>
              <option value="ESTEBAN">Esteban</option>
              <option value="MATEO">Mateo</option>
              <option value="SALOMON">Salomón</option>
              <option value="DAVID">David</option>
              <option value="JONATAN">Jonatán</option>
            </select>
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

        <!-- Filtros activos -->
        <div v-if="hasActiveFilters" class="mt-4 flex items-center space-x-2">
          <span class="text-xs text-slate-500">Filtros activos:</span>
          <div class="flex flex-wrap gap-2">
            <span v-if="filter.name" 
                  class="inline-flex items-center space-x-1 px-2 py-1 bg-slate-100 text-slate-700 text-xs rounded-full">
              <span>Nombre: "{{ filter.name }}"</span>
              <button @click="filter.name = ''" class="hover:text-slate-900">
                <X class="w-3 h-3" />
              </button>
            </span>
            <span v-if="filter.level" 
                  class="inline-flex items-center space-x-1 px-2 py-1 bg-slate-100 text-slate-700 text-xs rounded-full">
              <span>Nivel: {{ formatLevel(filter.level) }}</span>
              <button @click="filter.level = ''" class="hover:text-slate-900">
                <X class="w-3 h-3" />
              </button>
            </span>
            <span v-if="filter.grade" 
                  class="inline-flex items-center space-x-1 px-2 py-1 bg-slate-100 text-slate-700 text-xs rounded-full">
              <span>Grado: {{ formatGrade(filter.grade) }}</span>
              <button @click="filter.grade = ''" class="hover:text-slate-900">
                <X class="w-3 h-3" />
              </button>
            </span>
            <span v-if="filter.section" 
                  class="inline-flex items-center space-x-1 px-2 py-1 bg-slate-100 text-slate-700 text-xs rounded-full">
              <span>Sección: {{ filter.section }}</span>
              <button @click="filter.section = ''" class="hover:text-slate-900">
                <X class="w-3 h-3" />
              </button>
            </span>
            <button @click="clearAllFilters" 
                    class="text-xs text-slate-500 hover:text-slate-700 underline">
              Limpiar todo
            </button>
          </div>
        </div>
      </div>

      <!-- Loading State -->
      <transition name="fade">
        <div v-if="loading" class="flex flex-col items-center justify-center py-12">
          <Loader2 class="w-8 h-8 text-slate-400 animate-spin mb-3" />
          <p class="text-sm text-slate-500">Cargando estudiantes...</p>
        </div>
      </transition>

      <!-- Empty State -->
      <transition name="fade">
        <div v-if="!loading && totalStudents === 0" 
             class="bg-white rounded-xl border border-slate-200 p-12 text-center animate-fade-in-up">
          <GraduationCap class="w-16 h-16 text-slate-300 mx-auto mb-4" />
          <h3 class="text-lg font-semibold text-slate-800 mb-2">No hay estudiantes</h3>
          <p class="text-sm text-slate-500 mb-6">
            {{ hasActiveFilters ? 'No se encontraron estudiantes con los filtros aplicados' : 'Comienza agregando tu primer estudiante' }}
          </p>
          <button v-if="hasActiveFilters" 
                  @click="clearAllFilters"
                  class="inline-flex items-center space-x-2 text-slate-600 hover:text-slate-800">
            <X class="w-4 h-4" />
            <span>Limpiar filtros</span>
          </button>
          <button v-else
                  class="bg-slate-800 text-white px-6 py-2 rounded-lg hover:bg-slate-700 transition-all duration-300">
            Agregar Estudiante
          </button>
        </div>
      </transition>

      <transition name="fade">
        <div v-if="errorMessage" class="mb-6 bg-red-50 border border-red-200 rounded-xl p-4 flex items-start space-x-3 animate-shake">
          <AlertCircle class="w-5 h-5 text-red-500 shrink-0 mt-0.5" />
          <div class="flex-1">
            <h3 class="text-sm font-medium text-red-800">Error</h3>
            <p class="text-xs text-red-600 mt-1">{{ errorMessage }}</p>
          </div>
        </div>
      </transition>

      <!-- Tabla de estudiantes -->
      <transition name="fade">
        <div v-if="!loading && totalStudents > 0" class="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden animate-fade-in-up">
          <div class="overflow-x-auto">
            <table class="w-full">
              <thead>
                <tr class="bg-slate-50 border-b border-slate-200">
                  <th v-for="column in columns" :key="column.key"
                      class="px-6 py-4 text-left text-xs font-semibold text-slate-600 uppercase tracking-wider">
                    <button @click="toggleSort(column.key)"
                            class="flex items-center space-x-1 hover:text-slate-800 group">
                      <span>{{ column.label }}</span>
                      <span class="text-slate-400 group-hover:text-slate-600">
                        <MoveVerticalIcon v-if="sort.field !== column.key" class="w-3 h-3" />
                        <MoveUpIcon v-else-if="sort.field === column.key && sort.direction === 'asc'" class="w-3 h-3" />
                        <MoveDownIcon v-else-if="sort.field === column.key && sort.direction === 'desc'" class="w-3 h-3" />
                      </span>
                    </button>
                  </th>
                  <th class="px-6 py-4 text-left text-xs font-semibold text-slate-600 uppercase tracking-wider">
                    Acciones
                  </th>
                </tr>
              </thead>
              <tbody class="divide-y divide-slate-200">
                <tr v-for="student in students" 
                    :key="student.dni"
                    class="hover:bg-slate-50 transition-colors duration-200 group">
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span class="text-sm font-mono text-slate-600">{{ student.dni }}</span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span class="text-sm font-medium text-slate-800">{{ student.name }}</span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-600">{{ student.firstLastName }}</td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-600">{{ student.secondLastName }}</td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span :class="getLevelBadgeClass(student.level)" 
                          class="px-2 py-1 text-xs rounded-full">
                      {{ formatLevel(student.level) }}
                    </span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span class="text-sm text-slate-600">{{ formatGrade(student.grade) }}</span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span class="px-2 py-1 bg-slate-100 text-slate-700 text-xs rounded-full">
                      Sección {{ student.section }}
                    </span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <div class="flex items-center space-x-2 opacity-0 group-hover:opacity-100 transition-opacity duration-200">
                      <button @click="editStudent(student)"
                              class="p-2 text-slate-600 hover:bg-slate-100 rounded-lg transition-all duration-200 hover:scale-110"
                              title="Editar estudiante">
                        <Edit class="w-4 h-4" />
                      </button>
                      <button @click="confirmDelete(student)"
                              class="p-2 text-red-600 hover:bg-red-50 rounded-lg transition-all duration-200 hover:scale-110"
                              title="Eliminar estudiante">
                        <Trash2 class="w-4 h-4" />
                      </button>
                      <button @click="viewStudentDetails(student)"
                              class="p-2 text-slate-600 hover:bg-slate-100 rounded-lg transition-all duration-200 hover:scale-110"
                              title="Ver detalles">
                        <Eye class="w-4 h-4" />
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- Paginación -->
          <div class="bg-white px-6 py-4 border-t border-slate-200 flex flex-col sm:flex-row sm:items-center sm:justify-between">
            <div class="text-xs text-slate-500 mb-4 sm:mb-0">
              Mostrando {{ paginationInfo }}
            </div>
            
            <div class="flex items-center justify-between sm:justify-end space-x-4">
              <div class="text-sm text-slate-600">
                Página <span class="font-semibold">{{ currentPage + 1 }}</span> de {{ totalPages }}
              </div>
              
              <div class="flex space-x-2">
                <button @click="goToFirstPage"
                        :disabled="currentPage === 0 || loading"
                        class="p-2 border border-slate-200 rounded-lg text-slate-600 hover:bg-slate-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                        title="Primera página">
                  <ChevronsLeft class="w-4 h-4" />
                </button>
                <button @click="prevPage"
                        :disabled="currentPage === 0 || loading"
                        class="p-2 border border-slate-200 rounded-lg text-slate-600 hover:bg-slate-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                        title="Página anterior">
                  <ChevronLeft class="w-4 h-4" />
                </button>
                <button @click="nextPage"
                        :disabled="currentPage >= totalPages - 1 || loading"
                        class="p-2 border border-slate-200 rounded-lg text-slate-600 hover:bg-slate-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                        title="Página siguiente">
                  <ChevronRight class="w-4 h-4" />
                </button>
                <button @click="goToLastPage"
                        :disabled="currentPage >= totalPages - 1 || loading"
                        class="p-2 border border-slate-200 rounded-lg text-slate-600 hover:bg-slate-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                        title="Última página">
                  <ChevronsRight class="w-4 h-4" />
                </button>
              </div>
            </div>
          </div>
        </div>
      </transition>
    </div>

    <!-- Modal de confirmación para eliminar -->
    <transition name="fade">
      <div v-if="showDeleteModal" 
           class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4 z-50"
           @click.self="showDeleteModal = false">
        <div class="bg-white rounded-2xl max-w-md w-full p-6 animate-slide-up">
          <div class="flex items-center justify-between mb-4">
            <div class="flex items-center space-x-3">
              <div class="w-10 h-10 bg-red-100 rounded-lg flex items-center justify-center">
                <AlertTriangle class="w-5 h-5 text-red-600" />
              </div>
              <h3 class="text-lg font-semibold text-slate-800">Confirmar eliminación</h3>
            </div>
            <button @click="showDeleteModal = false" 
                    class="p-2 hover:bg-slate-100 rounded-lg transition-colors">
              <X class="w-5 h-5 text-slate-500" />
            </button>
          </div>
          
          <p class="text-sm text-slate-600 mb-6">
            ¿Estás seguro de que deseas eliminar al estudiante 
            <span class="font-semibold">{{ selectedStudent?.name }} {{ selectedStudent?.firstLastName }}</span>?
            Esta acción no se puede deshacer.
          </p>
          
          <div class="flex justify-end space-x-3">
            <button @click="showDeleteModal = false"
                    class="px-4 py-2 text-sm text-slate-600 hover:bg-slate-100 rounded-lg transition-colors">
              Cancelar
            </button>
            <button @click="executeDelete"
                    class="px-4 py-2 bg-red-600 text-white text-sm rounded-lg hover:bg-red-700 transition-colors">
              Eliminar
            </button>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import Header from '@/components/Header.vue'
import { getStudents, removeStudent } from '@/services/studentsService'
import type { StudentFilter, StudentResponse } from '@/types/Student'
import type { PageRequest, Sort } from '@/types/Pages'
import {
  GraduationCap,
  Plus,
  Search,
  BarChart3,
  Layers,
  Grid3x3,
  Loader2,
  X,
  Edit,
  Trash2,
  Eye,
  ChevronLeft,
  ChevronRight,
  ChevronsLeft,
  ChevronsRight,
  AlertTriangle,
  AlertCircle,
  MoveVerticalIcon,
  MoveUpIcon,
  MoveDownIcon,
  Filter,
  RotateCw
} from 'lucide-vue-next'
import router from '@/router'
import { useToast } from '@/composables/useToast'

// Constantes
const PAGE_SIZE = 15

// Definición de columnas
const columns = [
  { key: 'dni', label: 'DNI' },
  { key: 'name', label: 'Nombre' },
  { key: 'firstLastName', label: 'Apellido Paterno' },
  { key: 'secondLastName', label: 'Apellido Materno' },
  { key: 'level', label: 'Nivel' },
  { key: 'grade', label: 'Grado' },
  { key: 'section', label: 'Sección' }
] as const

// Estado
const totalStudents = ref()
const totalPages = ref(0)
const currentPage = ref(0)
const students = ref<StudentResponse[]>([])
const loading = ref(false)
const errorMessage = ref('')
const showDeleteModal = ref(false)
const deleteSuccess = ref(false)
const selectedStudent = ref<StudentResponse | null>(null)

const filter = ref<StudentFilter>({
  name: '',
  level: '',
  grade: '',
  section: ''
})

const sort = ref<Sort>({
  field: 'firstLastName',
  direction: 'asc'
})

// Inicializar toast
const toast = useToast()

// Computed properties
const hasActiveFilters = computed(() => {
  return Object.values(filter.value).some(value => value !== '')
})

const paginationInfo = computed(() => {
  const start = currentPage.value * PAGE_SIZE + 1
  const end = Math.min((currentPage.value + 1) * PAGE_SIZE, totalStudents.value)
  return `${start} - ${end} de ${totalStudents.value} estudiantes`
})

// Funciones de utilidad
const formatLevel = (level: string) => {
  return level === 'PRIMARIA' ? 'Primaria' : 'Secundaria'
}

const formatGrade = (grade: string) => {
  const grades: Record<string, string> = {
    'PRIMERO': '1°',
    'SEGUNDO': '2°',
    'TERCERO': '3°',
    'CUARTO': '4°',
    'QUINTO': '5°',
    'SEXTO': '6°'
  }
  return grades[grade] || grade
}

const getLevelBadgeClass = (level: string) => {
  return level === 'PRIMARIA' 
    ? 'bg-blue-50 text-blue-700' 
    : 'bg-purple-50 text-purple-700'
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
  loadStudents()
}

// Funciones de filtro
const applyFilters = () => {
  currentPage.value = 0
  loadStudents()
  toast.showInfo('Filtros aplicados', 'Los filtros se han aplicado correctamente', 3000)
}

const clearAllFilters = () => {
  filter.value = {
    name: '',
    level: '',
    grade: '',
    section: ''
  }
  currentPage.value = 0
  loadStudents()
  toast.showInfo('Filtros limpiados', 'Se han eliminado todos los filtros', 3000)
}

const refreshTable = () => {
  loadStudents()
  toast.showInfo('Actualizando', 'Recargando datos de la tabla...', 3000)
}

// Funciones de paginación
const goToFirstPage = () => {
  if (currentPage.value > 0) {
    currentPage.value = 0
    loadStudents()
  }
}

const goToLastPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value = totalPages.value - 1
    loadStudents()
  }
}

const prevPage = () => {
  if (currentPage.value > 0) {
    currentPage.value -= 1
    loadStudents()
  }
}

const nextPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value += 1
    loadStudents()
  }
}

// Acciones de estudiantes
const editStudent = (student: StudentResponse) => {
  console.log('Editar:', student)
  // Implementar navegación a edición
}

const confirmDelete = (student: StudentResponse) => {
  selectedStudent.value = student
  showDeleteModal.value = true
}

const executeDelete = async () => {
  if (!selectedStudent.value) return

  showDeleteModal.value = false
  loading.value = true
  errorMessage.value = ''
  try {
    const response = await removeStudent(selectedStudent.value.dni)

    if (response.success) {
      toast.showSuccess('Estudiante eliminado', `El estudiante ${selectedStudent.value?.name} ha sido eliminado correctamente`)
      await loadStudents()
    } else {
      errorMessage.value = response.error.message
      toast.showError('Error al eliminar', errorMessage.value)
    }

    deleteSuccess.value = true
    await loadStudents()
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Error de conexión'
    toast.showError('Error de conexión', error instanceof Error ? error.message : 'Error desconocido')
  } finally {
    loading.value = false
    selectedStudent.value = null
  }
}

const viewStudentDetails = (student: StudentResponse) => {
  router.push(`/students/${student.dni}`)
}

// Carga de datos
const buildPayloadFilter = (): StudentFilter => ({
  name: filter.value.name,
  level: filter.value.level,
  grade: filter.value.grade,
  section: filter.value.section
})

const buildPayloadPage = (): PageRequest => ({
  page: currentPage.value,
  size: PAGE_SIZE
})

const buildPayloadSort = (): Sort => ({
  field: sort.value.field,
  direction: sort.value.direction
})

const loadStudents = async () => {
  loading.value = true
  errorMessage.value = ''
  try {
    const response = await getStudents(
      buildPayloadFilter(),
      buildPayloadPage(),
      buildPayloadSort()
    )

    if (response.success) {
      totalStudents.value = Number(response.data.totalElements)
      totalPages.value = response.data.totalPages
      currentPage.value = response.data.page
      students.value = response.data.content
    } else {
      errorMessage.value = response.error.message
    }
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Error de conexión'
  } finally {
    loading.value = false
  }
}

// Lifecycle
onMounted(loadStudents)

const goToNewStudent = () => {
  router.push('/addStudent')
}
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
  animation: fadeInUp 0.6s ease-out;
}

.animate-slide-up {
  animation: slideUp 0.4s ease-out;
}

/* Transiciones suaves */
* {
  transition-property: background-color, border-color, color, fill, stroke, opacity, box-shadow, transform;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 200ms;
}

/* Estilos para scrollbar */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f5f9;
}

::-webkit-scrollbar-thumb {
  background: #94a3b8;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #64748b;
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