<template>
<Header />
  <div class="min-h-screen bg-slate-50">
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="mb-8 animate-fade-in-down">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div class="flex items-center space-x-3">
            <div class="w-12 h-12 bg-slate-800 rounded-xl flex items-center justify-center shadow-lg">
              <Users class="w-6 h-6 text-white" />
            </div>
            <div>
              <h1 class="text-2xl font-bold text-slate-800">Usuarios Activos</h1>
              <p class="text-sm text-slate-500">Gestiona los usuarios del sistema</p>
              <p class="text-xs text-slate-400 mt-1">Escuela actual: {{ schoolLabel }}</p>
            </div>
          </div>

          <button
            @click="openCreateModal"
            class="inline-flex items-center space-x-2 px-4 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 transition-all duration-300 transform hover:scale-105 shadow-md"
          >
            <UserPlus class="w-4 h-4" />
            <span>Nuevo Usuario</span>
          </button>
        </div>
      </div>

      <transition name="fade">
        <div v-if="loading" class="flex flex-col items-center justify-center py-12 bg-white rounded-xl border border-slate-200">
          <Loader2 class="w-8 h-8 text-slate-400 animate-spin mb-3" />
          <p class="text-sm text-slate-500">Cargando usuarios...</p>
        </div>
      </transition>

      <transition name="fade">
        <div
          v-if="errorMessage"
          class="mb-6 bg-red-50 border border-red-200 rounded-xl p-4 flex items-start space-x-3"
        >
          <AlertCircle class="w-5 h-5 text-red-500 shrink-0 mt-0.5" />
          <div class="flex-1">
            <h3 class="text-sm font-medium text-red-800">Error al cargar los datos</h3>
            <p class="text-xs text-red-600 mt-1">{{ errorMessage }}</p>
          </div>
          <button @click="loadUsers" class="text-red-400 hover:text-red-600">
            <RotateCw class="w-4 h-4" />
          </button>
        </div>
      </transition>

      <div v-if="!loading && !errorMessage" class="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden animate-fade-in-up">
        <div class="overflow-x-auto">
          <table class="w-full table-fixed">
            <colgroup>
              <col class="w-[18%]" />
              <col class="w-[34%]" />
              <col class="w-[28%]" />
              <col class="w-[20%]" />
            </colgroup>
            <thead>
              <tr class="bg-slate-50 border-b border-slate-200">
                <th class="px-6 py-4 text-left text-xs font-semibold text-slate-600 uppercase tracking-wider">DNI</th>
                <th class="px-6 py-4 text-left text-xs font-semibold text-slate-600 uppercase tracking-wider">Nombre completo</th>
                <th class="px-6 py-4 text-left text-xs font-semibold text-slate-600 uppercase tracking-wider">Email</th>
                <th class="px-6 py-4 text-left text-xs font-semibold text-slate-600 uppercase tracking-wider">Rol</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-200">
              <tr
                v-for="user in paginatedUsers"
                :key="user.dni"
                class="hover:bg-slate-50 transition-colors duration-200"
              >
                <td class="px-6 py-4 whitespace-nowrap overflow-hidden text-ellipsis">
                  <span class="text-sm font-mono text-slate-600">{{ user.dni }}</span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap overflow-hidden text-ellipsis">
                  <div class="flex items-center min-w-0">
                    <div class="w-8 h-8 bg-slate-200 rounded-lg flex items-center justify-center mr-3 shrink-0">
                      <span class="text-xs font-bold text-slate-600">{{ getInitials(getDisplayName(user)) }}</span>
                    </div>
                    <div class="min-w-0">
                      <p class="text-sm font-medium text-slate-800 truncate">{{ getDisplayName(user) }}</p>
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap overflow-hidden text-ellipsis">
                  <div class="flex items-center space-x-1 min-w-0">
                    <Mail class="w-4 h-4 text-slate-400 shrink-0" />
                    <span class="text-sm text-slate-600 truncate">{{ user.email }}</span>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap overflow-hidden text-ellipsis">
                  <span :class="getUserTypeBadgeClass(user.role)" class="inline-flex px-2 py-1 text-xs rounded-full">
                    {{ formatUserType(user.role) }}
                  </span>
                </td>
              </tr>

              <tr v-if="filteredUsers.length === 0">
                <td colspan="4" class="px-6 py-12 text-center">
                  <div class="flex flex-col items-center">
                    <Users class="w-12 h-12 text-slate-300 mb-3" />
                    <p class="text-sm text-slate-500 mb-2">No hay usuarios registrados</p>
                    <button @click="openCreateModal" class="text-sm text-indigo-600 hover:text-indigo-800 font-medium">
                      + Crear primer usuario
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </main>

    <transition name="fade">
      <div
        v-if="showModal"
        class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4 z-50"
        @click.self="closeModal"
      >
        <div class="bg-white rounded-2xl max-w-md w-full p-6 animate-slide-up">
          <div class="flex items-center justify-between mb-4">
            <div class="flex items-center space-x-3">
              <div class="w-10 h-10 bg-amber-100 rounded-lg flex items-center justify-center">
                <UserPlus class="w-5 h-5 text-amber-600" />
              </div>
              <h3 class="text-lg font-semibold text-slate-800">Nuevo Usuario</h3>
            </div>
            <button @click="closeModal" class="p-2 hover:bg-slate-100 rounded-lg transition-colors">
              <X class="w-5 h-5 text-slate-500" />
            </button>
          </div>

          <form @submit.prevent="submitUser" class="space-y-4">
            <div class="space-y-1.5">
              <label for="names" class="block text-sm font-medium text-slate-700">
                Nombres <span class="text-red-500">*</span>
              </label>
              <input
                id="names"
                type="text"
                v-model="userForm.names"
                class="w-full px-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent"
                placeholder="Juan Carlos"
              />
            </div>

            <div class="space-y-1.5">
              <label for="firstLastName" class="block text-sm font-medium text-slate-700">
                Primer Apellido <span class="text-red-500">*</span>
              </label>
              <input
                id="firstLastName"
                type="text"
                v-model="userForm.firstLastName"
                class="w-full px-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent"
                placeholder="Pérez"
              />
            </div>

            <div class="space-y-1.5">
              <label for="secondLastName" class="block text-sm font-medium text-slate-700">
                Segundo Apellido
              </label>
              <input
                id="secondLastName"
                type="text"
                v-model="userForm.secondLastName"
                class="w-full px-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent"
                placeholder="García"
              />
            </div>

            <div class="space-y-1.5">
              <label for="email" class="block text-sm font-medium text-slate-700">
                Email <span class="text-red-500">*</span>
              </label>
              <input
                id="email"
                type="email"
                v-model="userForm.email"
                class="w-full px-4 py-2.5 bg-white border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-slate-800 focus:border-transparent"
                placeholder="usuario@colegio.edu"
              />
            </div>

            <div class="rounded-lg border border-slate-200 bg-slate-50 px-4 py-3">
              <p class="text-xs font-medium text-slate-500">Escuela asignada</p>
              <p class="text-sm font-semibold text-slate-800">{{ schoolLabel }}</p>
            </div>

            <div class="flex items-center justify-end space-x-3 pt-4">
              <button
                type="button"
                @click="closeModal"
                class="px-4 py-2 text-sm text-slate-600 hover:bg-slate-100 rounded-lg transition-colors"
              >
                Cancelar
              </button>
              <button
                type="submit"
                :disabled="submitting || !isFormValid"
                class="relative px-6 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 transition-all duration-300 transform hover:scale-105 shadow-md disabled:opacity-50 disabled:cursor-not-allowed flex items-center space-x-2"
              >
                <Loader2 v-if="submitting" class="w-4 h-4 animate-spin" />
                <Save v-else class="w-4 h-4" />
                <span>{{ submitting ? 'Guardando...' : 'Crear Usuario' }}</span>
              </button>
            </div>
          </form>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from 'vue'
