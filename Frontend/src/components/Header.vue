<template>
  <header class="bg-white border-b border-slate-200 sticky z-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between items-center h-16">
        <!-- Logo y nombre del colegio -->
        <div class="flex items-center space-x-3">
          <div class="w-10 h-10 bg-slate-800 rounded-xl flex items-center justify-center shadow-md">
            <GraduationCap class="w-6 h-6 text-white" />
          </div>
          <div class="hidden sm:block">
            <span class="text-lg font-semibold text-slate-800">{{ auth.user?.schoolName }}</span>
            <p class="text-xs text-slate-500">Portal Docente</p>
          </div>
        </div>

        <!-- Navegación rápida (visible en desktop) -->
        <nav class="hidden md:flex items-center space-x-1" aria-label="Main navigation">
          <button v-for="item in navItems" :key="item.path"
                  @click="navigateTo(item.path)"
                  class="px-4 py-2 rounded-lg text-sm font-medium transition-all duration-200 flex items-center space-x-2"
                  :class="[isActive(item.path) ? 'bg-slate-100 text-slate-800' : 'text-slate-600 hover:bg-slate-50 hover:text-slate-800']">
            <component :is="item.icon" class="w-4 h-4" />
            <span>{{ item.label }}</span>
          </button>
        </nav>

        <!-- Acciones rápidas y perfil -->
        <div class="flex items-center space-x-3">

          <!-- Perfil del docente -->
          <div class="flex items-center space-x-3 cursor-pointer group relative"
               @click="toggleUserMenu">
            <div class="text-right hidden sm:block">
              <p class="text-sm font-semibold text-slate-800">{{ auth.user?.names }}</p>
              <p class="text-xs text-slate-500">{{ auth.user?.userType }}</p>
            </div>
            <div class="relative">
              <img class="h-9 w-9 rounded-xl border-2 border-slate-200 group-hover:border-slate-400 transition-all duration-200" 
                   :src="auth.user?.urlPicture || 'https://ui-avatars.com/api/?name=' + encodeURIComponent(auth.user?.names || 'User') + '&background=0D8ABC&color=fff&size=128'" 
                   :alt="auth.user?.names">
              <div class="absolute -bottom-1 -right-1 w-3.5 h-3.5 bg-green-500 rounded-full border-2 border-white"></div>
            </div>

            <!-- Menú desplegable del usuario -->
            <transition name="fade">
              <div v-if="showUserMenu" 
                   class="absolute right-0 top-12 w-64 bg-white rounded-xl border border-slate-200 shadow-lg py-2 z-50"
                   @click.stop>
                <div class="px-4 py-3 border-b border-slate-100">
                  <p class="text-sm font-semibold text-slate-800">{{ auth.user?.names }}</p>
                  <p class="text-xs text-slate-500">{{ auth.user?.email }}</p>
                </div>
                
                <div class="py-2">
                  <button v-for="item in userMenuItems" :key="item.label"
                          @click="handleUserMenuItem(item)"
                          class="w-full px-4 py-2 text-sm text-left hover:bg-slate-50 flex items-center space-x-3 transition-colors">
                    <component :is="item.icon" class="w-4 h-4 text-slate-500" />
                    <span class="text-slate-700">{{ item.label }}</span>
                  </button>
                </div>
                
                <div class="border-t border-slate-100 py-2">
                  <button @click="logout"
                          class="w-full px-4 py-2 text-sm text-left hover:bg-slate-50 flex items-center space-x-3 text-red-600 transition-colors">
                    <LogOut class="w-4 h-4" />
                    <span>Cerrar sesión</span>
                  </button>
                </div>
              </div>
            </transition>
          </div>

          <!-- Botón menú móvil -->
          <button @click="toggleMobileMenu"
                  class="md:hidden p-2 text-slate-600 hover:bg-slate-100 rounded-lg transition-colors">
            <Menu class="w-5 h-5" />
          </button>
        </div>
      </div>

      <!-- Menú móvil -->
      <transition name="slide">
        <div v-if="showMobileMenu" 
             class="md:hidden border-t border-slate-200 py-4 px-2 bg-white">
          <nav class="grid grid-cols-2 gap-2" aria-label="Mobile navigation">
            <button v-for="item in navItems" :key="item.path"
                    @click="navigateTo(item.path)"
                    class="px-4 py-3 rounded-lg text-sm font-medium transition-all duration-200 flex flex-col items-center space-y-1"
                    :class="[isActive(item.path) ? 'bg-slate-100 text-slate-800' : 'text-slate-600 hover:bg-slate-50']">
              <component :is="item.icon" class="w-5 h-5" />
              <span>{{ item.label }}</span>
            </button>
          </nav>
        </div>
      </transition>
    </div>

    <!-- Barra de progreso/indicador de vista actual (opcional) -->
    <div class="h-0.5 bg-slate-100">
      <div class="h-full bg-slate-800 transition-all duration-300"
           :style="{ width: progressWidth }"></div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  GraduationCap,
  User,
  Settings,
  LogOut,
  Menu,
  Home,
  Users,
  ClipboardCheck,
  BarChart3,
  Moon,
} from 'lucide-vue-next'
import { useAuthStore } from '@/stores/authStore';

