<template>
    <h1>Clases</h1>

    <form @submit.prevent="submitClass">
        <label for="level">Nivel</label>
        <select name="level" id="level" v-model="newClass!.level" required>
            <option value="">Selecciona una</option>
            <option value="PRIMARIA">Primaria</option>
            <option value="SECUNDARIA">Secundaria</option>
        </select>

        <label for="grade">Grado</label>
        <select name="grade" id="grade" v-model="newClass!.grade" required>
            <option value="">Selecciona una</option>
            <option value="PRIMERO">1ro</option>
            <option value="SEGUNDO">2do</option>
            <option value="TERCERO">3ro</option>
            <option value="CUARTO">4to</option>
            <option value="QUINTO">5to</option>
            <option value="SEXTO">6to</option>
        </select>

        <label for="section">Sección</label>
        <input type="text" v-model="newClass!.section" required>

        <button type="submit" :disabled="loading">{{ loading ? 'Guardando...' : 'Guardar Clase' }}</button>
    </form>

    <div v-if="errorMessage" class="error">{{ errorMessage}}</div>

    {{ newClass }}

</template>
<script setup lang="ts">
import { createClass } from '@/services/classService';
import type { ClassRequest } from '@/types/Class';
import { ref } from 'vue'

const loading = ref(false)
const errorMessage = ref<string>('')
const newClass = ref<ClassRequest>({ level: '', grade: '', section: '' })

const submitClass = async () => {
    loading.value = true
    errorMessage.value = ''

    if (!newClass.value.level || !newClass.value.grade || !newClass.value.section) {
        errorMessage.value = 'Por favor completa todos los campos'
        return
    }
    
    try {
        const response = await createClass(newClass.value)
        if (response.success) {
            alert('Clase creada exitosamente')
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