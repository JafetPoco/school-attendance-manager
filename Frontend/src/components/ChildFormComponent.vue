<template>
  <div class="relative bg-slate-50 rounded-xl border border-slate-200 p-6 hover:border-slate-300 transition-all duration-300">
    <!-- Header del hijo -->
    <div class="flex items-center justify-between mb-4">
      <div class="flex items-center space-x-3">
        <div class="w-10 h-10 bg-white rounded-lg flex items-center justify-center shadow-sm">
          <span class="text-sm font-bold text-slate-700">#{{ index + 1 }}</span>
        </div>
        <h3 class="font-medium text-slate-800">
          {{ child.name || 'Nuevo Hijo' }}
          <span v-if="child.name" class="ml-2 text-xs text-slate-500">({{ child.grade }} {{ child.section }})</span>
        </h3>
      </div>
      
      <div class="flex items-center space-x-2">
        <button type="button"
                @click="$emit('add-sibling')"
                class="p-1.5 text-slate-500 hover:text-slate-700 hover:bg-white rounded-lg transition-all duration-200"
                title="Agregar otro hijo">
          <Plus class="w-4 h-4" />
        </button>
        <button type="button"
                @click="$emit('remove')"
                class="p-1.5 text-slate-500 hover:text-red-600 hover:bg-white rounded-lg transition-all duration-200"
                title="Eliminar este hijo">
          <Trash2 class="w-4 h-4" />
        </button>
      </div>
    </div>

    <!-- Grid de campos -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
      <!-- DNI -->
      <div class="space-y-1">
        <label :for="`dni-${index}`" class="block text-xs font-medium text-slate-600">
          DNI <span class="text-red-500">*</span>
        </label>
        <div class="relative">
          <input type="text"
                 :id="`dni-${index}`"
                 v-model="dniModel"
                 @blur="$emit('validate')"
                 class="w-full px-3 py-2 bg-white border rounded-lg text-sm transition-all duration-200 focus:ring-2 focus:ring-slate-800 focus:border-transparent"
                 :class="[errors?.dni ? 'border-red-300 bg-red-50' : 'border-slate-200 hover:border-slate-300']"
                 placeholder="12345678">
          <Check v-if="!errors?.dni && child.dni" class="absolute right-3 top-3 w-4 h-4 text-emerald-500"/>
        </div>
        <p v-if="errors?.dni" class="text-xs text-red-500 flex items-center">
          <AlertCircle class="w-3 h-3 mr-1" />
          {{ errors.dni }}
        </p>
      </div>

      <!-- Nombres -->
      <div class="space-y-1">
        <label :for="`name-${index}`" class="block text-xs font-medium text-slate-600">
          Nombres <span class="text-red-500">*</span>
        </label>
        <div class="relative">
          <input type="text"
                 :id="`name-${index}`"
                 v-model="nameModel"
                 @blur="$emit('validate')"
                 class="w-full px-3 py-2 bg-white border rounded-lg text-sm transition-all duration-200 focus:ring-2 focus:ring-slate-800 focus:border-transparent"
                 :class="[errors?.name ? 'border-red-300 bg-red-50' : 'border-slate-200 hover:border-slate-300']"
                 placeholder="Ej: Luis Miguel">
          <Check v-if="!errors?.name && child.name" class="absolute right-3 top-3 w-4 h-4 text-emerald-500"/>
        </div>
        <p v-if="errors?.name" class="text-xs text-red-500 flex items-center">
          <AlertCircle class="w-3 h-3 mr-1" />
          {{ errors.name }}
        </p>
      </div>

      <!-- Primer Apellido -->
      <div class="space-y-1">
        <label :for="`firstLastName-${index}`" class="block text-xs font-medium text-slate-600">
          Primer Apellido <span class="text-red-500">*</span>
        </label>
        <div class="relative">
          <input type="text"
                 :id="`firstLastName-${index}`"
                 v-model="firstLastNameModel"
                 @blur="$emit('validate')"
                 class="w-full px-3 py-2 bg-white border rounded-lg text-sm transition-all duration-200 focus:ring-2 focus:ring-slate-800 focus:border-transparent"
                 :class="[errors?.firstLastName ? 'border-red-300 bg-red-50' : 'border-slate-200 hover:border-slate-300']"
                 placeholder="Ej: García">
          <Check v-if="!errors?.firstLastName && child.firstLastName" class="absolute right-3 top-3 w-4 h-4 text-emerald-500"/>
        </div>
        <p v-if="errors?.firstLastName" class="text-xs text-red-500 flex items-center">
          <AlertCircle class="w-3 h-3 mr-1" />
          {{ errors.firstLastName }}
        </p>
      </div>

      <!-- Segundo Apellido -->
      <div class="space-y-1">
        <label :for="`secondLastName-${index}`" class="block text-xs font-medium text-slate-600">
          Segundo Apellido <span class="text-red-500">*</span>
        </label>
        <div class="relative">
          <input type="text"
                 :id="`secondLastName-${index}`"
                 v-model="secondLastNameModel"
                 @blur="$emit('validate')"
                 class="w-full px-3 py-2 bg-white border rounded-lg text-sm transition-all duration-200 focus:ring-2 focus:ring-slate-800 focus:border-transparent"
                 :class="[errors?.secondLastName ? 'border-red-300 bg-red-50' : 'border-slate-200 hover:border-slate-300']"
                 placeholder="Ej: Pérez">
          <Check v-if="!errors?.secondLastName && child.secondLastName" class="absolute right-3 top-3 w-4 h-4 text-emerald-500"/>
        </div>
        <p v-if="errors?.secondLastName" class="text-xs text-red-500 flex items-center">
          <AlertCircle class="w-3 h-3 mr-1" />
          {{ errors.secondLastName }}
        </p>
      </div>

      <!-- Nivel -->
      <div class="space-y-1">
        <label :for="`level-${index}`" class="block text-xs font-medium text-slate-600">Nivel</label>
        <select :id="`level-${index}`"
                v-model="levelModel"
                class="w-full px-3 py-2 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent">
          <option value="PRIMARIA">Primaria</option>
          <option value="SECUNDARIA">Secundaria</option>
        </select>
      </div>

      <!-- Grado -->
      <div class="space-y-1">
        <label :for="`grade-${index}`" class="block text-xs font-medium text-slate-600">Grado</label>
        <select :id="`grade-${index}`"
                v-model="gradeModel"
                class="w-full px-3 py-2 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent">
          <option value="PRIMERO">Primero</option>
          <option value="SEGUNDO">Segundo</option>
          <option value="TERCERO">Tercero</option>
          <option value="CUARTO">Cuarto</option>
          <option value="QUINTO">Quinto</option>
          <option value="SEXTO">Sexto</option>
        </select>
      </div>

      <!-- Sección -->
      <div class="space-y-1">
        <label :for="`section-${index}`" class="block text-xs font-medium text-slate-600">Sección</label>
        <select :id="`section-${index}`"
                v-model="sectionModel"
                class="w-full px-3 py-2 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent">          <option value="A">A</option>
          <option value="B">B</option>
          <option value="C">C</option>
          <option value="D">D</option>
          <option value="E">E</option>
          <option value="F">F</option>
        </select>
      </div>
    </div>

    <!-- Badge de campos completados -->
    <div class="absolute -top-2 -right-2" v-if="isComplete">
      <div class="bg-emerald-500 text-white text-xs px-2 py-1 rounded-full shadow-lg flex items-center space-x-1">
        <Check class="w-3 h-3" />
        <span>Completo</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Plus, Trash2, User, IdCard, AlertCircle, Check } from 'lucide-vue-next'
