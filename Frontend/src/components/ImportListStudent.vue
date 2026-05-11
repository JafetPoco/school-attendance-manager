<!-- components/ImportListStudent.vue -->
<template>
  <div class="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden">
    <!-- Cabecera -->
    <div class="bg-slate-50 px-6 py-4 border-b border-slate-200">
      <div class="flex items-center space-x-3">
        <div class="w-8 h-8 bg-slate-800 rounded-lg flex items-center justify-center">
          <Upload class="w-4 h-4 text-white" />
        </div>
        <div>
          <h2 class="text-lg font-semibold text-slate-800">Importar Estudiantes</h2>
          <p class="text-xs text-slate-500">Sube un archivo Excel o CSV con la lista de estudiantes</p>
        </div>
      </div>
    </div>

    <div class="p-6">
      <!-- Área de subida de archivo -->
      <div class="relative">
        <div 
          class="border-2 border-dashed rounded-xl transition-all duration-200 p-8 text-center cursor-pointer"
          :class="[
            isDragging ? 'border-slate-800 bg-slate-50' : 'border-slate-200 hover:border-slate-300',
            fileError ? 'border-red-300 bg-red-50' : ''
          ]"
          @dragover.prevent="isDragging = true"
          @dragleave.prevent="isDragging = false"
          @drop.prevent="handleFileDrop"
          @click="triggerFileInput"
        >
          <input type="file"
                 ref="fileInput"
                 accept=".xlsx,.xls,.csv"
                 class="hidden"
                 @change="handleFileSelect">
          
          <Upload class="w-12 h-12 text-slate-400 mx-auto mb-3" />
          <p class="text-sm text-slate-600 mb-1">
            Arrastra tu archivo aquí o <span class="text-slate-800 font-medium">selecciona un archivo</span>
          </p>
          <p class="text-xs text-slate-400">
            Formatos permitidos: Excel (.xlsx, .xls)
          </p>
        </div>
      </div>

      <!-- Archivo seleccionado -->
      <div v-if="selectedFile" class="mt-4">
        <div class="flex items-center justify-between p-3 bg-slate-50 rounded-lg border border-slate-200">
          <div class="flex items-center space-x-3">
            <div class="w-10 h-10 bg-white rounded-lg flex items-center justify-center shadow-sm">
              <FileSpreadsheet class="w-5 h-5 text-slate-600" />
            </div>
            <div>
              <p class="text-sm font-medium text-slate-800">{{ selectedFile.name }}</p>
              <p class="text-xs text-slate-500">{{ formatFileSize(selectedFile.size) }}</p>
            </div>
          </div>
          <button type="button"
                  @click="removeFile"
                  class="p-2 text-slate-400 hover:text-red-500 hover:bg-red-50 rounded-lg transition-colors">
            <X class="w-4 h-4" />
          </button>
        </div>
      </div>

      <!-- Estado de carga con progreso y tiempo transcurrido -->
      <div v-if="uploading" class="mt-4">
        <div class="flex items-center justify-between text-sm mb-1">
          <div class="flex items-center space-x-2">
            <Loader2 class="w-4 h-4 animate-spin text-slate-600" />
            <span class="text-slate-600">{{ statusMessage }}</span>
          </div>
          <span class="text-slate-600">{{ uploadProgress }}%</span>
        </div>
        <div class="w-full h-2 bg-slate-200 rounded-full overflow-hidden">
          <div class="h-full bg-slate-800 rounded-full transition-all duration-300"
               :style="{ width: uploadProgress + '%' }"></div>
        </div>
        
        <!-- Tiempo transcurrido y advertencia -->
        <div class="mt-2 flex items-center justify-between text-xs">
          <div class="text-slate-500">
            Tiempo transcurrido: {{ formatTime(elapsedTime) }}
          </div>
          <div v-if="showTimeWarning" class="text-amber-600 flex items-center space-x-1">
            <AlertCircle class="w-3 h-3" />
            <span>La importación está tomando más tiempo de lo esperado</span>
          </div>
        </div>

        <!-- Mensaje de paciencia para procesos largos -->
        <div v-if="uploadProgress > 50 && !showTimeWarning" class="mt-2 text-xs text-slate-400 text-center">
          <i class="fas fa-info-circle"></i> Procesando, por favor no cierres esta ventana
        </div>
      </div>

      <!-- Mensaje de error -->
      <p v-if="fileError" class="text-xs text-red-500 flex items-center mt-2">
        <AlertCircle class="w-3 h-3 mr-1" />
        {{ fileError }}
      </p>

      <!-- Plantilla de ejemplo -->
      <div class="mt-6 p-4 bg-slate-50 rounded-lg border border-slate-200">
        <div class="flex items-center justify-between">
          <div class="flex items-center space-x-2">
            <Download class="w-4 h-4 text-slate-500" />
            <span class="text-sm font-medium text-slate-700">¿Necesitas una plantilla?</span>
          </div>
          <button @click="downloadTemplate"
                  :disabled="downloadingTemplate"
                  class="text-sm text-indigo-600 hover:text-indigo-800 font-medium disabled:opacity-50 disabled:cursor-not-allowed">
            {{ downloadingTemplate ? 'Descargando...' : 'Descargar plantilla de ejemplo →' }}
          </button>
        </div>
        <p class="text-xs text-slate-500 mt-2">
          Descarga nuestra plantilla de ejemplo para asegurar que el formato sea el correcto
        </p>
      </div>

      <!-- Botones de acción -->
      <div class="flex items-center justify-end space-x-3 mt-6 pt-4 border-t border-slate-200">
        <button type="button"
                @click="resetUpload"
                :disabled="uploading"
                class="px-4 py-2 text-sm text-slate-600 hover:bg-slate-100 rounded-lg border border-slate-200 transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed">
          Cancelar
        </button>
        <button @click="uploadFile"
                :disabled="!selectedFile || uploading"
                class="relative px-6 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 transition-all duration-300 transform hover:scale-105 shadow-md disabled:opacity-50 disabled:cursor-not-allowed flex items-center space-x-2">
          <Loader2 v-if="uploading" class="w-4 h-4 animate-spin" />
          <Upload v-else class="w-4 h-4" />
          <span>{{ uploading ? 'Procesando...' : 'Importar Estudiantes' }}</span>
        </button>
      </div>
    </div>

    <!-- Modal de progreso detallado (opcional para procesos muy largos) -->
    <transition name="fade">
      <div v-if="showProgressModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-xl p-6 max-w-md w-full mx-4 shadow-xl">
          <div class="text-center">
            <div class="w-16 h-16 bg-slate-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <Loader2 class="w-8 h-8 animate-spin text-slate-800" />
            </div>
            <h3 class="text-lg font-semibold text-slate-800 mb-2">Importando estudiantes</h3>
            <p class="text-sm text-slate-600 mb-4">{{ statusMessage }}</p>
            
            <div class="mb-4">
              <div class="flex justify-between text-sm mb-1">
                <span>Progreso</span>
                <span>{{ uploadProgress }}%</span>
              </div>
              <div class="w-full h-2 bg-slate-200 rounded-full overflow-hidden">
                <div class="h-full bg-slate-800 rounded-full transition-all duration-300"
                     :style="{ width: uploadProgress + '%' }"></div>
              </div>
            </div>
            
            <p class="text-xs text-slate-400">
              Tiempo transcurrido: {{ formatTime(elapsedTime) }}
            </p>
            
            <button @click="cancelUpload"
                    class="mt-4 px-4 py-2 text-sm text-slate-600 hover:bg-slate-100 rounded-lg border border-slate-200">
              Cancelar
            </button>
          </div>
        </div>
      </div>
    </transition>

    <!-- Mensaje de éxito -->
    <transition name="slide-up">
      <div v-if="successMessage" 
           class="fixed bottom-4 right-4 bg-emerald-600 text-white rounded-lg shadow-lg p-4 flex items-center space-x-3 z-50">
        <CheckCircle class="w-5 h-5" />
        <div>
          <p class="text-sm font-medium">{{ successMessage.title }}</p>
          <p class="text-xs opacity-90">{{ successMessage.message }}</p>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, onUnmounted } from 'vue'
