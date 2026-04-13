<template>
  <div class="min-h-screen bg-slate-50">
    <!-- Sidebar moderno -->
    <aside class="fixed left-0 top-0 h-full w-72 bg-white border-r border-slate-200 shadow-sm z-50">
      <div class="p-6 border-b border-slate-200">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 bg-slate-800 rounded-xl flex items-center justify-center shadow-md">
            <img src="/logo-dark.svg" alt="Logo" class="w-6 h-6" />
          </div>
          <div>
            <h2 class="font-bold text-slate-800">{{ auth.user?.schoolName }}</h2>
            <p class="text-xs text-slate-500">Portal Docente</p>
          </div>
        </div>
      </div>
      
      <nav class="p-4 space-y-1">
        <router-link 
          to="/dashboard" 
          class="flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200"
          :class="$route.path === '/dashboard' ? 'bg-slate-100 text-slate-800' : 'text-slate-600 hover:bg-slate-50 hover:text-slate-800'"
        >
          <LayoutDashboard class="w-5 h-5" />
          <span class="font-medium text-sm">Dashboard</span>
        </router-link>
        
        <router-link 
          to="/markAttendance" 
          class="flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200 text-slate-600 hover:bg-slate-50 hover:text-slate-800"
        >
          <ClipboardList class="w-5 h-5" />
          <span class="font-medium text-sm">Tomar Asistencia</span>
        </router-link>
        
        <router-link 
          to="/pendingJustifications" 
          class="flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200 text-slate-600 hover:bg-slate-50 hover:text-slate-800"
        >
          <FileCheck class="w-5 h-5" />
          <span class="font-medium text-sm">Justificaciones</span>
        </router-link>
        
        <router-link 
          to="/students" 
          class="flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200 text-slate-600 hover:bg-slate-50 hover:text-slate-800"
        >
          <Users class="w-5 h-5" />
          <span class="font-medium text-sm">Mis Alumnos</span>
        </router-link>
        
        <router-link 
          to="/reports" 
          class="flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200 text-slate-600 hover:bg-slate-50 hover:text-slate-800"
        >
          <BarChart3 class="w-5 h-5" />
          <span class="font-medium text-sm">Reportes</span>
        </router-link>
        
        <router-link 
          to="/addSchoolPolicy" 
          class="flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200 text-slate-600 hover:bg-slate-50 hover:text-slate-800"
        >
          <Settings class="w-5 h-5" />
          <span class="font-medium text-sm">Configuración</span>
        </router-link>
      </nav>
      
      <div class="absolute bottom-0 left-0 right-0 p-6 border-t border-slate-200 bg-white">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 bg-slate-200 rounded-xl flex items-center justify-center">
            <img class="h-10 w-10 rounded-xl border-2 border-slate-200 group-hover:border-slate-400 transition-all duration-300" 
                :src="auth.user?.urlPicture" 
                :alt="auth.user?.names" @error="useFallback">
          </div>
          <div class="flex-1">
            <p class="text-sm font-medium text-slate-800">{{ auth.user?.names + ' ' + auth.user?.firstLastName + ' ' +auth.user?.secondLastName}}</p>
            <p class="text-xs text-slate-500">{{ auth.user?.userType }}</p>
          </div>
          <LogOut class="w-5 h-5 text-slate-400 cursor-pointer hover:text-red-500 transition-colors" />
        </div>
      </div>
    </aside>

    <!-- Contenido principal -->
    <main class="ml-72">
      <!-- Header superior -->
      <div class="bg-white border-b border-slate-200 sticky top-0 z-40 px-8 py-4">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div>
            <h1 class="text-2xl font-bold text-slate-800">Dashboard</h1>
            <p class="text-slate-500 text-sm mt-1">Bienvenida de vuelta, {{ auth.user?.names }}</p>
          </div>
        </div>
      </div>

      <div class="px-8 py-8">
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
        </div>

        <!-- Tarjetas KPI -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <!-- Asistencia hoy -->
          <div class="bg-white rounded-xl border border-slate-200 shadow-sm p-6 hover:shadow-md transition-all duration-300 group">
            <div class="flex items-center justify-between mb-4">
              <div class="w-12 h-12 bg-emerald-100 rounded-xl flex items-center justify-center">
                <UserCheck class="w-6 h-6 text-emerald-600" />
              </div>
              <TrendingUp class="w-5 h-5 text-emerald-500" />
            </div>
            <p class="text-3xl font-bold text-slate-800">{{ presentPorcent }}%</p>
            <p class="text-slate-500 text-sm mt-1">Asistencia hoy</p>
            <p class="text-xs text-slate-400 mt-2">{{ attendancesStatsModel?.totalPresences }} de {{ attendancesStatsModel?.totalAttendances }} presentes</p>
          </div>

          <!-- Tardes registradas -->
          <div class="bg-white rounded-xl border border-slate-200 shadow-sm p-6 hover:shadow-md transition-all duration-300">
            <div class="flex items-center justify-between mb-4">
              <div class="w-12 h-12 bg-amber-100 rounded-xl flex items-center justify-center">
                <Clock class="w-6 h-6 text-amber-600" />
              </div>
              <AlertCircle class="w-5 h-5 text-amber-500" />
            </div>
            <p class="text-3xl font-bold text-slate-800">{{ attendancesStatsModel?.totalLate || 0 }}</p>
            <p class="text-slate-500 text-sm mt-1">Tardes registradas</p>
          </div>

          <!-- Ausencias sin justificar -->
          <div class="bg-white rounded-xl border border-slate-200 shadow-sm p-6 hover:shadow-md transition-all duration-300">
            <div class="flex items-center justify-between mb-4">
              <div class="w-12 h-12 bg-red-100 rounded-xl flex items-center justify-center">
                <UserX class="w-6 h-6 text-red-600" />
              </div>
              <AlertCircle class="w-5 h-5 text-red-500" />
            </div>
            <p class="text-3xl font-bold text-slate-800">{{ attendancesStatsModel?.totalAbsences || 0 }}</p>
            <p class="text-slate-500 text-sm mt-1">Ausencias sin justificar</p>
          </div>

          <!-- Justificaciones pendientes -->
          <div class="bg-white rounded-xl border border-slate-200 shadow-sm p-6 hover:shadow-md transition-all duration-300 cursor-pointer group" @click="goToJustifications">
            <div class="flex items-center justify-between mb-4">
              <div class="w-12 h-12 bg-purple-100 rounded-xl flex items-center justify-center">
                <FileText class="w-6 h-6 text-purple-600" />
              </div>
              <ChevronRight class="w-5 h-5 text-slate-400 group-hover:translate-x-1 transition-transform" />
            </div>
            <p class="text-3xl font-bold text-slate-800">{{ attendancesStatsModel?.totalPendingJustifications || 0 }}</p>
            <p class="text-slate-500 text-sm mt-1">Justificaciones pendientes</p>
          </div>
        </div>

        <!-- Gráficos -->
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
          <!-- Evolución semanal -->
          <div class="bg-white rounded-xl border border-slate-200 shadow-sm p-6">
            <div class="flex justify-between items-center mb-6">
              <div>
                <h3 class="font-semibold text-slate-800">Evolución de asistencia</h3>
                <p class="text-xs text-slate-500 mt-1">Última semana</p>
              </div>
              <div class="flex gap-2">
                <button class="text-xs px-2 py-1 rounded-lg bg-emerald-50 text-emerald-600">Presentes</button>
                <button class="text-xs px-2 py-1 rounded-lg text-slate-600 hover:bg-slate-50">Tardes</button>
              </div>
            </div>
            <div ref="weeklyChartRef" class="w-full h-80"></div>
          </div>

          <!-- Top alumnos con más tardes -->
          <div class="bg-white rounded-xl border border-slate-200 shadow-sm p-6">
            <div class="flex justify-between items-center mb-4">
              <div>
                <h3 class="font-semibold text-slate-800">Top alumnos con más tardes</h3>
                <p class="text-xs text-slate-500 mt-1">Acumulado del mes</p>
              </div>
              <Medal class="w-5 h-5 text-amber-500" />
            </div>
            <div class="space-y-3">
              <div v-for="(student, index) in topLateStudents" :key="student.name" 
                   class="flex items-center justify-between p-3 bg-slate-50 rounded-xl hover:bg-slate-100 transition-colors">
                <div class="flex items-center gap-3">
                  <div class="w-8 h-8 rounded-lg flex items-center justify-center font-bold text-sm" 
                       :class="index === 0 ? 'bg-amber-100 text-amber-700' : index === 1 ? 'bg-slate-300 text-slate-700' : 'bg-orange-100 text-orange-700'">
                    {{ index + 1 }}
                  </div>
                  <div>
                    <p class="font-medium text-slate-800">{{ student.name }}</p>
                    <p class="text-xs text-slate-500">{{ student.group }}</p>
                  </div>
                </div>
                <div class="flex items-center gap-4">
                  <div class="text-right">
                    <p class="text-lg font-bold text-amber-600">{{ student.lates }}</p>
                    <p class="text-xs text-slate-500">tardes</p>
                  </div>
                  <button class="text-blue-600 hover:text-blue-700 text-sm font-medium">Contactar</button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Sección adicional: Resumen de justificaciones -->
        <div class="grid grid-cols-1 gap-6">
          <div class="bg-white rounded-xl border border-slate-200 shadow-sm p-6">
            <div class="flex justify-between items-center mb-6">
              <div>
                <h3 class="font-semibold text-slate-800">Resumen de justificaciones</h3>
                <p class="text-xs text-slate-500 mt-1">Distribución de justificaciones del mes</p>
              </div>
              <div class="flex gap-2">
                <span class="px-2 py-1 bg-emerald-100 text-emerald-700 text-xs rounded-full">Aprobadas: {{ approvedJustifications }}</span>
                <span class="px-2 py-1 bg-amber-100 text-amber-700 text-xs rounded-full">Pendientes: {{ attendancesStatsModel?.totalPendingJustifications || 0 }}</span>
                <span class="px-2 py-1 bg-red-100 text-red-700 text-xs rounded-full">Rechazadas: 2</span>
              </div>
            </div>
            <div ref="justificationsChartRef" class="w-full h-80"></div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import {
  LayoutDashboard,
  ClipboardList,
  FileCheck,
  Users,
  BarChart3,
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
  Loader2
} from 'lucide-vue-next'
import type { AttendanceStats } from '@/types/Attendance'
import { attendancesStats } from '@/services/dashBoardService'
import { useAuthStore } from '@/stores/authStore'

