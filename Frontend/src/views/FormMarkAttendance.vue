<template>
    <h1>Registrar Asistecia</h1>
    <form @submit.prevent="onSubmit">
        <label for="dni">DNI:</label><br>
        <input type="text" id="dni" name="dni" class="border-2" v-model="request.dni"><br><br>
        <select name="attendanceType" id="attendanceType" class="border-2" v-model="request.attendanceType">
            <option value="presente">Presente</option>
            <option value="tarde">Tarde</option>
        </select>


        <button type="submit" class="border-3">Registrar</button>
    </form>

    <div v-if="error" class="border-2 border-red-500">Error {{ error }}</div>
    <div v-if="response" class="border-2 border-red-500">Exito!
        <h2>Asistencia del Alumno: {{ response.studentName }} guardade Exitosamente</h2>
        <div>Fecha: {{ response.date }}</div>
    </div>

    {{ request }}

</template>
<script setup lang="ts">
import { addAttendance } from '@/services/addAttendanceService';
import type { AttendanceResponse, AttendanceRequest } from '@/types/Attendance';
import { ref } from 'vue'

const loading = ref(false)
const error = ref<string>('')
const response = ref<AttendanceResponse | null>(null)
const request = ref<AttendanceRequest>({
    dni: '',
    attendanceType: 'presente'
})

const resetForm = () => {
  request.value = {
    dni: '',
    attendanceType: 'presente'
  }
  response.value = null
  error.value = ''
}

const onSubmit = async () => {
    loading.value = true
    error.value = ''

    try {
        const result = await addAttendance(request.value)

        if (result.success) {
            response.value = result.data
            setTimeout(() => {
                resetForm()
            }, 5000)
        } else {
            response.value = null
            error.value = result.error.message
        }
    } catch (err: unknown) {
        error.value = err instanceof Error ? err.message : 'Error de conexión'
    } finally {
        loading.value = false
    }
}


</script>