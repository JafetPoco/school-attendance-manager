<template>
  <div class="min-h-screen bg-slate-50">
    <Header />
    
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Header de la página -->
      <div class="mb-8 animate-fade-in-down">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between">
          <div class="flex items-center space-x-3 mb-4 sm:mb-0">
            <div class="w-12 h-12 bg-slate-800 rounded-xl flex items-center justify-center shadow-lg">
              <BookOpen class="w-6 h-6 text-white" />
            </div>
            <div>
              <h1 class="text-2xl font-bold text-slate-800">Clases</h1>
              <p class="text-sm text-slate-500">Gestiona los cursos y secciones del colegio</p>
            </div>
          </div>
          
          <!-- Botón crear clase -->
          <button @click="openCreateModal"
                  class="inline-flex items-center space-x-2 px-4 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 transition-all duration-300 transform hover:scale-105 shadow-md">
            <Plus class="w-4 h-4" />
            <span>Nueva Clase</span>
          </button>
        </div>
      </div>

      <!-- Estados de carga y error -->
      <transition name="fade">
        <div v-if="loading" class="flex flex-col items-center justify-center py-12 bg-white rounded-xl border border-slate-200">
          <Loader2 class="w-8 h-8 text-slate-400 animate-spin mb-3" />
          <p class="text-sm text-slate-500">Cargando clases...</p>
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
          <button @click="getAllClasses" class="text-red-400 hover:text-red-600">
            <RotateCw class="w-4 h-4" />
          </button>
        </div>
      </transition>

      <!-- Grid de tarjetas -->
      <div v-if="!loading && !errorMessage" class="animate-fade-in-up">
        <!-- Estadísticas -->
        <div class="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6">
          <div class="bg-white rounded-xl border border-slate-200 p-4 shadow-sm">
            <div class="flex items-center justify-between">
              <div>
                <p class="text-xs text-slate-500">Total Clases</p>
                <p class="text-2xl font-bold text-slate-800">{{ allClasses.length }}</p>
              </div>
              <div class="w-10 h-10 bg-slate-100 rounded-lg flex items-center justify-center">
                <BookOpen class="w-5 h-5 text-slate-600" />
              </div>
            </div>
          </div>
          
          <div class="bg-white rounded-xl border border-slate-200 p-4 shadow-sm">
            <div class="flex items-center justify-between">
              <div>
                <p class="text-xs text-slate-500">Primaria</p>
                <p class="text-2xl font-bold text-blue-600">{{ getCountByLevel('PRIMARIA') }}</p>
              </div>
              <div class="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center">
                <GraduationCap class="w-5 h-5 text-blue-600" />
              </div>
            </div>
          </div>
          
          <div class="bg-white rounded-xl border border-slate-200 p-4 shadow-sm">
            <div class="flex items-center justify-between">
              <div>
                <p class="text-xs text-slate-500">Secundaria</p>
                <p class="text-2xl font-bold text-purple-600">{{ getCountByLevel('SECUNDARIA') }}</p>
              </div>
              <div class="w-10 h-10 bg-purple-100 rounded-lg flex items-center justify-center">
                <GraduationCap class="w-5 h-5 text-purple-600" />
              </div>
            </div>
          </div>
        </div>

        <!-- Grid de tarjetas de clases -->
        <div v-if="allClasses.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div v-for="clase in allClasses" 
               :key="clase.id"
               class="group bg-white rounded-xl border border-slate-200 shadow-sm hover:shadow-lg transition-all duration-300 overflow-hidden hover:scale-[1.02]">
            
            <!-- Cabecera de la tarjeta con gradiente según nivel -->
            <div :class="getHeaderGradientClass(clase.level)" class="px-5 py-4">
              <div class="flex items-center justify-between">
                <div class="flex items-center space-x-3">
                  <div class="w-10 h-10 bg-white/10 rounded-xl flex items-center justify-center backdrop-blur-sm">
                    <GraduationCap class="w-5 h-5 text-white" />
                  </div>
                  <div>
                    <h3 class="text-white font-semibold text-lg">{{ formatLevel(clase.level) }}</h3>
                  </div>
                </div>
                <button @click="openEditModal(clase)"
                        class="p-2 bg-white/10 rounded-lg text-white hover:bg-white/20 transition-all duration-200 hover:scale-110"
                        title="Editar clase">
                  <Edit class="w-4 h-4" />
                </button>
              </div>
            </div>

            <!-- Contenido de la tarjeta -->
            <div class="p-5 space-y-4">
              <!-- Grado -->
              <div class="flex items-center justify-between">
                <div class="flex items-center space-x-2">
                  <div class="w-8 h-8 bg-slate-100 rounded-lg flex items-center justify-center">
                    <Layers class="w-4 h-4 text-slate-500" />
                  </div>
                  <div>
                    <p class="text-xs text-slate-500">Grado</p>
                    <p class="text-base font-semibold text-slate-800">{{ formatGrade(clase.grade) }}</p>
                  </div>
                </div>
              </div>

              <!-- Sección -->
              <div class="flex items-center justify-between">
                <div class="flex items-center space-x-2">
                  <div class="w-8 h-8 bg-slate-100 rounded-lg flex items-center justify-center">
                    <Grid3x3 class="w-4 h-4 text-slate-500" />
                  </div>
                  <div>
                    <p class="text-xs text-slate-500">Sección</p>
                    <p class="text-base font-semibold text-slate-800">{{ clase.section }}</p>
                  </div>
                </div>
                
                <!-- Badge de nivel -->
                <span :class="getLevelBadgeClass(clase.level)" 
                      class="px-2 py-1 text-xs rounded-full">
                  {{ formatLevel(clase.level) }}
                </span>
              </div>

              <!-- Información adicional -->
              <div class="pt-3 border-t border-slate-100">
                <div class="flex items-center justify-between text-xs text-slate-400">
                  <div class="flex items-center space-x-1">
                    <Users class="w-3 h-3" />
                    <span>Activa</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Empty state -->
        <div v-else class="bg-white rounded-xl border border-slate-200 p-12 text-center">
          <div class="flex flex-col items-center">
            <BookOpen class="w-16 h-16 text-slate-300 mb-4" />
            <h3 class="text-lg font-semibold text-slate-800 mb-2">No hay clases registradas</h3>
            <p class="text-sm text-slate-500 mb-4">Comienza creando tu primera clase</p>
            <button @click="openCreateModal"
                    class="inline-flex items-center space-x-2 px-4 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 transition-all duration-300">
              <Plus class="w-4 h-4" />
              <span>Crear primera clase</span>
            </button>
          </div>
        </div>
      </div>
    </main>

    <!-- Modal Crear/Editar Clase -->
    <transition name="fade">
      <div v-if="showModal" 
           class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4 z-50"
           @click.self="closeModal">
        <div class="bg-white rounded-2xl max-w-md w-full p-6 animate-slide-up">
          <div class="flex items-center justify-between mb-4">
            <div class="flex items-center space-x-3">
              <div class="w-10 h-10 bg-amber-100 rounded-lg flex items-center justify-center">
                <PenBox class="w-5 h-5 text-amber-600" />
              </div>
              <h3 class="text-lg font-semibold text-slate-800">
                {{ isEditing ? 'Editar Clase' : 'Nueva Clase' }}
              </h3>
            </div>
            <button @click="closeModal" 
                    class="p-2 hover:bg-slate-100 rounded-lg transition-colors">
              <X class="w-5 h-5 text-slate-500" />
            </button>
          </div>

          <form @submit.prevent="submitClass" class="space-y-5">
            <!-- Nivel -->
            <div class="space-y-1.5">
              <label for="level" class="block text-sm font-medium text-slate-700">
                Nivel <span class="text-red-500">*</span>
              </label>
              <div class="relative">
                <GraduationCap class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
                <select v-model="formData.level" id="level"
                        class="w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent appearance-none cursor-pointer">
                  <option value="">Selecciona un nivel</option>
                  <option value="PRIMARIA">Primaria</option>
                  <option value="SECUNDARIA">Secundaria</option>
                </select>
              </div>
            </div>

            <!-- Grado -->
            <div class="space-y-1.5">
              <label for="grade" class="block text-sm font-medium text-slate-700">
                Grado <span class="text-red-500">*</span>
              </label>
              <div class="relative">
                <Layers class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
                <select v-model="formData.grade" id="grade"
                        class="w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent appearance-none cursor-pointer">
                  <option value="">Selecciona un grado</option>
                  <option value="PRIMERO">Primero</option>
                  <option value="SEGUNDO">Segundo</option>
                  <option value="TERCERO">Tercero</option>
                  <option value="CUARTO">Cuarto</option>
                  <option value="QUINTO">Quinto</option>
                  <option value="SEXTO">Sexto</option>
                </select>
              </div>
            </div>

            <!-- Sección -->
            <div class="space-y-1.5">
              <label for="section" class="block text-sm font-medium text-slate-700">
                Sección <span class="text-red-500">*</span>
              </label>
              <div class="relative">
                <Grid3x3 class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
                <input type="text" id="section"
                       v-model="formData.section"
                       maxlength="1"
                       class="w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent"
                       :class="errors.section ? 'border-red-300 bg-red-50' : 'hover:border-slate-300'"
                       placeholder="Ej: A, B, Benjamin...">
              </div>
              <p v-if="errors.section" class="text-xs text-red-500 flex items-center">
                <AlertCircle class="w-3 h-3 mr-1" />
                {{ errors.section }}
              </p>
              <p class="text-xs text-slate-400">Ingresa nombre de la sección</p>
            </div>

            <!-- Vista previa de la clase -->
            <div v-if="formData.level && formData.grade && formData.section" 
                 class="bg-slate-50 rounded-lg p-3">
              <p class="text-xs text-slate-500 mb-1">Vista previa:</p>
              <p class="text-sm font-medium text-slate-800">
                {{ formatLevel(formData.level) }} - 
                {{ formatGrade(formData.grade) }} - 
                Sección {{ formData.section.toUpperCase() }}
              </p>
            </div>

            <!-- Botones -->
            <div class="flex items-center justify-end space-x-3 pt-4">
              <button type="button"
                      @click="closeModal"
                      class="px-4 py-2 text-sm text-slate-600 hover:bg-slate-100 rounded-lg transition-colors">
                Cancelar
              </button>
              <button type="submit"
                      :disabled="submitting"
                      class="relative px-6 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 transition-all duration-300 transform hover:scale-105 shadow-md disabled:opacity-50 disabled:cursor-not-allowed flex items-center space-x-2">
                <Loader2 v-if="submitting" class="w-4 h-4 animate-spin" />
                <Save v-else class="w-4 h-4" />
                <span>{{ submitting ? 'Guardando...' : (isEditing ? 'Actualizar' : 'Crear Clase') }}</span>
              </button>
            </div>
          </form>
        </div>
      </div>
    </transition>

    <!-- Sistema de Toasts -->
    <transition-group name="toast" tag="div" class="fixed bottom-4 right-4 z-50 space-y-2">
      <div v-for="toast in toasts" 
           :key="toast.id"
           :class="[
             'flex items-center space-x-3 px-4 py-3 rounded-lg shadow-lg min-w-75 max-w-md animate-slide-in-right',
             toast.type === 'success' ? 'bg-emerald-600 text-white' : '',
             toast.type === 'error' ? 'bg-red-600 text-white' : '',
             toast.type === 'info' ? 'bg-blue-600 text-white' : ''
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
import { ref, reactive, onMounted } from 'vue'
import Header from '@/components/Header.vue'
import { createClass, editClass, getClassesFullInfo } from '@/services/classService'
import type { ClassFullInfoResponse, ClassRequest } from '@/types/Class'
import {
  BookOpen,
  Plus,
  Loader2,
  AlertCircle,
  RotateCw,
  Edit,
  PenBox,
  X,
  CheckCircle,
  XCircle,
  Info,
  GraduationCap,
  Layers,
  Grid3x3,
  Save,
  Users
} from 'lucide-vue-next'

