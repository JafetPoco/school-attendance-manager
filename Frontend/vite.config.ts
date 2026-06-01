import { defineConfig } from 'vite'
import tailwindcss from '@tailwindcss/vite'
import vue from '@vitejs/plugin-vue'
import VueDevTools from 'vite-plugin-vue-devtools'
import path from 'node:path'

// https://vite.dev/config/
export default defineConfig(({ command }) => {
  const plugins = [
    vue(),
    tailwindcss(),
    command === 'serve' ? VueDevTools() : null
  ].filter(Boolean) // Esto elimina los valores null/undefined

  return {
    plugins: plugins as any, // O usa 'as Plugin[]' si quieres tipado correcto
    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src')
      }
    }
  }
})