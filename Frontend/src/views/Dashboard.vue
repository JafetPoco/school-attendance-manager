<template>
  <div class="min-h-screen bg-slate-50">
    <!-- Navbar superior con tonos slate -->
    <nav class="bg-white border-b border-slate-200 sticky top-0 z-50">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
          <!-- Logo y nombre del colegio -->
          <div class="flex items-center space-x-3">
            <div class="w-10 h-10 bg-slate-800 rounded-xl flex items-center justify-center shadow-md">
              <GraduationCap class="w-6 h-6 text-white" />
            </div>
            <div>
              <span v-if="auth.user?.schoolName" class="text-lg font-semibold text-slate-800">{{ auth.user.schoolName }}</span>
              <span v-else class="text-lg font-semibold text-slate-800">PANEL ADMINISTRADOR</span>
              <p class="text-xs text-slate-500">Portal Docente</p>
            </div>
          </div>

          <!-- Información del docente y acciones -->
          <div class="flex items-center space-x-6">
            <!-- Notificaciones -->
            <button class="relative p-2 text-slate-600 hover:bg-slate-100 rounded-lg transition-all duration-300 hover:scale-110 group">
              <Bell class="w-5 h-5" />
              <span class="absolute top-1 right-1 w-2 h-2 bg-red-500 rounded-full ring-2 ring-white"></span>
              <span class="absolute -bottom-8 left-1/2 transform -translate-x-1/2 bg-slate-800 text-white text-xs py-1 px-2 rounded opacity-0 group-hover:opacity-100 transition-opacity duration-200 whitespace-nowrap">
                Notificaciones
              </span>
            </button>

            <!-- Perfil docente (muestra skeletons mientras carga) -->
            <div v-if="auth.loading && !auth.user" class="flex items-center space-x-3">
              <div class="text-right hidden sm:block">
                <div class="w-32 h-4 bg-slate-200 rounded mb-1 animate-pulse"></div>
                <div class="w-20 h-3 bg-slate-200 rounded animate-pulse"></div>
              </div>
              <div class="relative">
                <div class="h-10 w-10 rounded-xl bg-slate-200 animate-pulse"></div>
              </div>
            </div>
            <div v-else class="flex items-center space-x-3 cursor-pointer group relative">
              <div class="text-right hidden sm:block">
                <p class="text-sm font-semibold text-slate-800">{{ auth.user?.names }}</p>
                <p class="text-xs text-slate-500">{{ auth.user?.userType }}</p>
              </div>
              <div class="relative">
                <img class="h-10 w-10 rounded-xl border-2 border-slate-200 group-hover:border-slate-400 transition-all duration-300" 
                     :src="auth.user?.urlPicture" 
                     :alt="auth.user?.names" @error="useFallback">
                <div class="absolute -bottom-1 -right-1 w-4 h-4 bg-green-500 rounded-full border-2 border-white"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </nav>

    <!-- Contenido principal -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Header con bienvenida y fecha -->
      <div class="flex flex-col md:flex-row md:items-center md:justify-between mb-8 animate-fade-in-down">
        <div v-if="!auth.loading && auth.user">
          <h1 class="text-2xl font-bold text-slate-800 mb-1">
            ¡Hola, {{ auth.user?.names }} {{ auth.user.firstLastName}} {{ auth.user.secondLastName }}!
          </h1>
          <p class="text-slate-500 flex items-center">
            <Calendar class="w-4 h-4 mr-2" />
            {{ currentDate }}
          </p>
        </div>
        <div v-else class="space-y-2">
          <div class="w-48 h-6 bg-slate-200 rounded mb-2 animate-pulse"></div>
          <div class="w-64 h-4 bg-slate-200 rounded animate-pulse"></div>
        </div>
      </div>

      <!-- Tarjetas de resumen académico -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <div v-for="(stat, index) in academicStats" :key="index"
             class="bg-white rounded-xl border border-slate-200 p-6 hover:border-slate-300 hover:shadow-md transition-all duration-500 animate-fade-in-up group cursor-pointer"
             :style="{ animationDelay: `${index * 100}ms` }">
          <div class="flex items-center justify-between mb-3">
            <div class="w-12 h-12 bg-slate-100 rounded-xl flex items-center justify-center group-hover:bg-slate-200 transition-colors duration-300">
              <component :is="stat.icon" class="w-6 h-6 text-slate-700" />
            </div>
            <span :class="`text-xs font-medium px-2 py-1 rounded-full ${
              stat.trend === 'up' ? 'bg-emerald-50 text-emerald-600' : 'bg-amber-50 text-amber-600'
            }`">
              {{ stat.change }}
            </span>
          </div>
          <div class="space-y-1">
            <p class="text-sm text-slate-500">{{ stat.label }}</p>
            <div class="flex items-baseline justify-between">
              <p class="text-2xl font-bold text-slate-800">{{ stat.value }}</p>
              <p class="text-xs text-slate-400">{{ stat.period }}</p>
            </div>
          </div>
          <!-- Barra de progreso minimalista -->
          <div class="mt-4 w-full h-1 bg-slate-100 rounded-full overflow-hidden">
            <div class="h-full bg-slate-800 rounded-full transition-all duration-1000 ease-out"
                 :style="{ width: stat.progress + '%' }"></div>
          </div>
        </div>
      </div>

      <!-- Grid principal de 2 columnas -->
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <!-- Columna izquierda - 2/3 del ancho -->
        <div class="lg:col-span-2 space-y-8">
          <!-- Sección de cursos y asistencias -->
          <div class="bg-white rounded-xl border border-slate-200 p-6 animate-fade-in-left">
            <div class="flex items-center justify-between mb-6">
              <h2 class="text-lg font-semibold text-slate-800 flex items-center">
                <Users class="w-5 h-5 mr-2 text-slate-600" />
                Mis Cursos - Asistencia Hoy
              </h2>
              <button class="text-sm text-slate-600 hover:text-slate-800 font-medium flex items-center">
                Ver todos
                <ChevronRight class="w-4 h-4 ml-1" />
              </button>
            </div>

            <div class="space-y-4">
              <div v-for="(course, index) in teacherCourses" :key="index"
                   class="border border-slate-200 rounded-xl p-4 hover:border-slate-300 hover:shadow-sm transition-all duration-300">
                <div class="flex flex-col md:flex-row md:items-center md:justify-between">
                  <div class="flex items-start space-x-4">
                    <div :class="`w-12 h-12 bg-${course.color}-50 rounded-xl flex items-center justify-center`">
                      <BookOpen class="w-6 h-6" :class="`text-${course.color}-600`" />
                    </div>
                    <div>
                      <h3 class="font-semibold text-slate-800">{{ course.name }}</h3>
                      <p class="text-sm text-slate-500">{{ course.grade }} • {{ course.schedule }}</p>
                      <div class="flex items-center mt-2 space-x-4">
                        <span class="text-xs text-slate-500 flex items-center">
                          <Users class="w-3 h-3 mr-1" />
                          {{ course.students }} estudiantes
                        </span>
                        <span class="text-xs text-slate-500 flex items-center">
                          <Clock class="w-3 h-3 mr-1" />
                          {{ course.progress }}% asistencia
                        </span>
                      </div>
                    </div>
                  </div>
                  
                  <div class="flex items-center space-x-2 mt-4 md:mt-0">
                    <button class="flex items-center space-x-1 bg-slate-800 text-white px-4 py-2 rounded-lg text-sm hover:bg-slate-700 transition-all duration-300 transform hover:scale-105">
                      <CheckSquare class="w-4 h-4 mr-1" />
                      Tomar Asistencia
                    </button>
                    <button class="p-2 text-slate-600 hover:bg-slate-100 rounded-lg transition-all duration-300">
                      <MoreVertical class="w-4 h-4" />
                    </button>
                  </div>
                </div>

                <!-- Mini tabla de resumen de asistencia -->
                <div class="mt-4 pt-4 border-t border-slate-100">
                  <div class="grid grid-cols-3 gap-2 text-center text-xs">
                    <div class="p-2 bg-slate-50 rounded-lg">
                      <p class="text-slate-600 mb-1">Presentes</p>
                      <p class="font-semibold text-emerald-600">{{ course.attendance.present }}/{{ course.students }}</p>
                    </div>
                    <div class="p-2 bg-slate-50 rounded-lg">
                      <p class="text-slate-600 mb-1">Ausentes</p>
                      <p class="font-semibold text-amber-600">{{ course.attendance.absent }}</p>
                    </div>
                    <div class="p-2 bg-slate-50 rounded-lg">
                      <p class="text-slate-600 mb-1">Justificados</p>
                      <p class="font-semibold text-blue-600">{{ course.attendance.justified }}</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Gráficos de asistencia -->
          <div class="bg-white rounded-xl border border-slate-200 p-6 animate-fade-in-up">
            <div class="flex items-center justify-between mb-6">
              <h2 class="text-lg font-semibold text-slate-800 flex items-center">
                <BarChart3 class="w-5 h-5 mr-2 text-slate-600" />
                Estadísticas de Asistencia
              </h2>
              <div class="flex space-x-2">
                <button class="px-3 py-1 text-xs bg-slate-100 text-slate-600 rounded-lg hover:bg-slate-200 transition-colors">Semana</button>
                <button class="px-3 py-1 text-xs bg-slate-800 text-white rounded-lg hover:bg-slate-700 transition-colors">Mes</button>
                <button class="px-3 py-1 text-xs bg-slate-100 text-slate-600 rounded-lg hover:bg-slate-200 transition-colors">Trimestre</button>
              </div>
            </div>

            <!-- Gráfico de barras minimalista -->
            <div class="h-64 flex items-end justify-between space-x-2">
              <div v-for="(day, index) in attendanceChart" :key="index" 
                   class="flex-1 flex flex-col items-center group">
                <div class="w-full relative">
                  <div class="absolute bottom-full left-1/2 transform -translate-x-1/2 mb-2 bg-slate-800 text-white text-xs py-1 px-2 rounded opacity-0 group-hover:opacity-100 transition-opacity whitespace-nowrap">
                    {{ day.value }}% asistencia
                  </div>
                  <div class="bg-slate-100 w-full rounded-t-lg overflow-hidden" :style="{ height: '160px' }">
                    <div class="bg-slate-800 w-full transition-all duration-1000 ease-out"
                         :style="{ height: day.value + '%' }"></div>
                  </div>
                </div>
                <span class="text-xs text-slate-600 mt-2">{{ day.label }}</span>
              </div>
            </div>

            <!-- Leyenda -->
            <div class="flex items-center justify-center space-x-6 mt-6 pt-4 border-t border-slate-100">
              <div class="flex items-center">
                <div class="w-3 h-3 bg-slate-800 rounded-full mr-2"></div>
                <span class="text-xs text-slate-600">Presentes</span>
              </div>
              <div class="flex items-center">
                <div class="w-3 h-3 bg-amber-400 rounded-full mr-2"></div>
                <span class="text-xs text-slate-600">Ausentes</span>
              </div>
              <div class="flex items-center">
                <div class="w-3 h-3 bg-blue-400 rounded-full mr-2"></div>
                <span class="text-xs text-slate-600">Justificados</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Columna derecha - 1/3 del ancho -->
        <div class="space-y-8">
          <!-- Acciones rápidas docentes -->
          <div class="bg-white rounded-xl border border-slate-200 p-6 animate-fade-in-right" style="animation-delay: 200ms">
            <h3 class="text-lg font-semibold text-slate-800 mb-4 flex items-center">
              <Zap class="w-5 h-5 mr-2 text-slate-600" />
              Acciones Rápidas
            </h3>
            
            <div class="space-y-3">
              <button v-for="(action, index) in teacherActions" :key="index" @click="navigateTo(action.path)"
                      class="w-full flex items-center justify-between p-3 bg-slate-50 rounded-xl hover:bg-slate-100 transition-all duration-300 group">
                <div class="flex items-center space-x-3">
                  <div class="w-10 h-10 bg-white rounded-lg flex items-center justify-center group-hover:scale-110 transition-transform duration-300">
                    <component :is="action.icon" class="w-5 h-5 text-slate-700" />
                  </div>
                  <div class="text-left">
                    <p class="text-sm font-medium text-slate-800">{{ action.title }}</p>
                    <p class="text-xs text-slate-500">{{ action.description }}</p>
                  </div>
                </div>
                <ChevronRight class="w-4 h-4 text-slate-400 group-hover:text-slate-600 group-hover:translate-x-1 transition-all duration-300" />
              </button>
            </div>
          </div>

          <!-- Próximos eventos -->
          <div class="bg-white rounded-xl border border-slate-200 p-6 animate-fade-in-right" style="animation-delay: 300ms">
            <h3 class="text-lg font-semibold text-slate-800 mb-4 flex items-center">
              <Calendar class="w-5 h-5 mr-2 text-slate-600" />
              Próximos Eventos
            </h3>
            
            <div class="space-y-4">
              <div v-for="(event, index) in upcomingEvents" :key="index"
                   class="flex items-start space-x-3 group cursor-pointer">
                <div class="w-12 h-12 bg-slate-100 rounded-lg flex flex-col items-center justify-center shrink-0 group-hover:bg-slate-200 transition-colors">
                  <span class="text-xs text-slate-500">{{ event.month }}</span>
                  <span class="text-lg font-bold text-slate-800 -mt-1">{{ event.day }}</span>
                </div>
                <div>
                  <p class="text-sm font-medium text-slate-800">{{ event.title }}</p>
                  <p class="text-xs text-slate-500">{{ event.time }}</p>
                  <p class="text-xs text-slate-400 mt-1">{{ event.location }}</p>
                </div>
              </div>
            </div>
            
            <button class="w-full mt-4 text-sm text-slate-600 hover:text-slate-800 font-medium flex items-center justify-center py-2 border-t border-slate-100">
              Ver calendario completo
              <ChevronRight class="w-4 h-4 ml-1" />
            </button>
          </div>

          <!-- Reportes recientes -->
          <div class="bg-white rounded-xl border border-slate-200 p-6 animate-fade-in-right" style="animation-delay: 400ms">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-semibold text-slate-800 flex items-center">
                <FileText class="w-5 h-5 mr-2 text-slate-600" />
                Reportes Recientes
              </h3>
              <button class="text-xs text-slate-600 hover:text-slate-800 font-medium">Nuevo</button>
            </div>
            
            <div class="space-y-3">
              <div v-for="(report, index) in recentReports" :key="index"
                   class="flex items-center justify-between p-2 hover:bg-slate-50 rounded-lg transition-colors">
                <div class="flex items-center space-x-3">
                  <FileText class="w-4 h-4 text-slate-400" />
                  <div>
                    <p class="text-sm font-medium text-slate-800">{{ report.name }}</p>
                    <p class="text-xs text-slate-500">{{ report.date }}</p>
                  </div>
                </div>
                <button class="text-slate-400 hover:text-slate-600 transition-colors">
                  <Download class="w-4 h-4" />
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/authStore'
import {
  GraduationCap,
  Bell,
  Calendar,
  Download,
  Plus,
  Users,
  BookOpen,
  CheckSquare,
  MoreVertical,
  BarChart3,
  Zap,
  ChevronRight,
  FileText,
  Clock,
  Activity,
  Award,
  Calendar as CalendarIcon,
} from 'lucide-vue-next'
import router from '@/router'

const auth = useAuthStore()

onMounted(async () => {
  if (!auth.user && !auth.loading) {
    await auth.fetchUser()
  }
})

const useFallback = (event: Event) => {
  const img = (event.currentTarget ?? event.target) as HTMLImageElement | null
  const username = auth.user?.names ?? 'Usuario'
  const name = username.replace(/\s+/g, '+')
  if (img) {
    img.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&background=334155&color=fff&size=128`
  }
};

