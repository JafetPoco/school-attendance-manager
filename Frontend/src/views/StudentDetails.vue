<template>
  <div class="min-h-screen bg-slate-50">
    <Header />
    
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Header con navegación -->
      <div class="mb-8 animate-fade-in-down">
        <div class="flex items-center justify-between">
          <div class="flex items-center space-x-4">
            <button @click="goBack" 
                    class="p-2 hover:bg-white rounded-lg transition-colors flex items-center space-x-2 text-slate-600 hover:text-slate-800">
              <ChevronLeft class="w-5 h-5" />
              <span class="text-sm font-medium">Volver</span>
            </button>
            <div class="h-6 w-px bg-slate-200"></div>
            <div class="flex items-center space-x-3">
              <div class="w-10 h-10 bg-slate-800 rounded-xl flex items-center justify-center">
                <User class="w-5 h-5 text-white" />
              </div>
              <div>
                <h1 class="text-2xl font-bold text-slate-800">Perfil del Estudiante</h1>
                <p class="text-sm text-slate-500">Información personal y registro de asistencias</p>
              </div>
            </div>
          </div>

          <!-- Acciones rápidas -->
          <div class="flex items-center space-x-3">
            <div class="relative group w-fit">
              <button disabled
                      class="flex items-center space-x-2 px-4 py-2 text-sm text-slate-600 rounded-lg border border-slate-200 cursor-not-allowed opacity-70">
                <QrCode class="w-4 h-4" />
                <span class="hidden sm:inline">Generar QR</span>
              </button>
              <span
                  class="absolute bottom-full left-1/2 -translate-x-1/2 mb-2
                    pointer-events-none opacity-0 translate-y-1
                    bg-black text-white text-sm px-2 py-1 rounded whitespace-nowrap
                    transition-all duration-200 ease-out
                    group-hover:opacity-100 group-hover:translate-y-0">
                Próximamente
              </span>
            </div>
            <button @click="editStudent" 
                    class="flex items-center space-x-2 px-4 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 transition-all duration-300 transform hover:scale-105">
              <Edit class="w-4 h-4" />
              <span class="hidden sm:inline">Editar</span>
            </button>
          </div>
        </div>
      </div>

      <!-- Estados de carga y error -->
      <transition name="fade">
        <div v-if="loading" class="flex flex-col items-center justify-center py-12">
          <Loader2 class="w-8 h-8 text-slate-400 animate-spin mb-3" />
          <p class="text-sm text-slate-500">Cargando información del estudiante...</p>
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
          <button @click="loadStudentData" class="text-red-400 hover:text-red-600">
            <RotateCw class="w-4 h-4" />
          </button>
        </div>
      </transition>

      <!-- Contenido principal -->
      <div v-if="!loading && !errorMessage && studentInfo" class="space-y-8">
        <!-- Grid de información personal -->
        <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
          <!-- Columna izquierda - Foto y datos básicos -->
          <div class="lg:col-span-1">
            <div class="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden animate-fade-in-left">
              <!-- Cabecera con foto -->
              <div class="bg-linear-to-r from-slate-800 to-slate-700 px-6 py-8 text-center">
                <div class="relative inline-block">
                  <div class="w-28 h-28 rounded-xl bg-white/10 backdrop-blur-sm border-2 border-white/30 flex items-center justify-center mx-auto">
                    <span class="text-4xl font-bold text-white">
                      {{ getInitials(studentInfo.student.name, studentInfo.student.firstLastName) }}
                    </span>
                  </div>
                  <div class="absolute -bottom-2 -right-2 w-6 h-6 bg-green-500 rounded-full border-2 border-white"></div>
                </div>
                <h2 class="mt-4 text-xl font-bold text-white">
                  {{ studentInfo.student.name }} {{ studentInfo.student.firstLastName }} {{ studentInfo.student.secondLastName }}
                </h2>
                <p class="text-sm text-white/80">
                  {{ formatGrade(studentInfo.student.grade) }} • Sección {{ studentInfo.student.section }}
                </p>
              </div>

              <!-- Información detallada -->
              <div class="p-6 space-y-4">
                <div class="flex items-center justify-between py-2 border-b border-slate-100">
                  <span class="text-sm text-slate-500">DNI</span>
                  <span class="text-sm font-mono font-medium text-slate-800">{{ studentInfo.student.dni }}</span>
                </div>
                <div class="flex items-center justify-between py-2 border-b border-slate-100">
                  <span class="text-sm text-slate-500">Nivel</span>
                  <span class="text-sm font-medium text-slate-800">{{ formatLevel(studentInfo.student.level) }}</span>
                </div>
                <div class="flex items-center justify-between py-2 border-b border-slate-100">
                  <span class="text-sm text-slate-500">Grado</span>
                  <span class="text-sm font-medium text-slate-800">{{ formatGrade(studentInfo.student.grade) }}</span>
                </div>
                <div class="flex items-center justify-between py-2 border-b border-slate-100">
                  <span class="text-sm text-slate-500">Sección</span>
                  <span class="text-sm font-medium text-slate-800">{{ studentInfo.student.section }}</span>
                </div>
              </div>

              <!-- Badges adicionales -->
              <div class="px-6 pb-6 flex flex-wrap gap-2">
                <span class="px-3 py-1 bg-blue-50 text-blue-700 text-xs rounded-full">Estudiante Activo</span>
                <span class="px-3 py-1 bg-emerald-50 text-emerald-700 text-xs rounded-full">
                  {{ studentInfo.totalAttendances }} asistencias
                </span>
              </div>
            </div>
          </div>

          <!-- Columna derecha - Estadísticas y gráfico -->
          <div class="lg:col-span-2 space-y-8">
            <!-- Tarjetas de resumen -->
            <div class="grid grid-cols-2 sm:grid-cols-4 gap-4 animate-fade-in-right">
              <div class="bg-white rounded-xl border border-slate-200 p-4 text-center hover:shadow-md transition-all duration-300">
                <div class="w-7 h-7 bg-emerald-100 rounded-lg flex items-center justify-center mx-auto mb-2">
                  <CheckCircle class="w-5 h-5 text-emerald-600" />
                </div>
                <p class="text-2xl font-bold text-slate-800">{{ studentInfo.totalAttendances }}</p>
                <p class="text-xs text-slate-500">Presentes</p>
              </div>
              
              <div class="bg-white rounded-xl border border-slate-200 p-4 text-center hover:shadow-md transition-all duration-300">
                <div class="w-7 h-7 bg-amber-100 rounded-lg flex items-center justify-center mx-auto mb-2">
                  <Clock class="w-5 h-5 text-amber-600" />
                </div>
                <p class="text-2xl font-bold text-slate-800">{{ studentInfo.totalLate }}</p>
                <p class="text-xs text-slate-500">Tardanzas</p>
              </div>
              
              <div class="bg-white rounded-xl border border-slate-200 p-4 text-center hover:shadow-md transition-all duration-300">
                <div class="w-7 h-7 bg-red-100 rounded-lg flex items-center justify-center mx-auto mb-2">
                  <XCircle class="w-5 h-5 text-red-600" />
                </div>
                <p class="text-2xl font-bold text-slate-800">{{ studentInfo.totalAbsences }}</p>
                <p class="text-xs text-slate-500">Ausencias</p>
              </div>
              
              <div class="bg-white rounded-xl border border-slate-200 p-4 text-center hover:shadow-md transition-all duration-300">
                <div class="w-7 h-7 bg-blue-100 rounded-lg flex items-center justify-center mx-auto mb-2">
                  <FileCheck class="w-5 h-5 text-blue-600" />
                </div>
                <p class="text-2xl font-bold text-slate-800">{{ studentInfo.totalExcusedAbsences }}</p>
                <p class="text-xs text-slate-500">Justificados</p>
              </div>
            </div>

            <!-- Gráfico de asistencias -->
            <div class="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden animate-fade-in-right" style="animation-delay: 100ms">
              <div class="bg-slate-50 px-6 py-4 border-b border-slate-200">
                <h3 class="text-lg font-semibold text-slate-800 flex items-center">
                  <PieChart class="w-5 h-5 mr-2 text-slate-600" />
                  Distribución de Asistencias del Último Mes
                </h3>
              </div>
              <div class="p-6">
                <AttendancePieChart
                  :present="studentInfo.totalAttendances"
                  :late="studentInfo.totalLate"
                  :absent="studentInfo.totalAbsences"
                  :justified="studentInfo.totalExcusedAbsences"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Header from '@/components/Header.vue'
