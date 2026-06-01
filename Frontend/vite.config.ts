import { defineConfig } from 'vite'
import tailwindcss from '@tailwindcss/vite'
import vue from '@vitejs/plugin-vue'
import VueDevTools from 'vite-plugin-vue-devtools'
import path from 'node:path'

// https://vite.dev/config/
export default defineConfig(({ command }) => {
  const plugins = [
    vue(),
    tailwindcss()
  ]

  // Solo agregar VueDevTools en modo desarrollo
  if (command === 'serve') {
    plugins.push(VueDevTools())
  }

  return {
    plugins,
    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src')
      }
    }
  }
})