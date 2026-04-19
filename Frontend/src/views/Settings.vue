<template>
<Header />
  <div class="min-h-screen bg-slate-50 py-8 px-4 sm:px-6 lg:px-8">
    
    <main class="max-w-7xl mx-auto">
      <!-- Header de la página -->
      <div class="mb-8 animate-fade-in-down">
        <div class="flex items-center space-x-3 mb-2">
          <div class="w-12 h-12 bg-slate-800 rounded-xl flex items-center justify-center shadow-lg">
            <Settings class="w-6 h-6 text-white" />
          </div>
          <div>
            <h1 class="text-2xl font-bold text-slate-800">Configuración</h1>
            <p class="text-sm text-slate-500">Gestiona los parámetros y reglas del sistema escolar</p>
          </div>
        </div>
      </div>

      <!-- Estado de carga -->
      <transition name="fade">
        <div v-if="loading" class="flex flex-col items-center justify-center py-12 bg-white rounded-xl border border-slate-200">
          <Loader2 class="w-8 h-8 text-slate-400 animate-spin mb-3" />
          <p class="text-sm text-slate-500">Cargando configuración...</p>
        </div>
      </transition>

      <transition name="fade">
        <div v-if="errorMessage" class="mb-6 bg-red-50 border border-red-200 rounded-xl p-4 flex items-start space-x-3">
          <AlertCircle class="w-5 h-5 text-red-500 shrink-0 mt-0.5" />
          <div class="flex-1">
            <h3 class="text-sm font-medium text-red-800">Error de configuración</h3>
            <p class="text-xs text-red-600 mt-1">{{ errorMessage }}</p>
          </div>
        </div>
      </transition>

      <!-- Vista previa de la configuración actual -->
      <div v-if="!loading && !errorMessage" class="mb-8 bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden">
        <div class="bg-slate-50 px-6 py-4 border-b border-slate-200">
          <div class="flex items-center space-x-3">
            <div class="w-8 h-8 bg-emerald-100 rounded-lg flex items-center justify-center">
              <Eye class="w-4 h-4 text-emerald-600" />
            </div>
            <div>
              <h3 class="text-lg font-semibold text-slate-800">Configuración Actual</h3>
              <p class="text-xs text-slate-500">Parámetros activos en el sistema</p>
            </div>
          </div>
        </div>
        <div class="p-6">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div class="flex items-center space-x-3 p-3 bg-slate-50 rounded-lg">
              <div class="w-10 h-10 bg-slate-200 rounded-lg flex items-center justify-center">
                <Calendar class="w-5 h-5 text-slate-600" />
              </div>
              <div>
                <p class="text-xs text-slate-500">Días máximos para justificar</p>
                <p class="text-lg font-bold text-slate-800">{{ currentConfig.justificationExpirationDays }} días</p>
              </div>
            </div>
            <div class="flex items-center space-x-3 p-3 bg-slate-50 rounded-lg">
              <div class="w-10 h-10 bg-slate-200 rounded-lg flex items-center justify-center">
                <Clock class="w-5 h-5 text-slate-600" />
              </div>
              <div>
                <p class="text-xs text-slate-500">Hora límite de tardanza</p>
                <p class="text-lg font-bold text-slate-800">{{ currentConfig.lateAttendaceTime }}</p>
              </div>
            </div>
            <div class="flex items-center space-x-3 p-3 bg-slate-50 rounded-lg">
              <button class="inline-flex items-center space-x-2 px-4 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 transition-all duration-300 transform hover:scale-105 shadow-md" @click="enableEditing">
                Editar configuración
                <ChevronRight class="w-4 h-4 text-slate-400" />
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Formulario de configuración -->
      <div v-if="!loading && isEditing" class="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden animate-fade-in-up">
        <!-- Título de sección con indicador de modo -->
        <div class="bg-slate-50 px-6 py-4 border-b border-slate-200">
          <div class="flex items-center justify-between">
            <div class="flex items-center space-x-3">
              <div class="w-8 h-8 bg-slate-800 rounded-lg flex items-center justify-center">
                <Building class="w-4 h-4 text-white" />
              </div>
              <div>
                <h2 class="text-lg font-semibold text-slate-800">Parámetros del Sistema</h2>
                <p class="text-xs text-slate-500">Configura las reglas de asistencia y justificaciones</p>
              </div>
            </div>
          </div>
        </div>

        <div class="p-6 space-y-6">
          <!-- Días máximos para justificar -->
          <div class="space-y-2">
            <div class="flex items-center justify-between">
              <label for="daysJustify" class="block text-sm font-medium text-slate-700">
                Días máximos para justificar falta
              </label>
              <span class="text-xs text-slate-400 bg-slate-100 px-2 py-1 rounded-full">
                Obligatorio
              </span>
            </div>
            <div class="relative">
              <Calendar class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
              <input type="number" 
                     id="daysJustify" 
                     v-model="formConfig.justificationExpirationDays"
                     min="1"
                     max="30"
                     class="w-full pl-10 pr-4 py-2.5 bg-white border rounded-lg text-sm transition-all">
            </div>
            <p class="text-xs text-slate-400">Define el número de días hábiles que tiene el apoderado para justificar una falta</p>
            <p v-if="errors.days" class="text-xs text-red-500 flex items-center mt-1">
              <AlertCircle class="w-3 h-3 mr-1" />
              {{ errors.days }}
            </p>
          </div>

          <!-- Hora límite de tardanza -->
          <div class="space-y-2">
            <div class="flex items-center justify-between">
              <label for="lateTime" class="block text-sm font-medium text-slate-700">
                Hora a partir de la cual se considera tarde
              </label>
              <span class="text-xs text-slate-400 bg-slate-100 px-2 py-1 rounded-full">
                Obligatorio
              </span>
            </div>
            <div class="relative">
              <Clock class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
              <input type="time" 
                     id="lateTime" 
                     v-model="formConfig.lateAttendaceTime"
                     class="w-full pl-10 pr-4 py-2.5 bg-white border rounded-lg text-sm transition-all">
            </div>
            <p class="text-xs text-slate-400">Los estudiantes que lleguen después de esta hora serán marcados como "Tarde"</p>
            <p v-if="errors.time" class="text-xs text-red-500 flex items-center mt-1">
              <AlertCircle class="w-3 h-3 mr-1" />
              {{ errors.time }}
            </p>
          </div>

          <!-- Información adicional -->
          <div class="bg-blue-50 border border-blue-200 rounded-lg p-4">
            <div class="flex items-start space-x-3">
              <Info class="w-5 h-5 text-blue-500 shrink-0 mt-0.5" />
              <div class="text-sm text-blue-700">
                <p class="font-medium mb-1">Información importante:</p>
                <ul class="text-xs space-y-1 list-disc list-inside">
                  <li>Los cambios aplicarán a partir del siguiente día hábil</li>
                  <li>Las justificaciones fuera del plazo serán rechazadas automáticamente</li>
                  <li>La hora de tardanza aplica para todos los niveles educativos</li>
                </ul>
              </div>
            </div>
          </div>
        </div>

        <!-- Botones de acción -->
        <div class="bg-slate-50 px-6 py-4 border-t border-slate-200 flex items-center justify-end space-x-3">
          <template v-if="isEditing">
            <button @click="cancelEditing"
                    class="px-4 py-2 text-sm text-slate-600 hover:bg-white rounded-lg border border-slate-200 transition-all duration-300">
              Cancelar
            </button>
            <button @click="saveChanges"
                    :disabled="!isFormValid"
                    class="relative px-6 py-2 bg-emerald-600 text-white rounded-lg hover:bg-emerald-700 transition-all duration-300 transform hover:scale-105 shadow-md disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:scale-100 flex items-center space-x-2">
              <Save class="w-4 h-4" />
              <span>Guardar Cambios</span>
            </button>
          </template>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import Header from '@/components/Header.vue'
