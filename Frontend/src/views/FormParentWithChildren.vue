<template>
  <div class="min-h-screen bg-slate-50 py-8 px-4 sm:px-6 lg:px-8">
    <div class="max-w-4xl mx-auto">
      <!-- Header del formulario -->
      <div class="mb-8 animate-fade-in-down">
        <div class="flex items-center space-x-3 mb-4">
          <div class="w-10 h-10 bg-slate-800 rounded-xl flex items-center justify-center shadow-md">
            <Users class="w-5 h-5 text-white" />
          </div>
          <div>
            <h1 class="text-2xl font-bold text-slate-800">Registro de Apoderado</h1>
            <p class="text-sm text-slate-500">Completa los datos del padre/madre y sus hijos</p>
          </div>
        </div>
        
        <!-- Barra de progreso -->
        <div class="w-full h-1 bg-slate-200 rounded-full overflow-hidden">
          <div class="h-full bg-slate-800 rounded-full transition-all duration-500"
               :style="{ width: formProgress + '%' }"></div>
        </div>
      </div>

      <!-- Formulario principal -->
      <form @submit.prevent="onSubmit" class="space-y-6">
        <!-- Datos del padre -->
        <div class="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden">
          <div class="bg-slate-50 px-6 py-4 border-b border-slate-200">
            <h2 class="text-lg font-semibold text-slate-800 flex items-center">
              <User class="w-5 h-5 mr-2 text-slate-600" />
              Datos del Apoderado
            </h2>
          </div>
          
          <div class="p-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <!-- Nombre -->
              <div class="space-y-1">
                <label for="name" class="block text-sm font-medium text-slate-700">
                  Nombre completo <span class="text-red-500">*</span>
                </label>
                <div class="relative">
                  <input type="text" 
                         id="name"
                         v-model="parentForm.names"
                         @blur="validateParent('parentNames')"
                         class="w-full px-4 py-2.5 bg-white border rounded-lg transition-all duration-200 focus:ring-2 focus:ring-slate-800 focus:border-transparent"
                         :class="[parentError.names ? 'border-red-300 bg-red-50' : 'border-slate-200 hover:border-slate-300']"
                         placeholder="Ej: Juan Carlos Pérez">
                  <Check v-if="!parentError.names && parentError.names != ''" class="absolute right-3 top-3 w-5 h-5 text-emerald-500"/>
                </div>
                <p v-if="parentError.names" class="text-xs text-red-500 flex items-center">
                  <AlertCircle class="w-3 h-3 mr-1" />
                  {{ parentError.names }}
                </p>
              </div>

              <!-- Teléfono -->
              <div class="space-y-1">
                <label for="phone" class="block text-sm font-medium text-slate-700">
                  Número de celular <span class="text-red-500">*</span>
                </label>
                <div class="relative">
                  <input type="tel"
                         id="phone"
                         v-model="parentForm.phoneNumber"
                         @blur="validateParent('parentPhone')"
                         class="w-full px-4 py-2.5 bg-white border rounded-lg transition-all duration-200 focus:ring-2 focus:ring-slate-800 focus:border-transparent"
                         :class="[parentError.phoneNumber ? 'border-red-300 bg-red-50' : 'border-slate-200 hover:border-slate-300']"
                         placeholder="Ej: 987654321">
                  <Check v-if="!parentError.phoneNumber && parentError.phoneNumber != ''" class="absolute right-3 top-3 w-5 h-5 text-emerald-500"/>
                </div>
                <p v-if="parentError.phoneNumber" class="text-xs text-red-500 flex items-center">
                  <AlertCircle class="w-3 h-3 mr-1" />
                  {{ parentError.phoneNumber }}
                </p>
              </div>
            </div>
          </div>
        </div>

        <!-- Sección de hijos -->
        <div class="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden">
          <div class="bg-slate-50 px-6 py-4 border-b border-slate-200">
            <div class="flex items-center justify-between">
              <h2 class="text-lg font-semibold text-slate-800 flex items-center">
                <Users class="w-5 h-5 mr-2 text-slate-600" />
                Datos de los Hijos
                <span class="ml-2 px-2 py-0.5 bg-slate-200 text-slate-700 text-xs rounded-full">
                  {{ numChildren }}
                </span>
              </h2>
              
              <div class="flex space-x-2">
                <button type="button"
                        @click="addChild"
                        class="flex items-center space-x-1 px-3 py-1.5 bg-slate-800 text-white text-sm rounded-lg hover:bg-slate-700 transition-all duration-300 transform hover:scale-105">
                  <Plus class="w-4 h-4" />
                  <span>Agregar hijo</span>
                </button>
                <button type="button"
                        v-if="children.length > 0"
                        @click="removeChild"
                        class="flex items-center space-x-1 px-3 py-1.5 border border-slate-200 text-slate-600 text-sm rounded-lg hover:bg-slate-50 hover:border-slate-300 transition-all duration-300">
                  <Minus class="w-4 h-4" />
                  <span>Quitar último</span>
                </button>
              </div>
            </div>
          </div>

          <div class="p-6 space-y-6">
            <transition-group name="list" tag="div" class="space-y-6">
              <ChildFormComponent
                v-for="(child, index) in children"
                :key="index"
                :child="child"
                :index="index"
                :errors="childrenErrors[index]"
                @validate="() => validateChild(index)"
                @update:child="children[index] = $event"
                @remove="removeChildAtIndex(index)"
                @add-sibling="addChild"
              />
            </transition-group>

            <!-- Estado vacío -->
            <div v-if="children.length === 0" 
                 class="text-center py-12 border-2 border-dashed border-slate-200 rounded-xl">
              <Users class="w-12 h-12 text-slate-300 mx-auto mb-3" />
              <p class="text-sm text-slate-500">No hay hijos registrados</p>
              <button type="button"
                      @click="addChild"
                      class="mt-4 inline-flex items-center space-x-1 text-slate-600 hover:text-slate-800 text-sm font-medium">
                <Plus class="w-4 h-4" />
                <span>Agregar primer hijo</span>
              </button>
            </div>
          </div>
        </div>

        <!-- Mensajes de respuesta -->
        <transition name="fade">
          <div v-if="errorMessage" 
              class="mb-6 bg-red-50 border border-red-200 rounded-xl p-4 flex items-start space-x-3 animate-shake">
            <AlertCircle class="w-5 h-5 text-red-500 shrink-0 mt-0.5" />
            <div class="flex-1">
              <h3 class="text-sm font-medium text-red-800">Error en el registro</h3>
              <p class="text-xs text-red-600 mt-1">{{ errorMessage }}</p>
            </div>
            <button type="button" @click="errorMessage = ''" class="text-red-400 hover:text-red-600">
              <X class="w-4 h-4" />
            </button>
          </div>
        </transition>

        <transition name="fade">
          <div v-if="response" 
              class="mb-6 bg-emerald-50 border border-emerald-200 rounded-xl p-4 flex items-start space-x-3">
            <CheckCircle class="w-5 h-5 text-emerald-500 shrink-0 mt-0.5" />
            <div class="flex-1">
              <h3 class="text-sm font-medium text-emerald-800">¡Registro exitoso!</h3>
            </div>
            <button type="button" @click="response = null" class="text-emerald-400 hover:text-emerald-600">
              <X class="w-4 h-4" />
            </button>
          </div>
        </transition>

        <!-- Botones de acción -->
        <div class="flex items-center justify-end space-x-4 pt-4">
          <button type="button"
                  @click="resetForm"
                  class="px-6 py-2.5 border border-slate-200 text-slate-600 rounded-lg hover:bg-slate-50 hover:border-slate-300 transition-all duration-300">
            Cancelar
          </button>
          <button type="submit"
                   :disabled="loading || !isFormValid"
                  class="relative px-6 py-2.5 bg-slate-800 text-white rounded-lg hover:bg-slate-700 transition-all duration-300 transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:scale-100 overflow-hidden group">
            <span :class="{ 'opacity-0': loading }">Registrar Apoderado</span>
            <div v-if="loading" 
                 class="absolute inset-0 flex items-center justify-center">
              <Loader2 class="w-5 h-5 animate-spin" />
            </div>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import ChildFormComponent from '@/components/ChildFormComponent.vue'
