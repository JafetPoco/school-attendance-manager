<template>
  <div class="min-h-screen bg-slate-50">
    <!-- Botón de menú móvil -->
    <button 
      @click="toggleMobileMenu"
      class="fixed top-4 left-4 z-50 lg:hidden p-2 bg-white rounded-lg shadow-md border border-slate-200"
    >
      <Menu class="w-5 h-5 text-slate-600" />
    </button>

    <!-- Overlay para móvil -->
    <div 
      v-if="mobileMenuOpen"
      class="fixed inset-0 bg-black/50 z-40 lg:hidden transition-opacity duration-300"
      @click="toggleMobileMenu"
    ></div>

    <!-- Sidebar moderno - responsive -->
    <aside 
      class="fixed left-0 top-0 h-full w-72 bg-white border-r border-slate-200 shadow-sm z-50 transition-transform duration-300 lg:translate-x-0"
      :class="mobileMenuOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'"
    >
      <div class="p-6 border-b border-slate-200">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 bg-slate-800 rounded-xl flex items-center justify-center shadow-md">
            <GraduationCap class="w-6 h-6 text-white" />
          </div>
          <div>
            <h2 class="font-bold text-slate-800">{{ auth.user?.schoolName || 'Colegio' }}</h2>
            <p class="text-xs text-slate-500">Portal Docente</p>
          </div>
        </div>
      </div>
      
      <nav class="p-4 space-y-1 overflow-y-auto" style="max-height: calc(100vh - 180px)">
        <router-link 
          to="/dashboard" 
          class="flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200"
          :class="$route.path === '/dashboard' ? 'bg-slate-100 text-slate-800' : 'text-slate-600 hover:bg-slate-50 hover:text-slate-800'"
          @click="closeMobileMenu"
        >
          <LayoutDashboard class="w-5 h-5" />
          <span class="font-medium text-sm">Dashboard</span>
        </router-link>

        <router-link 
          to="/attendances" 
          class="flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200 text-slate-600 hover:bg-slate-50 hover:text-slate-800"
          @click="closeMobileMenu"
        >
          <UserCheck class="w-5 h-5" />
          <span class="font-medium text-sm">Ver Asistencias</span>
        </router-link>
        
        <router-link 
          to="/markAttendance" 
          class="flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200 text-slate-600 hover:bg-slate-50 hover:text-slate-800"
          @click="closeMobileMenu"
        >
          <ClipboardList class="w-5 h-5" />
          <span class="font-medium text-sm">Tomar Asistencia</span>
        </router-link>
        
        <router-link 
          to="/pendingJustifications" 
          class="flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200 text-slate-600 hover:bg-slate-50 hover:text-slate-800"
          @click="closeMobileMenu"
        >
          <FileCheck class="w-5 h-5" />
          <span class="font-medium text-sm">Justificaciones</span>
        </router-link>
        
        <router-link 
          to="/students" 
          class="flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200 text-slate-600 hover:bg-slate-50 hover:text-slate-800"
          @click="closeMobileMenu"
        >
          <Users class="w-5 h-5" />
          <span class="font-medium text-sm">Mis Alumnos</span>
        </router-link>

        <router-link 
          v-if="auth.user?.userType === 'ADMIN'"
          to="/classes" 
          class="flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200 text-slate-600 hover:bg-slate-50 hover:text-slate-800"
          @click="closeMobileMenu"
        >
          <School class="w-5 h-5" />
          <span class="font-medium text-sm">Mis Clases</span>
        </router-link>

        <router-link 
          v-if="auth.user?.userType === 'ADMIN'"
          to="/createUsers" 
          class="flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200 text-slate-600 hover:bg-slate-50 hover:text-slate-800"
          @click="closeMobileMenu"
        >
          <UserLockIcon class="w-5 h-5" />
          <span class="font-medium text-sm">Administrar Cuentas</span>
        </router-link>
        
        <router-link 
          to="/settings" 
          class="flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200 text-slate-600 hover:bg-slate-50 hover:text-slate-800"
          @click="closeMobileMenu"
        >
          <Settings class="w-5 h-5" />
          <span class="font-medium text-sm">Configuración</span>
        </router-link>
      </nav>
      
      <div class="absolute bottom-0 left-0 right-0 p-6 border-t border-slate-200 bg-white">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 bg-slate-200 rounded-xl flex items-center justify-center overflow-hidden">
            <img 
              class="h-10 w-10 object-cover" 
              :src="getUserAvatar()" 
              :alt="getUserFullName()"
              @error="handleImageError"
            />
          </div>
          <div class="flex-1 min-w-0">
            <p class="text-sm font-medium text-slate-800 truncate">{{ getUserFullName() }}</p>
            <p class="text-xs text-slate-500">{{ getUserRole() }}</p>
          </div>
          <LogOut 
            class="w-5 h-5 text-slate-400 cursor-pointer hover:text-red-500 transition-colors shrink-0" 
            @click="handleLogout"
          />
        </div>
      </div>
    </aside>

    <!-- Contenido principal -->
    <main class="lg:ml-72">
      <!-- Header superior -->
      <div class="bg-white border-b border-slate-200 sticky top-0 z-40 px-4 sm:px-8 py-4">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div class="pl-12 lg:pl-0">
            <h1 class="text-xl sm:text-2xl font-bold text-slate-800">Dashboard</h1>
            <p class="text-slate-500 text-sm mt-1">Bienvenido de vuelta, {{ getUserFirstName() }}</p>
          </div>
        </div>
      </div>

      <div class="px-4 sm:px-8 py-6 sm:py-8">
        <!-- Estado de carga -->
        <div v-if="loading" class="flex flex-col items-center justify-center py-12">
          <Loader2 class="w-8 h-8 text-slate-400 animate-spin mb-3" />
          <p class="text-sm text-slate-500">Cargando estadísticas...</p>
        </div>

        <!-- Mensaje de error -->
        <div v-if="errorMessage" class="mb-6 bg-red-50 border border-red-200 rounded-xl p-4 flex items-start space-x-3">
          <AlertCircle class="w-5 h-5 text-red-500 shrink-0 mt-0.5" />
          <div class="flex-1">
            <h3 class="text-sm font-medium text-red-800">Error al cargar los datos</h3>
            <p class="text-xs text-red-600 mt-1">{{ errorMessage }}</p>
          </div>
          <button @click="loadStats" class="text-red-400 hover:text-red-600">
            <RotateCw class="w-4 h-4" />
          </button>
        </div>

        <!-- Tarjetas KPI - responsive grid -->
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 sm:gap-6 mb-8">
          <!-- Asistencia hoy -->
          <div class="bg-white rounded-xl border border-slate-200 shadow-sm p-4 sm:p-6 hover:shadow-md transition-all duration-300 group">
            <div class="flex items-center justify-between mb-3 sm:mb-4">
              <div class="w-10 h-10 sm:w-12 sm:h-12 bg-emerald-100 rounded-xl flex items-center justify-center">
                <UserCheck class="w-5 h-5 sm:w-6 sm:h-6 text-emerald-600" />
              </div>
              <TrendingUp class="w-4 h-4 sm:w-5 sm:h-5 text-emerald-500" />
            </div>
            <p class="text-2xl sm:text-3xl font-bold text-slate-800">{{ presentPorcent }}%</p>
            <p class="text-slate-500 text-xs sm:text-sm mt-1">Asistencia hoy</p>
            <p class="text-xs text-slate-400 mt-2">{{ todayStats?.totalPresences || 0 }} de {{ dashboardStats?.totalStudents || 0 }} presentes</p>
          </div>

          <!-- Tardes registradas -->
          <div class="bg-white rounded-xl border border-slate-200 shadow-sm p-4 sm:p-6 hover:shadow-md transition-all duration-300">
            <div class="flex items-center justify-between mb-3 sm:mb-4">
              <div class="w-10 h-10 sm:w-12 sm:h-12 bg-amber-100 rounded-xl flex items-center justify-center">
                <Clock class="w-5 h-5 sm:w-6 sm:h-6 text-amber-600" />
              </div>
              <AlertCircle class="w-4 h-4 sm:w-5 sm:h-5 text-amber-500" />
            </div>
            <p class="text-2xl sm:text-3xl font-bold text-slate-800">{{ todayStats?.totalLate || 0 }}</p>
            <p class="text-slate-500 text-xs sm:text-sm mt-1">Tardes registradas</p>
          </div>

          <!-- Ausencias sin justificar -->
          <div class="bg-white rounded-xl border border-slate-200 shadow-sm p-4 sm:p-6 hover:shadow-md transition-all duration-300">
            <div class="flex items-center justify-between mb-3 sm:mb-4">
              <div class="w-10 h-10 sm:w-12 sm:h-12 bg-red-100 rounded-xl flex items-center justify-center">
                <UserX class="w-5 h-5 sm:w-6 sm:h-6 text-red-600" />
              </div>
              <AlertCircle class="w-4 h-4 sm:w-5 sm:h-5 text-red-500" />
            </div>
            <p class="text-2xl sm:text-3xl font-bold text-slate-800">{{ todayStats?.totalAbsences || 0 }}</p>
            <p class="text-slate-500 text-xs sm:text-sm mt-1">Ausencias sin justificar</p>
          </div>

          <!-- Justificaciones pendientes -->
          <div class="bg-white rounded-xl border border-slate-200 shadow-sm p-4 sm:p-6 hover:shadow-md transition-all duration-300 cursor-pointer group" @click="goToJustifications">
            <div class="flex items-center justify-between mb-3 sm:mb-4">
              <div class="w-10 h-10 sm:w-12 sm:h-12 bg-purple-100 rounded-xl flex items-center justify-center">
                <FileText class="w-5 h-5 sm:w-6 sm:h-6 text-purple-600" />
              </div>
              <ChevronRight class="w-4 h-4 sm:w-5 sm:h-5 text-slate-400 group-hover:translate-x-1 transition-transform" />
            </div>
            <p class="text-2xl sm:text-3xl font-bold text-slate-800">{{ todayStats?.totalPendingJustification || 0 }}</p>
            <p class="text-slate-500 text-xs sm:text-sm mt-1">Justificaciones pendientes</p>
          </div>
        </div>

        <!-- Gráficos - responsive -->
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
          <!-- Evolución semanal -->
          <div class="bg-white rounded-xl border border-slate-200 shadow-sm p-4 sm:p-6">
            <div class="flex justify-between items-center mb-4 sm:mb-6">
              <div>
                <h3 class="font-semibold text-slate-800 text-sm sm:text-base">Evolución de asistencia</h3>
                <p class="text-xs text-slate-500 mt-1">Última semana</p>
              </div>
            </div>
            <div ref="weeklyChartRef" class="w-full h-64 sm:h-80"></div>
          </div>

          <!-- Top alumnos con más tardes -->
          <div class="bg-white rounded-xl border border-slate-200 shadow-sm p-4 sm:p-6">
            <div class="flex justify-between items-center mb-4">
              <div>
                <h3 class="font-semibold text-slate-800 text-sm sm:text-base">Top alumnos con más tardes</h3>
                <p class="text-xs text-slate-500 mt-1">Acumulado del mes</p>
              </div>
              <Medal class="w-5 h-5 text-amber-500 shrink-0" />
            </div>
            <div v-if="studentsTopLate && studentsTopLate.length > 0" class="space-y-3">
              <div v-for="(student, index) in studentsTopLate.slice(0, 5)" :key="student.fullName" 
                   class="flex flex-col sm:flex-row sm:items-center justify-between p-3 bg-slate-50 rounded-xl hover:bg-slate-100 transition-colors gap-2">
                <div class="flex items-center gap-3">
                  <div class="w-8 h-8 rounded-lg flex items-center justify-center font-bold text-sm shrink-0" 
                       :class="index === 0 ? 'bg-amber-100 text-amber-700' : index === 1 ? 'bg-slate-300 text-slate-700' : index === 2 ? 'bg-orange-100 text-orange-700' : 'bg-slate-200 text-slate-600'">
                    {{ index + 1 }}
                  </div>
                  <div class="min-w-0">
                    <p class="font-medium text-slate-800 text-sm sm:text-base truncate">{{ student.fullName }}</p>
                    <p class="text-xs text-slate-500">{{ student.grade || 'Sin grado' }}</p>
                  </div>
                </div>
                <div class="flex items-center justify-between sm:justify-end gap-4 pl-11 sm:pl-0">
                  <div class="text-right">
                    <p class="text-lg font-bold text-amber-600">{{ student.totalLate }}</p>
                    <p class="text-xs text-slate-500">tardes</p>
                  </div>
                </div>
              </div>
            </div>
            <div v-else class="text-center py-8">
              <p class="text-sm text-slate-500">No hay datos de tardes registradas</p>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, nextTick, watch } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import {
  LayoutDashboard,
  ClipboardList,
  FileCheck,
  Users,
  Settings,
  LogOut,
  UserCheck,
  Clock,
  UserX,
  FileText,
  ChevronRight,
  TrendingUp,
  AlertCircle,
  Medal,
  Loader2,
  RotateCw,
  GraduationCap,
  School,
  UserLockIcon,
  Menu
} from 'lucide-vue-next'
import type { DashboardResponse, StudentsTopLate } from '@/types/Attendance'
import { attendancesStats } from '@/services/dashBoardService'
import { useAuthStore } from '@/stores/authStore'