import { updateSchoolPolicies, getSchoolPolicy, addSchoolPolicies } from '@/services/schoolPolicyService'
import type { SchoolPolicyRequest, SchoolPolicyResponse } from '@/types/SchoolPolicy'
import {
  Settings,
  ChevronRight,
  AlertCircle,
  Loader2,
  Calendar,
  Clock,
  Info,
  Save,
  Building,
  Eye
} from 'lucide-vue-next'
import { useToast } from '@/composables/useToast'

// Estado
const loading = ref(false)
const errorMessage = ref('')
const isEditing = ref(false)

const currentConfig = ref<SchoolPolicyResponse>({
  justificationExpirationDays: 0,
  lateAttendaceTime: '00:00'
})

const formConfig = ref<SchoolPolicyRequest>({
  justificationExpirationDays: 0,
  lateAttendaceTime: '00:00'
})

// Validaciones
const errors = ref({
  days: '',
  time: ''
})

// Inicializar toast
const toast = useToast()

const validateDays = () => {
  const days = formConfig.value.justificationExpirationDays
  if (!days || days <= 0) {
    errors.value.days = 'Los días deben ser un número positivo'
    return false
  }
  if (days > 30) {
    errors.value.days = 'Los días no pueden superar 30'
    return false
  }
  errors.value.days = ''
  return true
}