import { addParentWithChildren } from '@/services/addParentWithChildren'
import type { ParentWithChildrenRequest } from '@/types/ParentWithChildren'
import type { StudentRequest } from '@/types/Student'
import type { ParentResponse } from '@/types/Parent'
import {
  Users,
  User,
  Check,
  Plus,
  Minus,
  AlertCircle,
  CheckCircle,
  X,
  Loader2
} from 'lucide-vue-next'

// Interfaces para validaciones
interface ParentErrors {
  names: string | null
  phoneNumber: string | null
}

interface ChildErrors {
  dni: string | null
  name: string | null
  firstLastName: string | null
  secondLastName: string | null
}

type ChildForm = StudentRequest

// Estado del formulario
const parentForm = ref({
  names: '',
  phoneNumber: ''
})
const children = ref<ChildForm[]>([])

//Errores y respuestas
const parentError = ref<ParentErrors>({
  names: '',
  phoneNumber: ''
})
const childrenErrors = ref<ChildErrors[]>([])

const loading = ref(false)
const errorMessage = ref('')
const response = ref<ParentResponse | null>(null)

// Validaciones
const validateParent = (field: 'parentNames' | 'parentPhone') => {
  switch (field) {
    case 'parentNames':
      if (!parentForm.value.names.trim()) {
        parentError.value.names = 'El nombre es obligatorio'
      } else if (parentForm.value.names.trim().length < 3) {
        parentError.value.names = 'El nombre debe tener al menos 3 caracteres'
      } else {
        parentError.value.names = null
      }
      break
      
    case 'parentPhone': {
      const phoneRegex = /^9\d{8}$/
      const phoneValue = parentForm.value.phoneNumber.trim()

      if (!phoneValue) {
        parentError.value.phoneNumber = 'El teléfono es obligatorio'
      } else if (phoneRegex.test(phoneValue)) {
        parentError.value.phoneNumber = null
      } else {
        parentError.value.phoneNumber = 'El teléfono debe empezar con 9 y tener 9 dígitos'
      }
      break
    }
  }
}

