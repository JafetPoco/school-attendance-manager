<template>
  <div class="min-h-screen bg-slate-50 flex items-center justify-center p-4">
    <!-- Contenedor principal con animación -->
    <div class="max-w-4xl w-full animate-fade-in">
      <!-- Tarjeta de error -->
      <div class="bg-white rounded-2xl border border-slate-200 shadow-xl overflow-hidden">
        <div class="grid grid-cols-1 lg:grid-cols-2 min-h-125">
          <!-- Lado izquierdo - Información del error -->
          <div class="p-8 lg:p-12 flex flex-col justify-between">
            <div>
              <!-- Logo pequeño -->
              <div class="flex items-center space-x-2 mb-8">
                <div class="w-8 h-8 bg-slate-800 rounded-lg flex items-center justify-center">
                  <GraduationCap class="w-5 h-5 text-white" />
                </div>
                <span class="text-sm font-medium text-slate-600">Colegio Gral. José de San Martín</span>
              </div>

              <!-- Código de error -->
              <div class="mb-6">
                <span class="text-sm font-semibold text-slate-400 uppercase tracking-wider">Error 404</span>
              </div>

              <!-- Mensaje principal -->
              <h1 class="text-3xl lg:text-4xl font-bold text-slate-800 mb-4">
                Página no encontrada
              </h1>
              
              <p class="text-slate-500 mb-8 leading-relaxed">
                La página que estás buscando podría haber sido eliminada, 
                cambió su nombre o no está disponible temporalmente.
              </p>

              <!-- Acciones sugeridas -->
              <div class="space-y-4 mb-8">
                <div class="flex items-start space-x-3">
                  <div class="w-6 h-6 rounded-full bg-slate-100 flex items-center justify-center shrink-0 mt-0.5">
                    <Search class="w-4 h-4 text-slate-600" />
                  </div>
                  <div>
                    <h3 class="text-sm font-medium text-slate-800">Verifica la URL</h3>
                    <p class="text-xs text-slate-500">Asegúrate que la dirección sea correcta</p>
                  </div>
                </div>
                
                <div class="flex items-start space-x-3">
                  <div class="w-6 h-6 rounded-full bg-slate-100 flex items-center justify-center shrink-0 mt-0.5">
                    <Home class="w-4 h-4 text-slate-600" />
                  </div>
                  <div>
                    <h3 class="text-sm font-medium text-slate-800">Regresa al inicio</h3>
                    <p class="text-xs text-slate-500">Navega a la página principal del dashboard</p>
                  </div>
                </div>
                
                <div class="flex items-start space-x-3">
                  <div class="w-6 h-6 rounded-full bg-slate-100 flex items-center justify-center shrink-0 mt-0.5">
                    <LifeBuoy class="w-4 h-4 text-slate-600" />
                  </div>
                  <div>
                    <h3 class="text-sm font-medium text-slate-800">Contacta soporte</h3>
                    <p class="text-xs text-slate-500">Si el problema persiste, avísanos</p>
                  </div>
                </div>
              </div>

              <!-- Botones de acción -->
              <div class="flex flex-col sm:flex-row gap-3">
                <button @click="goToDashboard" 
                        class="flex-1 bg-slate-800 text-white px-6 py-3 rounded-xl hover:bg-slate-700 transition-all duration-300 transform hover:scale-105 hover:shadow-lg flex items-center justify-center space-x-2 group">
                  <ArrowLeft class="w-4 h-4 group-hover:-translate-x-1 transition-transform" />
                  <span>Volver al Dashboard</span>
                </button>
              </div>
            </div>
          </div>

          <!-- Lado derecho - Ilustración/Elemento visual -->
          <div class="relative bg-linear-to-br from-slate-800 to-slate-900 p-8 lg:p-12 flex items-center justify-center overflow-hidden">
            <!-- Patrón de fondo -->
            <div class="absolute inset-0 opacity-10">
              <div class="absolute top-0 left-0 w-40 h-40 bg-white rounded-full mix-blend-overlay filter blur-3xl"></div>
              <div class="absolute bottom-0 right-0 w-60 h-60 bg-white rounded-full mix-blend-overlay filter blur-3xl"></div>
            </div>

            <!-- Contenido visual animado -->
            <div class="relative text-center">
              <!-- Número 404 con animación -->
              <div class="flex items-center justify-center space-x-4 mb-8">
                <span class="text-8xl lg:text-9xl font-bold text-white opacity-50 animate-float">4</span>
                <div class="relative">
                  <span class="text-8xl lg:text-9xl font-bold text-white opacity-50 animate-float-delayed">0</span>
                  <div class="absolute -top-2 -right-2 w-4 h-4 bg-amber-400 rounded-full animate-ping"></div>
                  <div class="absolute -top-2 -right-2 w-4 h-4 bg-amber-400 rounded-full"></div>
                </div>
                <span class="text-8xl lg:text-9xl font-bold text-white opacity-50 animate-float">4</span>
              </div>

              <!-- Ilustración de búsqueda -->
              <div class="relative">
                <div class="w-32 h-32 mx-auto bg-white/10 rounded-3xl backdrop-blur-sm border border-white/20 flex items-center justify-center transform rotate-3 hover:rotate-0 transition-all duration-500">
                  <Search class="w-16 h-16 text-white/40" />
                </div>
                
                <!-- Elementos decorativos flotantes -->
                <div class="absolute -top-4 -right-4 w-16 h-16 bg-white/5 rounded-2xl backdrop-blur-sm border border-white/20 flex items-center justify-center animate-float-slow">
                  <BookOpen class="w-8 h-8 text-white/30" />
                </div>
                
                <div class="absolute -bottom-4 -left-4 w-20 h-20 bg-white/5 rounded-2xl backdrop-blur-sm border border-white/20 flex items-center justify-center animate-float-slow-delayed">
                  <Users class="w-10 h-10 text-white/30" />
                </div>
              </div>

              <!-- Texto descriptivo -->
              <div class="mt-8 space-y-2">
                <p class="text-white/80 text-sm">
                  Parece que te has perdido en el <br />camino educativo
                </p>
                <div class="flex items-center justify-center space-x-2 text-white/40 text-xs">
                  <Clock class="w-3 h-3" />
                  <span>No te preocupes, todos nos perdemos a veces</span>
                </div>
              </div>
            </div>

            <!-- Efecto de gradiente animado -->
            <div class="absolute bottom-0 left-0 right-0 h-32 bg-linear-to-t from-slate-900 to-transparent"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  GraduationCap,
  Search,
  Home,
  LifeBuoy,
  ArrowLeft,
  BookOpen,
  Users,
  Clock
} from 'lucide-vue-next'

