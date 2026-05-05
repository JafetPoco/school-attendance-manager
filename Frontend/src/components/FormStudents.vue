<template>
  <div class="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden">
    <!-- Cabecera -->
    <div class="bg-slate-50 px-6 py-4 border-b border-slate-200">
      <div class="flex items-center space-x-3">
        <div class="w-10 h-10 bg-slate-800 rounded-xl flex items-center justify-center shadow-md">
          <UserPlus class="w-5 h-5 text-white" />
        </div>
        <div>
          <h2 class="text-lg font-semibold text-slate-800">Registro de Estudiante</h2>
          <p class="text-xs text-slate-500">Completa los datos del estudiante y del apoderado</p>
        </div>
      </div>
    </div>

    <form @submit.prevent="submitForm" class="p-6 space-y-8">
      <!-- Sección: Datos del Apoderado -->
      <div class="space-y-4">
        <div class="flex items-center space-x-2 pb-2 border-b border-slate-200">
          <div class="w-6 h-6 bg-emerald-100 rounded-lg flex items-center justify-center">
            <span class="text-emerald-600 text-xs font-bold">1</span>
          </div>
          <h3 class="text-md font-semibold text-slate-800">Datos del Apoderado</h3>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-5">
          <!-- Nombre del apoderado -->
          <div class="space-y-1.5">
            <label for="parentName" class="block text-sm font-medium text-slate-700">
              Nombre Completo <span class="text-red-500">*</span>
            </label>
            <div class="relative">
              <User class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
              <input type="text" id="parentName" v-model="parentForm.names" @blur="validateField('parentNames')"
                class="w-full pl-10 pr-4 py-2.5 bg-white border rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent transition-all"
                :class="parenErrors.name ? 'border-red-300 bg-red-50' : 'border-slate-200 hover:border-slate-300'"
                placeholder="Juan Carlos Pérez García">
            </div>
            <p v-if="parenErrors.name" class="text-xs text-red-500">{{ parenErrors.name }}</p>
          </div>

          <!-- Teléfono -->
          <div class="space-y-1.5">
            <label for="parentPhone" class="block text-sm font-medium text-slate-700">
              Número de Celular <span class="text-red-500">*</span>
            </label>
            <div class="relative">
              <Phone class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
              <input type="tel" id="parentPhone" v-model="parentForm.phoneNumber" @blur="validateField('parentPhone')"
                class="w-full pl-10 pr-4 py-2.5 bg-white border rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent transition-all"
                :class="parenErrors.phone ? 'border-red-300 bg-red-50' : 'border-slate-200 hover:border-slate-300'"
                placeholder="987654321">
            </div>
            <p v-if="parenErrors.phone" class="text-xs text-red-500">{{ parenErrors.phone }}</p>
          </div>
        </div>
      </div>

      <!-- Sección: Hijos -->
      <div class="space-y-4">
        <div class="flex items-center justify-between pb-2 border-b border-slate-200">
          <div class="flex items-center space-x-2">
            <div class="w-6 h-6 bg-emerald-100 rounded-lg flex items-center justify-center">
              <span class="text-emerald-600 text-xs font-bold">2</span>
            </div>
            <h3 class="text-md font-semibold text-slate-800">Hijos del Apoderado</h3>
            <span class="text-xs text-slate-400 bg-slate-100 px-2 py-0.5 rounded-full">{{ children.length }}
              registrados</span>
          </div>

          <div class="flex space-x-2">
            <button type="button" @click="addChild"
              class="inline-flex items-center space-x-1 px-3 py-1.5 bg-slate-800 text-white text-sm rounded-lg hover:bg-slate-700 transition-all duration-300 transform hover:scale-105">
              <Plus class="w-4 h-4" />
              <span>Agregar Hijo</span>
            </button>
            <button type="button" v-if="children.length > 0" @click="removeChild"
              class="inline-flex items-center space-x-1 px-3 py-1.5 border border-slate-200 text-slate-600 text-sm rounded-lg hover:bg-slate-50 hover:border-slate-300 transition-all duration-300">
              <Minus class="w-4 h-4" />
              <span>Quitar Hijo</span>
            </button>
          </div>
        </div>

        <!-- Lista de hijos -->
        <div v-if="children.length === 0" class="text-center py-8 border-2 border-dashed border-slate-200 rounded-xl">
          <Users class="w-12 h-12 text-slate-300 mx-auto mb-3" />
          <p class="text-sm text-slate-500">No hay hijos registrados</p>
          <button type="button" @click="addChild"
            class="mt-3 text-sm text-indigo-600 hover:text-indigo-800 font-medium">
            + Agregar primer hijo
          </button>
        </div>

        <div v-else class="space-y-4">
          <div v-for="(child, index) in children" :key="index"
            class="relative bg-slate-50 rounded-xl border border-slate-200 p-5 hover:border-slate-300 transition-all duration-300">
            <!-- Cabecera del hijo -->
            <div class="flex items-center justify-between mb-4">
              <div class="flex items-center space-x-3">
                <div class="w-8 h-8 bg-white rounded-lg flex items-center justify-center shadow-sm">
                  <span class="text-sm font-bold text-slate-700">#{{ index + 1 }}</span>
                </div>
                <h4 class="font-medium text-slate-800">
                  {{ child.name || 'Nuevo Hijo' }}
                </h4>
              </div>
              <button type="button" @click="removeChildAtIndex(index)"
                class="p-1.5 text-slate-400 hover:text-red-500 hover:bg-red-50 rounded-lg transition-colors"
                title="Eliminar este hijo">
                <Trash2 class="w-4 h-4" />
              </button>
            </div>

            <!-- Campos del hijo -->
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              <!-- DNI del hijo -->
              <div class="space-y-1">
                <label for="dni" class="block text-xs font-medium text-slate-600">
                  DNI <span class="text-red-500">*</span>
                </label>
                <input type="text" id="dni" v-model="child.dni" @blur="validateChildField(index, 'dni')" maxlength="8"
                  class="w-full px-3 py-2 bg-white border rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent"
                  :class="childErrors[index]?.dni ? 'border-red-300 bg-red-50' : 'border-slate-200 hover:border-slate-300'"
                  placeholder="12345678">
                <p v-if="childErrors[index]?.dni" class="text-xs text-red-500">{{ childErrors[index]?.dni }}</p>
              </div>

              <!-- Nombres del hijo -->
              <div class="space-y-1">
                <label for="names" class="block text-xs font-medium text-slate-600">
                  Nombres <span class="text-red-500">*</span>
                </label>
                <input type="text" id="names" v-model="child.name" @blur="validateChildField(index, 'name')"
                  class="w-full px-3 py-2 bg-white border rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent"
                  :class="childErrors[index]?.name ? 'border-red-300 bg-red-50' : 'border-slate-200 hover:border-slate-300'"
                  placeholder="Luis Miguel">
                <p v-if="childErrors[index]?.name" class="text-xs text-red-500">{{ childErrors[index]?.name }}</p>
              </div>

              <!-- Primer Apellido -->
              <div class="space-y-1">
                <label for="firstLastName" class="block text-xs font-medium text-slate-600">
                  Primer Apellido <span class="text-red-500">*</span>
                </label>
                <input type="text" id="firstLastName" v-model="child.firstLastName"
                  @blur="validateChildField(index, 'firstLastName')"
                  class="w-full px-3 py-2 bg-white border rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent"
                  :class="childErrors[index]?.firstLastName ? 'border-red-300 bg-red-50' : 'border-slate-200 hover:border-slate-300'"
                  placeholder="García">
                <p v-if="childErrors[index]?.firstLastName" class="text-xs text-red-500">{{
                  childErrors[index]?.firstLastName }}</p>
              </div>

              <!-- Segundo Apellido -->
              <div class="space-y-1">
                <label for="secondLastName" class="block text-xs font-medium text-slate-600">
                  Segundo Apellido <span class="text-red-500">*</span>
                </label>
                <input type="text" id="secondLastName" v-model="child.secondLastName"
                  @blur="validateChildField(index, 'secondLastName')"
                  class="w-full px-3 py-2 bg-white border rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent"
                  :class="childErrors[index]?.secondLastName ? 'border-red-300 bg-red-50' : 'border-slate-200 hover:border-slate-300'"
                  placeholder="Pérez">
                <p v-if="childErrors[index]?.secondLastName" class="text-xs text-red-500">{{
                  childErrors[index]?.secondLastName }}</p>
              </div>

              <!-- Class Id -->
              <div class="space-y-1">
                <label for="class" class="block text-xs font-medium text-slate-600">Clase</label>
                <select id="class" v-model="child.classId"
                  class="w-full px-3 py-2 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent">
                  <option v-for="s in sections.sections" :key="s.id" :value="s.id">{{ s.name }}</option>
                </select>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Botones de acción -->
      <div class="flex items-center justify-end space-x-3 pt-4 border-t border-slate-200">
        <button type="button" @click="resetForm"
          class="px-4 py-2 text-sm text-slate-600 hover:bg-slate-100 rounded-lg border border-slate-200 transition-all duration-300">
          Limpiar Formulario
        </button>
        <button type="submit" :disabled="loading || !isFormValid"
          class="relative px-6 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 transition-all duration-300 transform hover:scale-105 shadow-md disabled:opacity-50 disabled:cursor-not-allowed flex items-center space-x-2">
          <Loader2 v-if="loading" class="w-4 h-4 animate-spin" />
          <Save v-else class="w-4 h-4" />
          <span>{{ loading ? 'Registrando...' : 'Registrar Estudiante' }}</span>
        </button>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useToast } from '@/composables/useToast'