const validateChild = (index: number) => {
  const child = children.value[index]
  if (!child) {
    return false
  }

  const errors: ChildErrors = {
    dni: null,
    name: null,
    firstLastName: null,
    secondLastName: null
  }

  // Validar DNI
  if (!child.dni || child.dni === '') {
    errors.dni = 'El DNI es obligatorio'
  } else if (child.dni.toString().length !== 8) {
    errors.dni = 'El DNI debe tener 8 dígitos'
  }

  // Validar nombres
  if (!child.name.trim()) {
    errors.name = 'El nombre es obligatorio'
  }

  // Validar apellidos
  if (!child.firstLastName.trim()) {
    errors.firstLastName = 'El primer apellido es obligatorio'
  }
  if (!child.secondLastName.trim()) {
    errors.secondLastName = 'El segundo apellido es obligatorio'
  }

  childrenErrors.value[index] = errors
  return Object.values(errors).every(e => e === null)
}

// Progreso del formulario
const formProgress = computed(() => {
  let totalFields = 2 // padre: nombre y teléfono
  let completedFields = 0

  // Campos del padre
  if (parentForm.value.names.trim()) completedFields++
  if (parentForm.value.phoneNumber.trim()) completedFields++

  // Campos de hijos
  children.value.forEach(child => {
    totalFields += 5 // dni, nombre, primer apellido, segundo apellido, grado
    if (child.dni) completedFields++
    if (child.name.trim()) completedFields++
    if (child.firstLastName.trim()) completedFields++
    if (child.secondLastName.trim()) completedFields++
    if (child.grade) completedFields++
  })

  return Math.round((completedFields / totalFields) * 100)
})

