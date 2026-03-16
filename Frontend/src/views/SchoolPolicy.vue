<template>
    <h1>School Policy</h1>

    <div v-if="errorMessage" class="border-red-500 border 2 bg-red-100">
        ERROR {{ errorMessage }}
    </div>
    <div v-if="success"  class="border-green-500 border 2 bg-green-100">
        Exito: Valores agregados
        - Dias maximos para justificar falta: {{ schoolPolicyResponse?.justificationExpirationDays }}
        - Hora a partir de la cual se considera tarde: {{ schoolPolicyResponse?.lateAttendaceTime }}
    </div>
    <div v-if="loading">Loading...</div>

    <label for="daysJustify">Dias maximos para justificar falta</label>
    <input type="number" id="daysJustify" v-model="schoolPolicy.justificationExpirationDays" class="border-2">
    <br>
    <label for="lateTime">Hora a partir de la cual se considera tarde</label>
    <input type="time" id="lateTime" v-model="schoolPolicy.lateAttendaceTime">
    <br>

    <button class="border-3 bg-amber-300" @click="addPolicy">
      Guardar Cambios
    </button>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import { addSchoolPolicies } from '@/services/schoolPolicyService'
import type { SchoolPolicyRequest, SchoolPolicyResponse } from '@/types/SchoolPolicy'

const loading = ref(false)
const errorMessage = ref('')
const success = ref(false)
const schoolPolicy = ref<SchoolPolicyRequest>({
    justificationExpirationDays: 0,
    lateAttendaceTime: '00:00'
})

const schoolPolicyResponse = ref<SchoolPolicyResponse | null>(null)

const addPolicy = async () => {
    loading.value = true
    errorMessage.value = ''
  try {
    const response = await addSchoolPolicies(schoolPolicy.value)

    if (response.success) {
      schoolPolicyResponse.value = response.data
      success.value = true
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