const router = useRouter()
const showReportModal = ref(false)
const currentUrl = ref('')

const reportData = ref({
  url: '',
  description: ''
})

onMounted(() => {
  currentUrl.value = globalThis.location.href
  reportData.value.url = globalThis.location.href
})

const goToDashboard = () => {
  router.push('/dashboard')
}

const reportProblem = () => {
  showReportModal.value = true
}

const submitReport = () => {
  // Aquí iría la lógica para enviar el reporte
  console.log('Reporte enviado:', reportData.value)
  showReportModal.value = false
  // Mostrar notificación de éxito
  alert('Reporte enviado con éxito. Te contactaremos pronto.')
}
</script>

<style scoped>
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
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

@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-10px);
  }
}

@keyframes floatDelayed {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-15px);
  }
}

@keyframes floatSlow {
  0%, 100% {
    transform: translateY(0px) rotate(3deg);
  }
  50% {
    transform: translateY(-8px) rotate(5deg);
  }
}

.animate-fade-in {
  animation: fadeIn 0.6s ease-out;
}

.animate-slide-up {
  animation: slideUp 0.4s ease-out;
}

.animate-float {
  animation: float 4s ease-in-out infinite;
}

.animate-float-delayed {
  animation: floatDelayed 5s ease-in-out infinite;
}

.animate-float-slow {
  animation: floatSlow 6s ease-in-out infinite;
}

.animate-float-slow-delayed {
  animation: floatSlow 7s ease-in-out infinite;
  animation-delay: 1s;
}

/* Hover effects en los enlaces */
a {
  position: relative;
  overflow: hidden;
}

a::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 0;
  height: 1px;
  background-color: currentColor;
  transition: width 0.3s ease;
}

a:hover::after {
  width: 100%;
}

/* Estilo para el botón de reporte */
button {
  cursor: pointer;
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