import {
  UserPlus,
  User,
  Phone,
  Plus,
  Minus,
  Trash2,
  Users,
  Save,
  Loader2,
} from 'lucide-vue-next'
import type { StudentRequest } from '@/types/Student'
import type { ParentWithChildrenRequest } from '@/types/ParentWithChildren'
import { addParentWithChildren } from '@/services/addParentWithChildren'
import { useSectionStore } from '@/stores/sectionStore'

// Toast
const toast = useToast()
const sections = useSectionStore()

// Estado
const loading = ref(false)

// Formularios
const createEmptyParent = (): ParentWithChildrenRequest => ({
  names: '',
  phoneNumber: '',
  children: [],
})

const parentForm = ref<ParentWithChildrenRequest>(createEmptyParent())

const children = ref<StudentRequest[]>([])

// Errores
const parenErrors = ref({
  name: '',
  phone: ''
})

const childErrors = ref<Array<{
  dni: string
  name: string
  firstLastName: string
  secondLastName: string
}>>([])

// Computed
const isFormValid = computed(() => {
  // Validar padre
  const parentValid = !parenErrors.value.name && !parenErrors.value.phone &&
    parentForm.value.names && parentForm.value.phoneNumber

  // Validar hijos
  let childrenValid = true
  if (children.value.length > 0) {
    children.value.forEach((_, index) => {
      if (!validateChildField(index, 'all', false)) {
        childrenValid = false
      }
    })
  }

  return parentValid && childrenValid
})