// Interfaces
interface ToastMessage {
  id: number
  type: 'success' | 'error' | 'info'
  title: string
  message: string
  icon: any
}

// Estado
const loading = ref(false)
const submitting = ref(false)
const errorMessage = ref('')
const allClasses = ref<ClassFullInfoResponse[]>([])
const showModal = ref(false)
const isEditing = ref(false)
const editingId = ref<number | null>(null)
const toasts = ref<ToastMessage[]>([])
let nextToastId = 1

const formData = reactive<ClassRequest>({
  level: '',
  grade: '',
  section: ''
})

const errors = reactive({
  section: ''
})

// Funciones de Toast
const addToast = (type: ToastMessage['type'], title: string, message: string, duration: number = 3000) => {
  const icon = type === 'success' ? CheckCircle : type === 'error' ? XCircle : Info
  const toast: ToastMessage = {
    id: nextToastId++,
    type,
    title,
    message,
    icon
  }
  toasts.value.push(toast)
  setTimeout(() => removeToast(toast.id), duration)
}

const removeToast = (id: number) => {
  toasts.value = toasts.value.filter(t => t.id !== id)
}

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

const getHeaderGradientClass = (level: string) => {
  return level === 'PRIMARIA' 
    ? 'bg-gradient-to-r from-blue-600 to-blue-500' 
    : 'bg-gradient-to-r from-purple-600 to-purple-500'
}