const router = useRouter()
const auth = useAuthStore()

// Estado
const dashboardStats = ref<DashboardResponse | null>(null)
const studentsTopLate = ref<StudentsTopLate[]>([])
const loading = ref(false)
const errorMessage = ref('')
const mobileMenuOpen = ref(false)

// Referencias para gráficos
const weeklyChartRef = ref<HTMLElement | null>(null)
let weeklyChart: echarts.ECharts | null = null

// Computed
const presentPorcent = computed(() => {
  if (!dashboardStats.value || dashboardStats.value.totalStudents === 0) return 0
  const totalStudents = dashboardStats.value.totalStudents
  const totalPresences = dashboardStats.value.statisticsToday?.totalPresences || 0
  return Math.round((totalPresences / totalStudents) * 100)
})

const todayStats = computed(() => {
  return dashboardStats.value?.statisticsToday || null
})

const weekPresents = computed(() => {
  if (!dashboardStats.value?.weekSumary) return [0, 0, 0, 0, 0]
  const values = [0, 0, 0, 0, 0]
  dashboardStats.value.weekSumary.forEach(day => {
    const total = day.attendances + day.absences + day.late
    if (total > 0 && day.day >= 1 && day.day <= 5) {
      values[day.day - 1] = (day.attendances / total) * 100
    }
  })
  return values
})