// Funciones de validación
const validateField = (field: string) => {
  switch (field) {
    case 'parentNames':
      if (!parentForm.value.names) {
        parenErrors.value.name = 'El nombre del apoderado es obligatorio'
      } else if (parentForm.value.names.length < 3) {
        parenErrors.value.name = 'Mínimo 3 caracteres'
      } else {
        parenErrors.value.name = ''
      }
      break
    case 'parentPhone':
      if (!parentForm.value.phoneNumber) {
        parenErrors.value.phone = 'El número de celular es obligatorio'
      } else if (/^\d{9}$/.test(parentForm.value.phoneNumber)) {
        parenErrors.value.phone = ''
      } else {
        parenErrors.value.phone = 'Debe tener 9 dígitos'
      }
      break
  }
}

const validateChildField = (index: number, field: string, showMessage: boolean = true) => {
  const child = children.value[index]
  if (!childErrors.value[index]) {
    childErrors.value[index] = { dni: '', name: '', firstLastName: '', secondLastName: '' }
  }

  let isValid = true

  if (field === 'dni' || field === 'all') {
    if (!child?.dni) {
      if (showMessage) childErrors.value[index].dni = 'El DNI es obligatorio'
      isValid = false
    } else if (/^\d{8}$/.test(child.dni)) {
      childErrors.value[index].dni = ''
    } else {
      if (showMessage) childErrors.value[index].dni = 'El DNI debe tener 8 dígitos'
      isValid = false
    }
  }

  if (field === 'name' || field === 'all') {
    if (!child?.name) {
      if (showMessage) childErrors.value[index].name = 'Los nombres son obligatorios'
      isValid = false
    } else if (child.name.length < 2) {
      if (showMessage) childErrors.value[index].name = 'Mínimo 2 caracteres'
      isValid = false
    } else {
      childErrors.value[index].name = ''
    }
  }

  if (field === 'firstLastName' || field === 'all') {
    if (!child?.firstLastName) {
      if (showMessage) childErrors.value[index].firstLastName = 'El primer apellido es obligatorio'
      isValid = false
    } else if (child?.firstLastName.length < 2) {
      if (showMessage) childErrors.value[index].firstLastName = 'Mínimo 2 caracteres'
      isValid = false
    } else {
      childErrors.value[index].firstLastName = ''
    }
  }

  if (field === 'secondLastName' || field === 'all') {
    if (!child?.secondLastName) {
      if (showMessage) childErrors.value[index].secondLastName = 'El segundo apellido es obligatorio'
      isValid = false
    } else if (child?.secondLastName.length < 2) {
      if (showMessage) childErrors.value[index].secondLastName = 'Mínimo 2 caracteres'
      isValid = false
    } else {
      childErrors.value[index].secondLastName = ''
    }
  }

  return isValid
}

