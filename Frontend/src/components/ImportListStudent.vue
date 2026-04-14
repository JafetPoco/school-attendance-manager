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

      <!-- Estado de carga -->
      <div v-if="uploading" class="mt-4">
        <div class="flex items-center justify-between text-sm mb-1">
          <span class="text-slate-600">Subiendo archivo...</span>
          <span class="text-slate-600">{{ uploadProgress }}%</span>
        </div>
        <div class="w-full h-2 bg-slate-200 rounded-full overflow-hidden">
          <div class="h-full bg-slate-800 rounded-full transition-all duration-300"
               :style="{ width: uploadProgress + '%' }"></div>
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
                class="px-4 py-2 text-sm text-slate-600 hover:bg-slate-100 rounded-lg border border-slate-200 transition-all duration-300">
          Cancelar
        </button>
        <button @click="uploadFile"
                :disabled="!selectedFile || uploading"
                class="relative px-6 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 transition-all duration-300 transform hover:scale-105 shadow-md disabled:opacity-50 disabled:cursor-not-allowed flex items-center space-x-2">
          <Loader2 v-if="uploading" class="w-4 h-4 animate-spin" />
          <Upload v-else class="w-4 h-4" />
          <span>{{ uploading ? 'Subiendo...' : 'Importar Estudiantes' }}</span>
        </button>
      </div>
    </div>

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
import { ref } from 'vue'
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

const formatFileSize = (bytes: number) => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return Number.parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
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

const handleFileSelect = (event: Event) => {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  if (file && validateFile(file)) {
    selectedFile.value = file
  } else if (file) {
    selectedFile.value = null
  }
  isDragging.value = false
}

const handleFileDrop = (event: DragEvent) => {
  const file = event.dataTransfer?.files[0]
  if (file && validateFile(file)) {
    selectedFile.value = file
  } else if (file) {
    selectedFile.value = null
  }
  isDragging.value = false
}

const triggerFileInput = () => {
  fileInput.value?.click()
}

const removeFile = () => {
  selectedFile.value = null
  if (fileInput.value) {
    fileInput.value.value = ''
  }
  fileError.value = ''
}

const resetUpload = () => {
  removeFile()
  uploadProgress.value = 0
}

const uploadFile = async () => {
  if (!selectedFile.value) {
    fileError.value = 'Selecciona un archivo para importar'
    return
  }

  if (!validateFile(selectedFile.value)) {
    return
  }

  const fileName = selectedFile.value.name
  
  uploading.value = true
  uploadProgress.value = 30
  fileError.value = ''
  
  try {
    const result = await importStudentsExcel(selectedFile.value)
    uploadProgress.value = 100

    if (!result.success) {
      const backendMessage = result.error.errorResponse?.message
      const errorMessage = backendMessage || result.error.message || 'No se pudo importar el archivo'
      fileError.value = errorMessage
      toast.showError('Error en importación', errorMessage)
      return
    }

    successMessage.value = {
      title: '¡Importación exitosa!',
      message: `Se importó correctamente el archivo ${fileName}`
    }

    toast.showSuccess('Importación exitosa', 'Los estudiantes fueron importados correctamente')

    setTimeout(() => {
      successMessage.value = null
    }, 5000)

    removeFile()
  } catch {
    const errorMessage = 'Ocurrió un problema al importar el archivo'
    fileError.value = errorMessage
    toast.showError('Error en importación', errorMessage)
  } finally {
    uploading.value = false
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
</style>