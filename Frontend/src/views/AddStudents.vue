<template>
  <Header />
  <div class="min-h-screen bg-slate-50 py-8 px-4 sm:px-6 lg:px-8">
    <main class="max-w-7xl mx-auto">
      <!-- Header de la página -->
      <div class="mb-8 animate-fade-in-down">
        <div class="flex items-center space-x-3 mb-2">
          <div class="w-12 h-12 bg-slate-800 rounded-xl flex items-center justify-center shadow-lg">
            <UserPlus class="w-6 h-6 text-white" />
          </div>
          <div>
            <h1 class="text-2xl font-bold text-slate-800">Agregar Estudiante</h1>
            <p class="text-sm text-slate-500">Registra un nuevo estudiante en el sistema</p>
          </div>
        </div>

        <!-- Breadcrumb -->
        <div class="flex items-center space-x-2 text-xs text-slate-400 mt-2">
          <span>Estudiantes</span>
          <ChevronRight class="w-3 h-3" />
          <span class="text-slate-600">Agregar Estudiante</span>
        </div>
      </div>

      <!-- Selector de método -->
      <div class="mb-8 animate-fade-in-up">
        <div class="bg-white rounded-xl border border-slate-200 shadow-sm p-1 inline-flex">
          <button @click="showForm = true"
                  class="relative px-6 py-2.5 rounded-lg text-sm font-medium transition-all duration-300 flex items-center space-x-2"
                  :class="[showForm ? 'bg-slate-800 text-white shadow-md' : 'text-slate-600 hover:text-slate-800 hover:bg-slate-50']">
            <FileText class="w-4 h-4" />
            <span>Formulario Manual</span>
          </button>
          
          <button @click="showForm = false"
                  class="relative px-6 py-2.5 rounded-lg text-sm font-medium transition-all duration-300 flex items-center space-x-2"
                  :class="[!showForm ? 'bg-slate-800 text-white shadow-md' : 'text-slate-600 hover:text-slate-800 hover:bg-slate-50']">
            <Upload class="w-4 h-4" />
            <span>Importar desde Archivo</span>
          </button>
        </div>
        
        <!-- Descripción del método seleccionado -->
        <p class="text-xs text-slate-500 mt-3 flex items-center space-x-1">
          <Info class="w-3 h-3" />
          <span>{{ showForm ? 'Completa el formulario con los datos del estudiante' : 'Sube un archivo Excel con la lista de estudiantes' }}</span>
        </p>
      </div>

      <!-- Contenedor del componente seleccionado -->
      <transition name="fade" mode="out-in">
        <div :key="showForm ? 'form' : 'import'" class="animate-fade-in-up">
          <FormStudent v-if="showForm" />
          <ImportListStudent v-else />
        </div>
      </transition>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Header from '@/components/Header.vue'
import FormStudent from '@/components/FormStudents.vue'
import ImportListStudent from '@/components/ImportListStudent.vue'
import {
  UserPlus,
  ChevronRight,
  FileText,
  Upload,
  Info
} from 'lucide-vue-next'

const showForm = ref(true)
</script>

<style scoped>
/* Animaciones */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
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

.animate-fade-in-down {
  animation: fadeInDown 0.6s ease-out;
}

.animate-fade-in-up {
  animation: fadeInUp 0.5s ease-out;
}

/* Transiciones suaves */
* {
  transition-property: background-color, border-color, color, fill, stroke, opacity, box-shadow, transform;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 200ms;
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