const validateTime = () => {
  const time = formConfig.value.lateAttendaceTime
  if (!time || time === '00:00') {
    errors.value.time = 'Debe seleccionar una hora válida'
    return false
  }
  errors.value.time = ''
  return true
}

// Computed
const isFormValid = computed(() => {
  return validateDays() && validateTime()
})

// Acciones
const enableEditing = () => {
  isEditing.value = true
  formConfig.value = { ...currentConfig.value }
}

const cancelEditing = () => {
  formConfig.value = { ...currentConfig.value }
  errors.value = { days: '', time: '' }
  isEditing.value = false
}

const saveChanges = async () => {
  if (!isFormValid.value) return

  loading.value = true
  errorMessage.value = ''

  try {
    const response = await updateSchoolPolicies(formConfig.value)

    if (response.success) {
      currentConfig.value = response.data
      isEditing.value = false      
      toast.showSuccess('Configuración actualizada exitosamente', 'Los cambios se han guardado correctamente')
    } else {
      errorMessage.value = response.error.message
    }
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Error de conexión'
  } finally {
    loading.value = false
  }
}

const createInicialConfig = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    const response = await addSchoolPolicies({
      justificationExpirationDays: 1,
      lateAttendaceTime: '08:00'
    })

    if (response.success) {
      currentConfig.value = response.data
    } else {
      errorMessage.value = response.error.message
    }
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Error de conexión'
  } finally {
    loading.value = false
  }
}

// Cargar configuración actual
const loadCurrentConfig = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    const response = await getSchoolPolicy()

    if (response.success) {
      currentConfig.value = response.data
      return
    }

    if (response.error.status === 404) {
      await createInicialConfig()
      return
    }

    errorMessage.value = response.error.message
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Error de conexión'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadCurrentConfig()
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

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
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

.animate-slide-down {
  animation: slideDown 0.3s ease-out;
}

/* Transiciones suaves */
* {
  transition-property: background-color, border-color, color, fill, stroke, opacity, box-shadow, transform;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 200ms;
}

/* Estilos para inputs deshabilitados */
input:disabled {
  cursor: not-allowed;
  background-color: #f8fafc;
}

/* Estilos para inputs tipo number */
input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
  opacity: 0.5;
}

input[type="number"]:hover::-webkit-inner-spin-button,
input[type="number"]:hover::-webkit-outer-spin-button {
  opacity: 1;
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