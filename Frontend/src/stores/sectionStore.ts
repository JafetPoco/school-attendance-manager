import { defineStore } from 'pinia'
import type { ClassResponse } from '@/types/Class'
import { getClasses } from '@/services/classService'

interface SectionState {
  sections: ClassResponse[]
  loading: boolean
}

export const useSectionStore = defineStore('sections', {
  state: (): SectionState => ({
    sections: [],
    loading: false
  }),

  getters: {
    areSections: (state): boolean => state.sections.length > 0
  },

  actions: {
    async fetchSections(): Promise<void> {
      this.loading = true
      try {
        const result = await getClasses()
        if (result.success) {
            this.sections = result.data
        } else {
            this.sections = []
        }
      } catch (error) {
        console.error('fetchSections error:', error)
        this.sections = []
      } finally {
        this.loading = false
      }
    }
  }
})
