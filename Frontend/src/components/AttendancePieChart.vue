<template>
  <div class="attendance-chart">
    <VChart
      :option="option"
      autoresize
      style="height:320px"
    />
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue"
import { use } from "echarts/core"
import { PieChart } from "echarts/charts"
import {
  TooltipComponent,
  LegendComponent
} from "echarts/components"
import { CanvasRenderer } from "echarts/renderers"
import VChart from "vue-echarts"

// Registrar solo lo necesario (tree shaking)
use([
  PieChart,
  TooltipComponent,
  LegendComponent,
  CanvasRenderer
])

interface Props {
  present: number
  late: number
  absent: number
  justified: number
}

const props = defineProps<Props>()

const option = computed(() => ({
  tooltip: {
    trigger: "item"
  },

  legend: {
    bottom: 0
  },

  series: [
    {
      name: "Asistencia",
      type: "pie",
      radius: ["45%", "70%"], // donut
      avoidLabelOverlap: false,

      label: {
        show: true,
        formatter: "{b}\n{d}%"
      },

      data: [
        {
          value: props.present,
          name: "Presente",
          itemStyle: { color: "#22c55e" }
        },
        {
          value: props.late,
          name: "Tarde",
          itemStyle: { color: "#facc15" }
        },
        {
          value: props.absent,
          name: "Falta",
          itemStyle: { color: "#ef4444" }
        },
        {
          value: props.justified,
          name: "Justificado",
          itemStyle: { color: "#3b82f6" }
        }
      ]
    }
  ]
}))
</script>

<style scoped>
.attendance-chart {
  width: 100%;
}
</style>