import { useToast } from '@/composables/useToast'
import { Upload, FileSpreadsheet, X, Loader2, CheckCircle, Download, AlertCircle } from 'lucide-vue-next'
import { getTemplate, importStudentsExcel } from '@/services/importStudents'

const toast = useToast()

const fileInput = ref<HTMLInputElement | null>(null)
const selectedFile = ref<File | null>(null)
const uploading = ref(false)
const uploadProgress = ref(0)
const fileError = ref('')
const isDragging = ref(false)
const downloadingTemplate = ref(false)
const successMessage = ref<{ title: string; message: string } | null>(null)
const showProgressModal = ref(false)
const statusMessage = ref('Preparando archivo...')
const elapsedTime = ref(0)
const showTimeWarning = ref(false)

// Timers
let progressInterval: number | null = null
let elapsedTimeInterval: number | null = null
let warningTimeout: number | null = null
let uploadStartTime = 0

const formatFileSize = (bytes: number) => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return Number.parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

const formatTime = (seconds: number) => {
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  if (mins > 0) {
    return `${mins} minuto${mins !== 1 ? 's' : ''} ${secs} segundos`
  }
  return `${secs} segundo${secs !== 1 ? 's' : ''}`
}

const validateFile = (file: File) => {
  const maxSize = 10 * 1024 * 1024 // 10MB
  const allowedTypes = [
    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    'application/vnd.ms-excel',
    'text/csv',
    'application/csv'
  ]
  
  if (file.size > maxSize) {
    fileError.value = 'El archivo no debe exceder los 10MB'
    return false
  }
  
  if (!allowedTypes.includes(file.type)) {
    fileError.value = 'Formato no permitido. Usa: Excel (.xlsx, .xls) o CSV (.csv)'
    return false
  }
  
  fileError.value = ''
  return true
}