const router = useRouter()

// Estado
const pendingJustifications = ref(4)
const attendancesStatsModel = ref<AttendanceStats | null>(null)
const loading = ref(false)
const errorMessage = ref('')
const approvedJustifications = ref(12)

// Referencias para gráficos
const weeklyChartRef = ref<HTMLElement | null>(null)
const justificationsChartRef = ref<HTMLElement | null>(null)
let weeklyChart: echarts.ECharts | null = null
let justificationsChart: echarts.ECharts | null = null

const auth = useAuthStore()

// Computed
const presentPorcent = computed(() => {
  if (!attendancesStatsModel.value) return 0
  const { totalAttendances, totalPresences } = attendancesStatsModel.value
  return totalAttendances > 0 ? Math.round((totalPresences / totalAttendances) * 100) : 0
})

// Datos
const topLateStudents = ref([
  { name: 'Carlos López', group: '3°A - Sección A', lates: 5 },
  { name: 'Ana Martínez', group: '3°B - Sección B', lates: 4 },
  { name: 'Luis Fernández', group: '3°A - Sección A', lates: 3 },
  { name: 'Sofía Ramírez', group: '4°C - Sección C', lates: 3 }
])

const useFallback = (event: Event) => {
  const img = (event.currentTarget ?? event.target) as HTMLImageElement | null
  const username = auth.user?.names ?? 'Usuario'
  const name = username.replace(/\s+/g, '+')
  if (img) {
    img.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&background=334155&color=fff&size=128`
  }
};

// Inicializar gráficos
const initCharts = () => {
  if (!weeklyChartRef.value || !justificationsChartRef.value) return

  // Gráfico semanal
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
      data: ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb'], 
      axisLabel: { fontSize: 12, color: '#64748b' },
      axisLine: { lineStyle: { color: '#e2e8f0' } }
    },
    yAxis: { 
      type: 'value', 
      name: 'Asistencia (%)', 
      max: 100, 
      axisLabel: { formatter: '{value}%', color: '#64748b' },
      splitLine: { lineStyle: { color: '#e2e8f0', type: 'dashed' } }
    },
    series: [
      {
        name: 'Presentes',
        type: 'line',
        data: [82, 88, 85, 78, 90, 87],
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
        data: [12, 8, 10, 15, 7, 9],
        smooth: true,
        lineStyle: { width: 3, color: '#f59e0b' },
        areaStyle: { opacity: 0.1, color: '#f59e0b' },
        symbol: 'circle',
        symbolSize: 8,
        itemStyle: { color: '#f59e0b' }
      }
    ]
  })

  // Gráfico de justificaciones
  if (justificationsChart) {
    justificationsChart.dispose()
  }
  justificationsChart = echarts.init(justificationsChartRef.value)
  justificationsChart.setOption({
    tooltip: { 
      trigger: 'item',
      backgroundColor: '#1e293b',
      borderColor: '#334155',
      textStyle: { color: '#f1f5f9', fontSize: 12 }
    },
    legend: { 
      orient: 'vertical', 
      left: 'left',
      textStyle: { fontSize: 12, color: '#64748b' }
    },
    series: [{
      name: 'Justificaciones',
      type: 'pie',
      radius: '55%',
      data: [
        { value: approvedJustifications.value, name: 'Aprobadas', itemStyle: { color: '#10b981' } },
        { value: attendancesStatsModel.value?.totalPendingJustifications || 0, name: 'Pendientes', itemStyle: { color: '#f59e0b' } },
        { value: 2, name: 'Rechazadas', itemStyle: { color: '#ef4444' } }
      ],
      emphasis: { scale: true },
      label: { show: true, formatter: '{b}: {d}%', color: '#475569' }
    }]
  })

  // Responsive
  window.addEventListener('resize', () => {
    weeklyChart?.resize()
    justificationsChart?.resize()
  })
}

// Cargar datos
const loadStats = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    const response = await attendancesStats()

    if (response.success) {
      attendancesStatsModel.value = response.data
      pendingJustifications.value = response.data.totalPendingJustifications
      // Actualizar gráfico después de cargar datos
      nextTick(() => {
        if (justificationsChart && attendancesStatsModel.value) {
          justificationsChart.setOption({
            series: [{
              data: [
                { value: approvedJustifications.value, name: 'Aprobadas', itemStyle: { color: '#10b981' } },
                { value: attendancesStatsModel.value.totalPendingJustifications, name: 'Pendientes', itemStyle: { color: '#f59e0b' } },
                { value: 2, name: 'Rechazadas', itemStyle: { color: '#ef4444' } }
              ]
            }]
          })
        }
      })
    } else {
      errorMessage.value = response.error.message
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
  setTimeout(() => {
    initCharts()
  }, 100)
})

// Watch para cuando los datos cambien
watch(attendancesStatsModel, () => {
  nextTick(() => {
    if (justificationsChart && attendancesStatsModel.value) {
      justificationsChart.setOption({
        series: [{
          data: [
            { value: approvedJustifications.value, name: 'Aprobadas', itemStyle: { color: '#10b981' } },
            { value: attendancesStatsModel.value.totalPendingJustifications, name: 'Pendientes', itemStyle: { color: '#f59e0b' } },
            { value: 2, name: 'Rechazadas', itemStyle: { color: '#ef4444' } }
          ]
        }]
      })
    }
  })
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
</style>