import { getStudentAttendance } from '@/services/attendancesService'
import type { StudentAttendanceDetailsResponse } from '@/types/Attendance'
import AttendancePieChart from '@/components/AttendancePieChart.vue'
import {
  ChevronLeft,
  User,
  Edit,
  Loader2,
  AlertCircle,
  RotateCw,
  CheckCircle,
  Clock,
  XCircle,
  FileCheck,
  PieChart,
  QrCode
} from 'lucide-vue-next'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const errorMessage = ref('')
const studentInfo = ref<StudentAttendanceDetailsResponse | null>(null)

// Funciones de utilidad
const getInitials = (name: string, lastName: string) => {
  return (name.charAt(0) + lastName.charAt(0)).toUpperCase()
}

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

// Acciones
const goBack = () => {
  router.back()
}

const editStudent = () => {
  router.push(`/students/${route.params.id}/edit`)
}

const loadStudentData = async () => {
  loading.value = true
  errorMessage.value = ''
  try {
    const id = route.params.id
    const response = await getStudentAttendance(String(id))

    if (response.success) {
      studentInfo.value = response.data
    } else {
      errorMessage.value = response.error.message
    }
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Error de conexión'
  } finally {
    loading.value = false
  }
}

onMounted(loadStudentData)
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

@keyframes fadeInLeft {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes fadeInRight {
  from {
    opacity: 0;
    transform: translateX(20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
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

.animate-fade-in-left {
  animation: fadeInLeft 0.6s ease-out;
}

.animate-fade-in-right {
  animation: fadeInRight 0.6s ease-out;
}

.animate-fade-in-up {
  animation: fadeInUp 0.6s ease-out;
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