const updateProgressSimulation = () => {
  if (!uploading.value) return
  
  // Simular progreso para feedback visual
  if (uploadProgress.value < 95) {
    const increment = Math.random() * 3
    uploadProgress.value = Math.min(uploadProgress.value + increment, 95)
    
    // Actualizar mensaje según progreso
    if (uploadProgress.value < 30) {
      statusMessage.value = 'Validando archivo...'
    } else if (uploadProgress.value < 60) {
      statusMessage.value = 'Procesando estudiantes...'
    } else if (uploadProgress.value < 90) {
      statusMessage.value = 'Guardando información...'
    } else {
      statusMessage.value = 'Finalizando...'
    }
  }
}

const startTimers = () => {
  uploadStartTime = Date.now()
  
  // Simular progreso
  progressInterval = globalThis.setInterval(updateProgressSimulation, 2000)
  
  // Contador de tiempo transcurrido
  elapsedTimeInterval = globalThis.setInterval(() => {
    if (uploading.value) {
      elapsedTime.value = Math.floor((Date.now() - uploadStartTime) / 1000)
    }
  }, 1000)
  
  // Advertencia después de 30 segundos
  warningTimeout = globalThis.setTimeout(() => {
    if (uploading.value) {
      showTimeWarning.value = true
      toast.showWarning(
        'Proceso prolongado',
        'La importación está tomando más tiempo de lo esperado. Por favor continúa esperando.'
      )
    }
  }, 30000)
}

const stopTimers = () => {
  if (progressInterval) {
    clearInterval(progressInterval)
    progressInterval = null
  }
  if (elapsedTimeInterval) {
    clearInterval(elapsedTimeInterval)
    elapsedTimeInterval = null
  }
  if (warningTimeout) {
    clearTimeout(warningTimeout)
    warningTimeout = null
  }
}

const handleFileSelect = (event: Event) => {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  if (file && validateFile(file)) {
    selectedFile.value = file
    fileError.value = ''
  } else if (file) {
    selectedFile.value = null
  }
  isDragging.value = false
}

