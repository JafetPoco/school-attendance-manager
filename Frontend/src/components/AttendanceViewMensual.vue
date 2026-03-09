<template>
  <div class="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden">
    <!-- Header mensual -->
    <div class="bg-slate-50 px-6 py-4 border-b border-slate-200">
      <div class="flex items-center justify-between">
        <div class="flex items-center space-x-4">
          <div class="flex items-center space-x-2">
            <CalendarRange class="w-5 h-5 text-slate-600" />
            <span class="text-lg font-semibold text-slate-800">Marzo 2024</span>
          </div>
          <div class="flex items-center space-x-2">
            <button class="p-1.5 hover:bg-white rounded-lg transition-colors">
              <ChevronLeft class="w-4 h-4 text-slate-600" />
            </button>
            <button class="p-1.5 hover:bg-white rounded-lg transition-colors">
              <ChevronRight class="w-4 h-4 text-slate-600" />
            </button>
          </div>
        </div>
        
        <!-- Leyenda -->
        <div class="flex items-center space-x-4">
          <div class="flex items-center space-x-2">
            <div class="w-3 h-3 bg-emerald-500 rounded-full"></div>
            <span class="text-xs text-slate-600">Alta asistencia</span>
          </div>
          <div class="flex items-center space-x-2">
            <div class="w-3 h-3 bg-amber-500 rounded-full"></div>
            <span class="text-xs text-slate-600">Media asistencia</span>
          </div>
          <div class="flex items-center space-x-2">
            <div class="w-3 h-3 bg-red-500 rounded-full"></div>
            <span class="text-xs text-slate-600">Baja asistencia</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Calendario de asistencia -->
    <div class="p-6">
      <!-- Días de la semana -->
      <div class="grid grid-cols-7 gap-2 mb-2">
        <div v-for="day in weekDays" :key="day" 
             class="text-center text-xs font-semibold text-slate-500 py-2">
          {{ day }}
        </div>
      </div>

      <!-- Grid del calendario -->
      <div class="grid grid-cols-7 gap-2">
        <div v-for="day in 35" :key="day"
             class="aspect-square p-2 border border-slate-200 rounded-lg hover:border-slate-300 transition-all cursor-pointer group"
             :class="{ 'bg-slate-50': day > 31 }">
          <div class="h-full flex flex-col">
            <span class="text-sm font-medium text-slate-600 mb-1">{{ day > 31 ? day - 31 : day }}</span>
            <div v-if="day <= 31" class="flex-1 flex flex-col justify-end">
              <!-- Indicadores de asistencia -->
              <div class="space-y-1">
                <div class="flex items-center justify-between text-xs">
                  <span class="text-slate-500">Presentes</span>
                  <span class="font-medium text-emerald-600">28</span>
                </div>
                <div class="w-full h-1 bg-slate-100 rounded-full overflow-hidden">
                  <div class="h-full bg-emerald-500 rounded-full" style="width: 90%"></div>
                </div>
              </div>
              
              <!-- Badge de resumen que aparece en hover -->
              <div class="absolute inset-0 bg-slate-800/90 rounded-lg opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center">
                <div class="text-white text-xs space-y-1">
                  <p class="font-semibold">28 Presentes</p>
                  <p>3 Tardanzas</p>
                  <p>2 Ausentes</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Resumen mensual -->
    <div class="bg-slate-50 px-6 py-4 border-t border-slate-200">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div class="bg-white rounded-lg p-3 border border-slate-200">
          <p class="text-xs text-slate-500 mb-1">Promedio asistencia</p>
          <p class="text-xl font-bold text-slate-800">94%</p>
        </div>
        <div class="bg-white rounded-lg p-3 border border-slate-200">
          <p class="text-xs text-slate-500 mb-1">Total presentes</p>
          <p class="text-xl font-bold text-emerald-600">672</p>
        </div>
        <div class="bg-white rounded-lg p-3 border border-slate-200">
          <p class="text-xs text-slate-500 mb-1">Total tardanzas</p>
          <p class="text-xl font-bold text-amber-600">28</p>
        </div>
        <div class="bg-white rounded-lg p-3 border border-slate-200">
          <p class="text-xs text-slate-500 mb-1">Total ausentes</p>
          <p class="text-xl font-bold text-red-600">16</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { CalendarRange, ChevronLeft, ChevronRight } from 'lucide-vue-next'

const weekDays = ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom']
</script>