const auth = useAuthStore()

// Emits
const emit = defineEmits<{
  (e: 'navigate', path: string): void
  (e: 'action', action: string): void
  (e: 'logout'): void
}>()

// Router
const router = useRouter()
const route = useRoute()

// Estado
const showUserMenu = ref(false)
const showMobileMenu = ref(false)
const isDarkMode = ref(false)

// Items de navegación
const navItems = [
  { path: '/dashboard', label: 'Inicio', icon: Home },
  { path: '/students', label: 'Estudiantes', icon: Users },
  { path: '/attendances', label: 'Asistencias', icon: ClipboardCheck },
  { path: '/reports', label: 'Reportes', icon: BarChart3 },
]

// Items del menú de usuario
const userMenuItems = [
  { icon: User, label: 'Mi Perfil', action: 'profile' },
  { icon: Settings, label: 'Configuración', action: 'settings' },
  { icon: Moon, label: 'Modo Oscuro', action: 'theme' }
]

// Progreso de la vista actual (ejemplo)
const progressWidth = computed(() => {
  // Esto podría calcularse según la posición del scroll o la vista actual
  return '0%'
})

// Verificar si la ruta está activa
const isActive = (path: string) => {
  return route.path === path || route.path.startsWith(path + '/')
}

// Navegación
const navigateTo = (path: string) => {
  router.push(path)
  emit('navigate', path)
  showMobileMenu.value = false
  showUserMenu.value = false
}

// Menú de usuario
const toggleUserMenu = () => {
  showUserMenu.value = !showUserMenu.value
  if (showUserMenu.value) {
    showMobileMenu.value = false
  }
}

const handleUserMenuItem = (item: any) => {
  if (item.action === 'theme') {
    isDarkMode.value = !isDarkMode.value
    document.documentElement.classList.toggle('dark')
  } else {
    console.log('Acción de usuario:', item.action)
    router.push(`/${item.action}`)
  }
  showUserMenu.value = false
}

const logout = () => {
  emit('logout')
  console.log('Cerrando sesión...')
  // Aquí iría la lógica de logout
}

// Menú móvil
const toggleMobileMenu = () => {
  showMobileMenu.value = !showMobileMenu.value
  if (showMobileMenu.value) {
    showUserMenu.value = false
  }
}

// Cerrar menús al hacer clic fuera
const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as HTMLElement
  if (!target.closest('.group')) {
    showUserMenu.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
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

.slide-enter-active,
.slide-leave-active {
  transition: all 0.3s ease;
}

.slide-enter-from,
.slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* Tooltips */
.group:hover .group-hover\:opacity-100 {
  opacity: 1;
}

/* Transiciones suaves */
* {
  transition-property: background-color, border-color, color, fill, stroke, opacity, box-shadow, transform;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 200ms;
}

/* Estilos para selects */
select {
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 20 20'%3e%3cpath stroke='%236b7280' stroke-linecap='round' stroke-linejoin='round' stroke-width='1.5' d='M6 8l4 4 4-4'/%3e%3c/svg%3e");
  background-position: right 0.5rem center;
  background-repeat: no-repeat;
  background-size: 1.5em 1.5em;
  padding-right: 2.5rem;
  -webkit-print-color-adjust: exact;
  print-color-adjust: exact;
  appearance: none;
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