const handleFileDrop = (event: DragEvent) => {
  const file = event.dataTransfer?.files[0]
  if (file && validateFile(file)) {
    selectedFile.value = file
    fileError.value = ''
  } else if (file) {
    selectedFile.value = null
  }
  isDragging.value = false
}

const triggerFileInput = () => {
  if (!uploading.value) {
    fileInput.value?.click()
  }
}

const removeFile = () => {
  if (!uploading.value) {
    selectedFile.value = null
    if (fileInput.value) {
      fileInput.value.value = ''
    }
    fileError.value = ''
  }
}

const resetUpload = () => {
  if (!uploading.value) {
    removeFile()
    uploadProgress.value = 0
    statusMessage.value = 'Preparando archivo...'
    elapsedTime.value = 0
    showTimeWarning.value = false
  }
}

const cancelUpload = () => {
  if (uploading.value) {
    uploading.value = false
    stopTimers()
    uploadProgress.value = 0
    statusMessage.value = 'Importación cancelada'
    setTimeout(() => {
      statusMessage.value = 'Preparando archivo...'
      showProgressModal.value = false
    }, 2000)
  }
}

const uploadFile = async () => {
  if (!selectedFile.value) {
    fileError.value = 'Selecciona un archivo para importar'
    return
  }

  if (!validateFile(selectedFile.value)) {
    return
  }
  
  uploading.value = true
  uploadProgress.value = 0
  fileError.value = ''
  statusMessage.value = 'Iniciando importación...'
  elapsedTime.value = 0
  showTimeWarning.value = false
  
  startTimers()
  
  try {
    const result = await importStudentsExcel(selectedFile.value)
    
    // Completar progreso
    uploadProgress.value = 100
    statusMessage.value = '¡Completado!'
    
    stopTimers()

    if (!result.success) {
      const backendMessage = result.error.errorResponse?.message
      const errorMessage = backendMessage || result.error.message || 'No se pudo importar el archivo'
      fileError.value = errorMessage
      toast.showError('Error en importación', errorMessage)
      uploading.value = false
      showProgressModal.value = false
      return
    }

    toast.showSuccess(result.data.status, result.data.message)

    setTimeout(() => {
      successMessage.value = null
    }, 5000)

    // Pequeña demora para mostrar el 100%
    setTimeout(() => {
      uploading.value = false
      showProgressModal.value = false
      removeFile()
      uploadProgress.value = 0
      // Emitir evento para recargar lista de estudiantes
      globalThis.dispatchEvent(new CustomEvent('students-imported'))
    }, 1000)
    
  } catch (error: any) {
    stopTimers()
    
    let errorMessage = 'Ocurrió un problema al importar el archivo'
    
    // Manejar error de timeout específicamente
    if (error.name === 'AbortError' || error.message?.includes('timeout')) {
      errorMessage = 'La importación está tomando demasiado tiempo. Por favor, intenta con un archivo más pequeño o contacta a soporte.'
    } else if (error.message) {
      errorMessage = error.message
    }
    
    fileError.value = errorMessage
    toast.showError('Error en importación', errorMessage)
    uploading.value = false
    showProgressModal.value = false
    uploadProgress.value = 0
  }
}

const downloadTemplate = async () => {
  downloadingTemplate.value = true

  try {
    const result = await getTemplate()

    if (!result.success) {
      const backendMessage = result.error.errorResponse?.message
      const errorMessage = backendMessage || result.error.message || 'No se pudo descargar la plantilla'
      toast.showError('Error', errorMessage)
      return
    }

    const url = URL.createObjectURL(result.data)
    const link = document.createElement('a')
    link.href = url
    link.download = 'studentsTemplate.xlsx'
    document.body.appendChild(link)
    link.click()
    link.remove()
    URL.revokeObjectURL(url)

    toast.showSuccess('Descarga completada', 'La plantilla se descargó correctamente')
  } catch {
    toast.showError('Error', 'No se pudo descargar la plantilla')
  } finally {
    downloadingTemplate.value = false
  }
}

// Limpiar timers al desmontar componente
onUnmounted(() => {
  stopTimers()
})
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

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.animate-spin {
  animation: spin 1s linear infinite;
}
</style>