// Manejo de hijos
const createEmptyChild = (): StudentRequest => ({
  dni: '',
  name: '',
  firstLastName: '',
  secondLastName: '',
  classId: 0
})

const addChild = () => {
  children.value.push(createEmptyChild())
  childErrors.value.push({ dni: '', name: '', firstLastName: '', secondLastName: '' })
}

const removeChild = () => {
  if (children.value.length > 0) {
    children.value.pop()
    childErrors.value.pop()
  }
}

const removeChildAtIndex = (index: number) => {
  children.value.splice(index, 1)
  childErrors.value.splice(index, 1)
}

// Reset formulario
const resetForm = () => {
  parentForm.value = createEmptyParent()

  children.value = []
  childErrors.value = []
}

// Submit
const submitForm = async () => {
  if (children.value.length === 0) {
    toast.showWarning('Sin hijos registrados', 'Agrega al menos un hijo para registrar al apoderado')
    return
  }

  children.value.forEach((_, index) => {
    validateChildField(index, 'all', true)
  })

  if (!isFormValid.value) {
    toast.showWarning('Campos incompletos', 'Por favor completa todos los campos obligatorios')
    return
  }

  loading.value = true

  try {
    parentForm.value.children = children.value
    const response = await addParentWithChildren(parentForm.value)

    if (response.success) {
      toast.showSuccess('Registro exitoso', 'El apoderado y sus hijos han sido registrados correctamente')
      resetForm()
    } else {
      toast.showError('Error en el registro', response.error.message)
    }
  } catch (error) {
    toast.showError('Error de conexión', error instanceof Error ? error.message : 'Error de conexión')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.slide-up-enter-active,
.slide-up-leave-active {
  transition: all 0.3s ease;
}

.slide-up-enter-from,
.slide-up-leave-to {
  opacity: 0;
  transform: translateY(20px);
}

/* Transiciones suaves */
* {
  transition-property: background-color, border-color, color, fill, stroke, opacity, box-shadow, transform;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 200ms;
}
</style>