const weekLates = computed(() => {
  if (!dashboardStats.value?.weekSumary) return [0, 0, 0, 0, 0]
  const values = [0, 0, 0, 0, 0]
  dashboardStats.value.weekSumary.forEach(day => {
    const total = day.attendances + day.absences + day.late
    if (total > 0 && day.day >= 1 && day.day <= 5) {
      values[day.day - 1] = (day.late / total) * 100
    }
  })
  return values
})

const weekAbsences = computed(() => {
  if (!dashboardStats.value?.weekSumary) return [0, 0, 0, 0, 0]
  const values = [0, 0, 0, 0, 0]
  dashboardStats.value.weekSumary.forEach(day => {
    const total = day.attendances + day.absences + day.late
    if (total > 0 && day.day >= 1 && day.day <= 5) {
      values[day.day - 1] = (day.absences / total) * 100
    }
  })
  return values
})

// Funciones de usuario
const getUserFullName = (): string => {
  const user = auth.user
  if (!user) return 'Usuario'
  return `${user.names || ''} ${user.firstLastName || ''} ${user.secondLastName || ''}`.trim() || 'Usuario'
}

const getUserFirstName = (): string => {
  const user = auth.user
  if (!user || !user.names) return 'Usuario'
  return user.names.trim().split(' ')[0] || 'Usuario'
}

