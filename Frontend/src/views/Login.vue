<script setup></script>
<template>
  <div class="min-h-screen flex items-center justify-center bg-linear-to-br from-slate-900 via-slate-800 to-slate-900 px-4">
    
    <!-- Card -->
    <div class="w-full max-w-md bg-white/5 backdrop-blur-xl border border-white/10 rounded-2xl shadow-2xl p-8">
      
      <!-- Logo / Title -->
      <div class="text-center mb-8">
        <h1 class="text-3xl font-bold text-white tracking-tight">
          Bienvenido
        </h1>
        <p class="text-slate-400 mt-2 text-sm">
          Accede a tu cuenta de Gmail
        </p>
      </div>

      <!-- Divider -->
      <div class="flex items-center gap-4 mb-6">
        <div class="flex-1 h-px bg-slate-700"></div>
        <span class="text-slate-500 text-xs uppercase tracking-wider">Acceso</span>
        <div class="flex-1 h-px bg-slate-700"></div>
      </div>

      <!-- Google Button -->
      <button
        @click="loginWithGoogle"
        class="group w-full relative overflow-hidden flex items-center justify-center gap-3 bg-white text-gray-800 font-medium py-3 rounded-xl transition-all duration-300 ease-out shadow-lg hover:shadow-2xl
         hover:-translate-y-0.5 active:scale-[0.98]"
      >
        <!-- Google Icon -->
        <svg class="w-5 h-5" viewBox="0 0 48 48">
          <path fill="#EA4335" d="M24 9.5c3.54 0 6.72 1.22 9.23 3.6l6.87-6.87C35.77 2.43 30.28 0 24 0 14.61 0 6.27 5.48 2.44 13.44l8.06 6.26C12.38 13.45 17.72 9.5 24 9.5z"/>
          <path fill="#4285F4" d="M46.5 24.5c0-1.63-.15-3.2-.42-4.7H24v9.04h12.7c-.55 2.96-2.2 5.47-4.68 7.15l7.25 5.63C43.9 37.54 46.5 31.5 46.5 24.5z"/>
          <path fill="#FBBC05" d="M10.5 28.7a14.5 14.5 0 010-9.4l-8.06-6.26A23.94 23.94 0 000 24c0 3.84.92 7.47 2.44 10.56l8.06-6.26z"/>
          <path fill="#34A853" d="M24 48c6.28 0 11.77-2.07 15.7-5.64l-7.25-5.63c-2.02 1.35-4.6 2.14-8.45 2.14-6.28 0-11.62-3.95-13.5-9.2l-8.06 6.26C6.27 42.52 14.61 48 24 48z"/>
        </svg>

        Continuar con Google
      </button>

      <!-- Footer -->
      <p class="text-center text-xs text-slate-500 mt-8">
        © 2026 Tu Empresa. Todos los derechos reservados.
      </p>
    </div>
  </div>
</template>
<script setup lang="ts">
const loginWithGoogle = () => {
  const baseUrl = import.meta.env.VITE_API_BASE_URL
  const oauth2Url = `${baseUrl}/oauth2/authorization/google`
  
  globalThis.location.href = oauth2Url
}
</script>
