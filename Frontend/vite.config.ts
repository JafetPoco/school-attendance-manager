import { defineConfig } from 'vite'
import tailwindcss from '@tailwindcss/vite'
import vue from '@vitejs/plugin-vue'
import VueDevTools from 'vite-plugin-vue-devtools'
import path from 'node:path'

// https://vite.dev/config/
export default defineConfig(({ command }) => {
  const devToolsPlugin = command === 'serve' ? VueDevTools() : undefined

  return {
    plugins: [
    vue(),
    tailwindcss(),
    ...(devToolsPlugin ? [devToolsPlugin] : [])
  ],
    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src')
      }
    }
  }
})