// Fecha actual
const currentDate = ref(new Date().toLocaleDateString('es-ES', { 
  weekday: 'long', 
  year: 'numeric', 
  month: 'long', 
  day: 'numeric' 
}))

// Estadísticas académicas
const academicStats = ref([
  { 
    icon: Users, 
    label: 'Total Estudiantes', 
    value: '127', 
    change: '+3 esta semana', 
    trend: 'up',
    period: 'este mes',
    progress: 85
  },
  { 
    icon: Activity, 
    label: 'Asistencia Promedio', 
    value: '94%', 
    change: '+2.5%', 
    trend: 'up',
    period: 'este mes',
    progress: 94
  },
  { 
    icon: FileText, 
    label: 'Reportes Generados', 
    value: '24', 
    change: '+8 este mes', 
    trend: 'up',
    period: 'este mes',
    progress: 60
  },
  { 
    icon: Award, 
    label: 'Evaluaciones', 
    value: '156', 
    change: '12 pendientes', 
    trend: 'neutral',
    period: 'este mes',
    progress: 45
  }
])

// Cursos del docente
const teacherCourses = ref([
  { 
    name: 'Matemáticas 5°B', 
    grade: '5° Básico',
    schedule: 'Lun-Mie 08:00-09:30',
    students: 32,
    progress: 92,
    color: 'slate',
    attendance: { present: 29, absent: 2, justified: 1 }
  },
  { 
    name: 'Matemáticas 6°A', 
    grade: '6° Básico',
    schedule: 'Mar-Jue 10:00-11:30',
    students: 28,
    progress: 88,
    color: 'slate',
    attendance: { present: 25, absent: 2, justified: 1 }
  },
  { 
    name: 'Ciencias 5°B', 
    grade: '5° Básico',
    schedule: 'Vie 08:00-09:30',
    students: 32,
    progress: 95,
    color: 'slate',
    attendance: { present: 30, absent: 1, justified: 1 }
  }
])

