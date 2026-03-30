<template>
  <div class="min-h-screen bg-linear-to-br from-gray-900 via-gray-800 to-gray-900 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-3xl mx-auto">

      <div v-if="loading" class="flex justify-center items-center py-12">
        <Loader2 class="animate-spin h-12 w-12 text-blue-400" />
      </div>

      <div v-if="!loading" class="bg-linear-to-br from-slate-200 to-gray-200 rounded-2xl shadow-xl overflow-hidden">
        <!-- Encabezado -->
        <div class="bg-linear-to-r from-gray-800 to-gray-900 px-8 py-6">
          <div class="flex items-center space-x-3">
            <FileText class="w-8 h-8 text-blue-400" />
            <div>
              <h1 class="text-3xl font-bold text-white tracking-tight">Justificaciones</h1>
              <p class="text-gray-300 mt-1">Formulario de justificación de inasistencia</p>
            </div>
          </div>
        </div>

        <!-- Contenido -->
        <div class="p-8">
          <!-- Mensaje de bienvenida -->
          <div class="mb-8 p-6 bg-gray-50 rounded-xl border border-gray-200">
            <div class="flex items-start space-x-3">
              <Calendar class="w-5 h-5 text-blue-600 mt-0.5 shrink-0" />
              <p class="text-gray-700 text-lg leading-relaxed">
                Apreciado padre de familia, el siguiente formulario corresponde para la justificación de la inasistencia del día 
                <span class="inline-flex items-center px-2 py-0.5 bg-blue-100 text-blue-800 font-semibold rounded-md">
                  {{ attendanceInfo?.date }}
                </span> 
                de su menor hijo.
              </p>
            </div>
          </div>

          <!-- Información del estudiante -->
          <div class="grid md:grid-cols-2 gap-6 mb-8">
            <div class="space-y-2">
              <label class="flex items-center text-sm font-semibold text-gray-700 uppercase tracking-wider">
                <User class="w-4 h-4 mr-2" />
                Nombre Completo
              </label>
              <p class="text-gray-900 text-lg font-medium bg-gray-50 px-4 py-3 rounded-lg border border-gray-200">
                {{ attendanceInfo?.fullName }}
              </p>
            </div>
            <div class="space-y-2">
              <label class="flex items-center text-sm font-semibold text-gray-700 uppercase tracking-wider">
                <GraduationCap class="w-4 h-4 mr-2" />
                Grado
              </label>
              <p class="text-gray-900 text-lg font-medium bg-gray-50 px-4 py-3 rounded-lg border border-gray-200">
                {{ attendanceInfo?.grade }}
              </p>
            </div>
          </div>

          <!-- Formulario -->
          <form @submit.prevent="onSubmit" class="space-y-6">
            <div class="space-y-2">
              <label for="description" class="flex items-center text-sm font-semibold text-gray-700 uppercase tracking-wider">
                <FileText class="w-4 h-4 mr-2" />
                Descripción de la justificación
                <span class="text-red-500 ml-1">*</span>
              </label>
              <textarea 
                id="description" 
                name="description" 
                rows="5"
                class="w-full px-4 py-3 border border-gray-300 rounded-lg text-gray-900 placeholder-gray-400 focus:outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all duration-200 resize-none"
                placeholder="Por favor, describa detalladamente el motivo de la inasistencia..."
                v-model="formData.description"
                required
              ></textarea>
            </div>

            <div class="space-y-2">
              <label for="evidence" class="flex items-center text-sm font-semibold text-gray-700 uppercase tracking-wider">
                <Link2 class="w-4 h-4 mr-2" />
                URL de evidencia
                <span class="text-gray-500 text-xs font-normal ml-2">(opcional)</span>
              </label>
              <div class="relative">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <Link class="w-5 h-5 text-gray-400" />
                </div>
                <input 
                  type="url" 
                  id="evidence" 
                  name="evidence" 
                  class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg text-gray-900 placeholder-gray-400 focus:outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all duration-200"
                  placeholder="https://ejemplo.com/documento.pdf"
                  v-model="formData.urlEvidence"
                >
              </div>
              <p class="text-xs text-gray-500 mt-1">
                Puede agregar enlace a Google Drive, Dropbox, o cualquier evidencia digital
              </p>
            </div>

            <!-- Botón de envío -->
            <div class="pt-4">
              <button 
                type="submit" 
                class="w-full bg-gray-800 hover:bg-gray-900 text-white font-semibold py-3 px-6 rounded-lg transition-all duration-200 transform hover:scale-[1.02] focus:outline-none focus:ring-2 focus:ring-gray-800 focus:ring-offset-2 shadow-md"
                :disabled="loading"
              >
                <span v-if="!loading" class="flex items-center justify-center">
                  <Send class="w-5 h-5 mr-2" />
                  Enviar Justificación
                </span>
                <span v-else class="flex items-center justify-center">
                  <Loader2 class="animate-spin h-5 w-5 mr-2" />
                  Enviando...
                </span>
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>

  <!-- Modal Justificacion exitosa -->
  <transition name="fade" v-if="justificationResponse">
    <div class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4 z-50"
         @click.self="justificationResponse = null">
      <div class="bg-white rounded-2xl max-w-md w-full p-6 animate-slide-up">
        <div class="flex items-center justify-between mb-4">
          <div class="flex items-center space-x-3">
            <div class="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center">
              <CheckCircle class="w-5 h-5 text-green-600" />
            </div>
            <h3 class="text-lg font-semibold text-slate-800">Justificación exitosa</h3>
          </div>
          <button @click="justificationResponse = null" 
                  class="p-2 hover:bg-slate-100 rounded-lg transition-colors">
            <X class="w-5 h-5 text-slate-500" />
          </button>
        </div>
        
        <p class="text-sm text-slate-600 mb-3">
          Se registro correctamente la justificación de inasistencia para el estudiante 
          <span class="font-semibold">{{ justificationResponse?.studentName }}</span> 
           del día <span class="font-semibold">{{ justificationResponse?.attendanceDate }}</span>.
        </p>
      </div>
    </div>
  </transition>

  <!-- Modal Error -->
  <transition name="fade" v-if="errorMessage">
    <div class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4 z-50"
         @click.self="errorMessage = ''">
      <div class="bg-white rounded-2xl max-w-md w-full p-6 animate-slide-up">
        <div class="flex items-center justify-between mb-4">
          <div class="flex items-center space-x-3">
            <div class="w-10 h-10 bg-red-100 rounded-lg flex items-center justify-center">
              <AlertCircle class="w-5 h-5 text-red-600" />
            </div>
            <h3 class="text-lg font-semibold text-slate-800">Error</h3>
          </div>
          <button @click="errorMessage = ''" 
                  class="p-2 hover:bg-slate-100 rounded-lg transition-colors">
            <X class="w-5 h-5 text-slate-500" />
          </button>
        </div>
        
        <p class="text-sm text-slate-600 mb-3">
          {{ errorMessage }}
        </p>
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { addJustification, getJustificationFormInfo } from '@/services/justificationsService';
import type { AttendanceInfoResponse } from '@/types/Attendance';
import type { JustificationRequest, JustificationResponse } from '@/types/Justification';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import {
  AlertCircle,
  Loader2,
  CheckCircle,
  FileText,
  Calendar,
  User,
  GraduationCap,
  Link2,
  Link,
  Send,
  X
} from 'lucide-vue-next';