import Header from '@/components/Header.vue'
import { useAuthStore } from '@/stores/authStore'
import { addProfessor, getUsers } from '@/services/userService'
import type { ProfessorRequest, UserInfoResponse } from '@/types/User'
import {
  Users,
  UserPlus,
  RotateCw,
  Loader2,
  AlertCircle,
  X,
  Mail,
  Save
} from 'lucide-vue-next'
import { useToast } from '@/composables/useToast'

const auth = useAuthStore()
const toast = useToast()

const loading = ref(false)
const submitting = ref(false)
const errorMessage = ref('')
const users = ref<UserInfoResponse[]>([])
const searchQuery = ref('')
const userTypeFilter = ref('')
const currentPage = ref(0)
const itemsPerPage = ref(10)
const showModal = ref(false)

const userForm = reactive<ProfessorRequest>({
  names: '',
  firstLastName: '',
  secondLastName: '',
  email: ''
})

const schoolLabel = computed(() => auth.user?.schoolName || 'Escuela no disponible')

const isFormValid = computed(() => {
  return (
    userForm.names.trim() !== '' &&
    userForm.firstLastName.trim() !== '' &&
    userForm.email.trim() !== ''
  )
})

const filteredUsers = computed(() => {
  let filtered = [...users.value]

  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(user => {
      const fullName = getDisplayName(user).toLowerCase()
      return fullName.includes(query) || user.email.toLowerCase().includes(query) || user.dni.toLowerCase().includes(query)
    })
  }

  if (userTypeFilter.value) {
    filtered = filtered.filter(user => user.role === userTypeFilter.value)
  }

  return filtered
})