import type { StudentRequest } from '../types/Student'

interface Props {
  child: StudentRequest
  index: number
  errors?: {
    dni: string | null
    name: string | null
    firstLastName: string | null
    secondLastName: string | null
  }
}

const props = defineProps<Props>()

const emit = defineEmits<{
  (e: 'update:child', value: StudentRequest): void
  (e: 'validate'): void
  (e: 'remove'): void
  (e: 'add-sibling'): void
}>()

// Helpers para emitir cambios
const updateChild = (updates: Partial<StudentRequest>): void => {
  const updatedChild = { ...props.child, ...updates }
  emit('update:child', updatedChild)
}

// Computed para manejar el DNI como string en el input
const dniModel = computed({
  get: () => props.child.dni?.toString() || '',
  set: (value: string) => {
    updateChild({ dni: value ? value : '' })
  }
})

// Computed para nombre
const nameModel = computed({
  get: () => props.child.name,
  set: (value: string) => {
    updateChild({ name: value })
  }
})

// Computed para primer apellido
const firstLastNameModel = computed({
  get: () => props.child.firstLastName,
  set: (value: string) => {
    updateChild({ firstLastName: value })
  }
})

// Computed para segundo apellido
const secondLastNameModel = computed({
  get: () => props.child.secondLastName,
  set: (value: string) => {
    updateChild({ secondLastName: value })
  }
})

// Computed para nivel
const levelModel = computed({
  get: () => props.child.level,
  set: (value: string) => {
    updateChild({ level: value as 'PRIMARIA' | 'SECUNDARIA' })
  }
})

// Computed para grado
const gradeModel = computed({
  get: () => props.child.grade,
  set: (value: string) => {
    updateChild({ grade: value as StudentRequest['grade'] })
  }
})

// Computed para sección
const sectionModel = computed({
  get: () => props.child.section,
  set: (value: string) => {
    updateChild({ section: value as 'A' | 'B' | 'C' | 'D' | 'E' | 'F' })
  }
})

// Verificar si todos los campos requeridos están completos
const isComplete = computed(() => {
  return (
    props.child.dni &&
    props.child.name.trim() &&
    props.child.firstLastName.trim() &&
    props.child.secondLastName.trim()
  )
})
</script>