const route = useRoute()

const loading = ref(false)
const errorMessage = ref('')
const attendanceInfo = ref<AttendanceInfoResponse | null>(null)
const formData = ref<JustificationRequest>({
    token: route.params.token as string,
    idAttendance: 0n,
    description: '',
    urlEvidence: ''
})
const justificationResponse = ref<JustificationResponse | null>(null)

onMounted(async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    const token = route.params.token as string
    const response = await getJustificationFormInfo(token)

    if (response.success) {
      attendanceInfo.value = response.data
      formData.value.idAttendance = response.data.id
    } else {
      errorMessage.value = response.error.message
    }
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Error de conexión'
  } finally {
    loading.value = false
  }
})

const onSubmit = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    const response = await addJustification(formData.value)

    if (response.success) {
      justificationResponse.value = response.data
    } else {
      errorMessage.value = response.error.message
    }
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Error de conexión'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
@keyframes fade-in {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fade-in {
  animation: fade-in 0.3s ease-out;
}

/* Estilos personalizados para el scrollbar en modo oscuro */
textarea::-webkit-scrollbar {
  width: 8px;
}

textarea::-webkit-scrollbar-track {
  background: rgba(31, 41, 55, 0.5);
  border-radius: 4px;
}

textarea::-webkit-scrollbar-thumb {
  background: rgba(59, 130, 246, 0.5);
  border-radius: 4px;
}

textarea::-webkit-scrollbar-thumb:hover {
  background: rgba(59, 130, 246, 0.7);
}
</style>