const paginatedUsers = computed(() => {
  const start = currentPage.value * itemsPerPage.value
  const end = start + itemsPerPage.value
  return filteredUsers.value.slice(start, end)
})

watch([searchQuery, userTypeFilter], () => {
  currentPage.value = 0
})

const getDisplayName = (user: UserInfoResponse) => {
  return [user.name, user.firstLastName, user.secondLastName].filter(Boolean).join(' ').trim()
}

const getInitials = (fullName: string) => {
  const parts = fullName.split(/\s+/).filter(Boolean)

  if (parts.length === 0) return 'U'
  if (parts.length === 1) return (parts[0]?.charAt(0) ?? 'U').toUpperCase()

  const firstInitial = parts[0]?.charAt(0) ?? 'U'
  const secondInitial = parts[1]?.charAt(0) ?? ''

  return `${firstInitial}${secondInitial}`.toUpperCase()
}

const formatUserType = (type: string) => {
  const labels: Record<string, string> = {
    ADMIN: 'Administrador',
    PROFESSOR: 'Profesor'
  }

  return labels[type] || type
}

const getUserTypeBadgeClass = (type: string) => {
  const classes: Record<string, string> = {
    ADMIN: 'bg-purple-50 text-purple-700',
    PROFESSOR: 'bg-blue-50 text-blue-700'
  }

  return classes[type] || 'bg-slate-50 text-slate-700'
}

const openCreateModal = () => {
  resetForm()
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  resetForm()
}

const resetForm = () => {
  userForm.names = ''
  userForm.firstLastName = ''
  userForm.secondLastName = ''
  userForm.email = ''
}

const submitUser = async () => {
  if (!userForm.names.trim() || !userForm.secondLastName.trim() || !userForm.firstLastName.trim() || !userForm.email.trim()) {
    toast.showError('Campos incompletos', 'Por favor completa los campos obligatorios')
    return
  }
  submitting.value = true

  try {
    const response = await addProfessor(userForm)

    if (response.success) {
      toast.showSuccess('Usuario creado', 'El nuevo usuario ha sido creado exitosamente')
      await loadUsers()
    } else {
      toast.showError('Error al crear usuario', response.error.message)
    }
  } catch (error) {
    toast.showError('Error de conexión', error instanceof Error ? error.message : 'No se pudo conectar al servidor')
  } finally {
    submitting.value = false
  }
}

const loadUsers = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    const response = await getUsers()
    if (response.success) {
      users.value = response.data
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
  loadUsers()
})
</script>

<style scoped>
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

* {
  transition-property: background-color, border-color, color, fill, stroke, opacity, box-shadow, transform;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 200ms;
}

@media (prefers-reduced-motion: reduce) {
  * {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
  }
}
</style>