// Funciones para manejar hijos
const createEmptyChild = (): ChildForm => ({
  dni: '',
  name: '',
  firstLastName: '',
  secondLastName: '',
  level: 'PRIMARIA',
  grade: 'PRIMERO',
  section: 'A'
})

const addChild = () => {
  children.value.push(createEmptyChild())
  childrenErrors.value.push({
    dni: null,
    name: null,
    firstLastName: null,
    secondLastName: null
  })
}

const removeChild = () => {
  if (children.value.length > 0) {
    children.value.pop()
    childrenErrors.value.pop()
  }
}

const removeChildAtIndex = (index: number) => {
  children.value.splice(index, 1)
  childrenErrors.value.splice(index, 1)
}

const numChildren = computed(() => children.value.length)

// Construir payload
const buildPayload = (): ParentWithChildrenRequest => ({
  names: parentForm.value.names.trim(),
  phoneNumber: parentForm.value.phoneNumber.trim(),
  children: children.value.map((child) => ({ 
    ...child,
    dni: child.dni
  }))
})

// Validación completa del formulario
const isFormValid = computed(() => {
  // Validar padre
  const parentValid = !parentError.value.names && !parentError.value.phoneNumber &&
                      parentForm.value.names.trim() && parentForm.value.phoneNumber.trim()

  // Validar hijos (si hay)
  let childrenValid = true
  if (children.value.length > 0) {
    children.value.forEach((_, index) => {
      if (!validateChild(index)) {
        childrenValid = false
      }
    })
  }

  return parentValid && childrenValid
})

// Submit del formulario
const onSubmit = async () => {

  validateParent('parentNames')
  validateParent('parentPhone')
  children.value.forEach((_, index) => validateChild(index))
  const payload = buildPayload()

  loading.value = true
  errorMessage.value = ''
  
  try {
    const result = await addParentWithChildren(payload)

    if (result.success) {
      response.value = result.data
      // Resetear formulario después de éxito
      setTimeout(() => {
        resetForm()
      }, 3000)
    } else {
      response.value = null
      errorMessage.value = result.error.message
    }
  } catch (error: unknown) {
    errorMessage.value = error instanceof Error ? error.message : 'Error de conexión'
  } finally {
    loading.value = false
  }
}

// Reset del formulario
const resetForm = () => {
  parentForm.value = { names: '', phoneNumber: '' }
  children.value = []
  response.value = null
  errorMessage.value = ''
}

// Watchers para validación en tiempo real
watch(() => parentForm.value.names, () => {
  if (parentError.value.names) validateParent('parentNames')
})

watch(() => parentForm.value.phoneNumber, () => {
  if (parentError.value.phoneNumber) validateParent('parentPhone')
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

.list-enter-active,
.list-leave-active {
  transition: all 0.5s ease;
}

.list-enter-from {
  opacity: 0;
  transform: translateX(30px);
}

.list-leave-to {
  opacity: 0;
  transform: translateX(-30px);
}

.list-move {
  transition: transform 0.5s ease;
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  10%, 30%, 50%, 70%, 90% { transform: translateX(-2px); }
  20%, 40%, 60%, 80% { transform: translateX(2px); }
}

.animate-shake {
  animation: shake 0.6s ease-in-out;
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

.animate-fade-in-down {
  animation: fadeInDown 0.6s ease-out;
}

/* Mejoras de accesibilidad */
@media (prefers-reduced-motion: reduce) {
  * {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
  }
}

/* Estilos para inputs */
input, select {
  outline: none;
}

input:focus, select:focus {
  box-shadow: 0 0 0 2px rgba(30, 41, 59, 0.1);
}
</style>