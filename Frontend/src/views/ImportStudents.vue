<template>
    <section class="max-w-2xl mx-auto p-6 space-y-6">
        <header>
            <h1 class="text-2xl font-semibold text-slate-800">Importar con Excel</h1>
            <p class="text-sm text-slate-500 mt-1">Selecciona un archivo .xlsx o .xls para registrar apoderados y alumnos.</p>
        </header>

        <div class="bg-white border border-slate-200 rounded-xl p-5 space-y-4 shadow-sm">
            <input
                type="file"
                accept=".xlsx,.xls"
                @change="loadFile"
                class="block w-full text-sm text-slate-600 file:mr-4 file:py-2 file:px-4 file:rounded-lg file:border-0 file:text-sm file:font-medium file:bg-slate-100 file:text-slate-700 hover:file:bg-slate-200"
            />

            <p v-if="selectedFileName" class="text-sm text-slate-700">
                Archivo seleccionado: <span class="font-medium">{{ selectedFileName }}</span>
            </p>

            <div class="flex gap-3">
                <button
                    @click="uploadFile"
                    :disabled="loading || !selectedFile"
                    class="px-4 py-2 rounded-lg bg-slate-800 text-white text-sm font-medium hover:bg-slate-700 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                    {{ loading ? 'Subiendo...' : 'Subir Excel' }}
                </button>
                <button
                    @click="clearSelection"
                    :disabled="loading"
                    class="px-4 py-2 rounded-lg border border-slate-300 text-slate-700 text-sm font-medium hover:bg-slate-50 disabled:opacity-50"
                >
                    Limpiar
                </button>
            </div>
        </div>

        <p v-if="errorMessage" class="text-sm text-red-600 bg-red-50 border border-red-200 rounded-lg p-3">
            {{ errorMessage }}
        </p>
        <p v-if="successMessage" class="text-sm text-emerald-700 bg-emerald-50 border border-emerald-200 rounded-lg p-3">
            {{ successMessage }}
        </p>
    </section>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import { importStudentsExcel } from '@/services/importStudents'

const selectedFile = ref<File | null>(null)
const selectedFileName = ref('')
const loading = ref(false)
const errorMessage = ref('')
const successMessage = ref('')

function clearMessages(): void {
    errorMessage.value = ''
    successMessage.value = ''
}

function clearSelection(): void {
    selectedFile.value = null
    selectedFileName.value = ''
    clearMessages()
}

function loadFile(event: Event): void {
    clearMessages()
    const input = event.target as HTMLInputElement
    const file = input.files?.[0]

    if (!file) {
        clearSelection()
        return
    }

    const allowedExtensions = ['xlsx', 'xls']
    const extension = file.name.split('.').pop()?.toLowerCase() ?? ''

    if (!allowedExtensions.includes(extension)) {
        clearSelection()
        errorMessage.value = 'Solo se permiten archivos Excel con extensión .xlsx o .xls.'
        return
    }

    selectedFile.value = file
    selectedFileName.value = file.name
}

async function uploadFile(): Promise<void> {
    if (!selectedFile.value || loading.value) {
        return
    }

    loading.value = true
    clearMessages()

    const result = await importStudentsExcel(selectedFile.value)

    if (!result.success) {
        errorMessage.value = result.error.message
        loading.value = false
        return
    }

    successMessage.value = 'El archivo se importo correctamente.'
    loading.value = false
    selectedFile.value = null
    selectedFileName.value = ''
}

</script>