const getUserRole = (): string => {
  const user = auth.user
  if (!user) return 'Docente'
  return user.userType === 'TEACHER' ? 'Docente' : user.userType || 'Usuario'
}

const getUserAvatar = (): string => {
  const user = auth.user
  if (user?.urlPicture) {
    return user.urlPicture
  }
  const name = getUserFullName().replace(/\s+/g, '+')
  return `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&background=334155&color=fff&size=128`
}

const handleImageError = (event: Event) => {
  const img = event.target as HTMLImageElement
  const name = getUserFullName().replace(/\s+/g, '+')
  img.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&background=334155&color=fff&size=128`
}

const handleLogout = async () => {
  try {
    await auth.logout()
  } finally {
    localStorage.removeItem('authToken')
    await router.push('/')
  }
}

// Funciones de menú móvil
const toggleMobileMenu = () => {
  mobileMenuOpen.value = !mobileMenuOpen.value
}

const closeMobileMenu = () => {
  mobileMenuOpen.value = false
}

// Inicializar gráficos
const initCharts = () => {
  if (!weeklyChartRef.value) return

  if (weeklyChart) {
    weeklyChart.dispose()
  }
  
  weeklyChart = echarts.init(weeklyChartRef.value)
  weeklyChart.setOption({
    tooltip: { 
      trigger: 'axis', 
      axisPointer: { type: 'shadow' },
      backgroundColor: '#1e293b',
      borderColor: '#334155',
      textStyle: { color: '#f1f5f9', fontSize: 12 }
    },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { 
      type: 'category', 
      data: ['Lun', 'Mar', 'Mié', 'Jue', 'Vie'], 
      axisLabel: { fontSize: 12, color: '#64748b' },
      axisLine: { lineStyle: { color: '#e2e8f0' } }
    },
    yAxis: { 
      type: 'value', 
      name: 'Porcentaje (%)', 
      max: 100, 
      axisLabel: { formatter: '{value}%', color: '#64748b' },
      splitLine: { lineStyle: { color: '#e2e8f0', type: 'dashed' } }
    },
    series: [
      {
        name: 'Presentes',
        type: 'line',
        data: weekPresents.value,
        smooth: true,
        lineStyle: { width: 3, color: '#10b981' },
        areaStyle: { opacity: 0.1, color: '#10b981' },
        symbol: 'circle',
        symbolSize: 8,
        itemStyle: { color: '#10b981' }
      },
      {
        name: 'Tardes',
        type: 'line',
        data: weekLates.value,
        smooth: true,
        lineStyle: { width: 3, color: '#f59e0b' },
        areaStyle: { opacity: 0.1, color: '#f59e0b' },
        symbol: 'circle',
        symbolSize: 8,
        itemStyle: { color: '#f59e0b' }
      },
      {
        name: 'Faltas',
        type: 'line',
        data: weekAbsences.value,
        smooth: true,
        lineStyle: { width: 3, color: '#f87171' },
        areaStyle: { opacity: 0.1, color: '#f87171' },
        symbol: 'circle',
        symbolSize: 8,
        itemStyle: { color: '#f87171' }
      }
    ]
  })
  
  // Manejar redimensionamiento
  window.addEventListener('resize', () => {
    weeklyChart?.resize()
  })
}

// Actualizar gráficos cuando los datos cambien
watch([weekPresents, weekLates, weekAbsences], () => {
  if (weeklyChart) {
    weeklyChart.setOption({
      series: [
        { data: weekPresents.value },
        { data: weekLates.value },
        { data: weekAbsences.value }
      ]
    })
  }
})

// Cargar datos
const loadStats = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    const response = await attendancesStats()

    if (response.success) {
      dashboardStats.value = response.data
      studentsTopLate.value = response.data.studentsTopLate || []
      // Actualizar gráficos después de cargar datos
      await nextTick()
      initCharts()
    } else {
      errorMessage.value = response.error?.message
    }
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Error de conexión'
  } finally {
    loading.value = false
  }
}

// Navegación
const goToJustifications = () => {
  router.push('/pendingJustifications')
}

// Lifecycle
onMounted(() => {
  loadStats()
})
</script>

<style scoped>
/* Transiciones y animaciones */
.transition-all {
  transition-property: all;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 200ms;
}

/* Scrollbar personalizada */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f5f9;
}

::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

/* Animación para el menú móvil */
@media (max-width: 1023px) {
  .sidebar-enter-active,
  .sidebar-leave-active {
    transition: transform 0.3s ease;
  }
}
</style>