// Datos para el gráfico
const attendanceChart = ref([
  { label: 'Lun', value: 95 },
  { label: 'Mar', value: 88 },
  { label: 'Mie', value: 92 },
  { label: 'Jue', value: 96 },
  { label: 'Vie', value: 89 },
  { label: 'Lun', value: 94 },
  { label: 'Mar', value: 91 }
])

// Acciones rápidas docentes
const teacherActions = ref([
  {
    icon: CalendarIcon,
    title: 'Ver Asistencias',
    description: 'Revisar registros anteriores',
    path: '/attendances'
  },
  { 
    icon: CheckSquare, 
    title: 'Tomar Asistencia', 
    description: 'Registrar asistencia del día',
    path: '/markAttendance'
  },
  { 
    icon: Users, 
    title: 'Ver Estudiantes', 
    description: 'Listado completo de estudiantes',
    path: '/students'
  }
])

const navigateTo = (path: string) => {
  router.push(path)
}

// Próximos eventos
const upcomingEvents = ref([
  {
    title: 'Consejo de Profesores',
    month: 'MAR',
    day: '15',
    time: '14:30 - 16:00',
    location: 'Sala de reuniones'
  },
  {
    title: 'Entrega de Notas',
    month: 'MAR',
    day: '20',
    time: 'Todo el día',
    location: 'Plataforma online'
  },
  {
    title: 'Reunión de Apoderados',
    month: 'MAR',
    day: '22',
    time: '18:00 - 20:00',
    location: 'Auditorio'
  }
])

// Reportes recientes
const recentReports = ref([
  { name: 'Asistencia Marzo 2024', date: 'Hace 2 días' },
  { name: 'Rendimiento 5°B - Matemáticas', date: 'Hace 5 días' },
  { name: 'Informe Trimestral Ciencias', date: 'Hace 1 semana' }
])
</script>

<style scoped>
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

.animate-fade-in-down {
  animation: fadeInDown 0.6s ease-out;
}

.animate-fade-in-up {
  animation: fadeInUp 0.6s ease-out;
}

.animate-fade-in-left {
  animation: fadeInLeft 0.6s ease-out;
}

.animate-fade-in-right {
  animation: fadeInRight 0.6s ease-out;
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
</style>