const getLevelBadgeClass = (level: string) => {
  return level === 'PRIMARIA' 
    ? 'bg-blue-50 text-blue-700' 
    : 'bg-purple-50 text-purple-700'
}

const getCountByLevel = (level: string) => {
  return allClasses.value.filter(c => c.level === level).length
}

// Validaciones
const validateSection = () => {
  const section = formData.section.trim().toUpperCase()
  if (!section) {
    errors.section = 'La sección es obligatoria'
    return false
  }
  if (!/^[A-Z]$/.test(section)) {
    errors.section = 'La sección debe ser una sola letra mayúscula'
    return false
  }
  errors.section = ''
  formData.section = section
  return true
}

// Acciones de modal
const openCreateModal = () => {
  resetForm()
  isEditing.value = false
  editingId.value = null
  showModal.value = true
}

const openEditModal = (clase: ClassFullInfoResponse) => {
  formData.level = clase.level
  formData.grade = clase.grade
  formData.section = clase.section
  isEditing.value = true
  editingId.value = clase.id
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  resetForm()
}

const resetForm = () => {
  formData.level = ''
  formData.grade = ''
  formData.section = ''
  errors.section = ''
}

// Envío de formulario
const submitClass = async () => {
  if (!validateSection()) return
  if (!formData.level || !formData.grade) {
    addToast('error', 'Campos incompletos', 'Por favor completa todos los campos')
    return
  }

  submitting.value = true

  try {
    let response
    if (isEditing.value && editingId.value) {
      response = await editClass(editingId.value, formData)
    } else {
      response = await createClass(formData)
    }

    if (response.success) {
      addToast('success', isEditing.value ? 'Clase actualizada' : 'Clase creada', 
               isEditing.value ? 'La clase ha sido actualizada correctamente' : 'La clase ha sido creada correctamente')
      await getAllClasses()
      closeModal()
    } else {
      addToast('error', 'Error', response.error.message)
    }
  } catch (error) {
    addToast('error', 'Error de conexión', error instanceof Error ? error.message : 'Error desconocido')
  } finally {
    submitting.value = false
  }
}

// Carga de datos
const getAllClasses = async () => {
  loading.value = true
  errorMessage.value = ''
  
  try {
    const response = await getClassesFullInfo()
    if (response.success) {
      allClasses.value = response.data
    } else {
      errorMessage.value = response.error.message
    }
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Error de conexión'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  getAllClasses()
})
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

.animate-slide-up {
  animation: slideUp 0.3s ease-out;
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