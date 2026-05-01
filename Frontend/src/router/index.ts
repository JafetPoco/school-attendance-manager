import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'

import page404 from '@/views/error/Error404.vue'
import { getJustificationFormInfo } from '@/services/justificationsService'
import { useSectionStore } from '@/stores/sectionStore'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: () => import('@/views/Login.vue')
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: () => import('@/views/Dashboard.vue'),
      meta: { requiresAuth: true}
    },
    {
      path: '/addStudent',
      name: 'addStudent',
      component: () => import('@/views/AddStudents.vue'),
      meta: { requiresAuth: true}
    },
    {
      path: '/students',
      name: 'students',
      component: () => import('@/views/Students.vue'),
      meta: { requiresAuth: true}
    },
    {
      path: '/markAttendance',
      name: 'markAttendance',
      component: () => import('@/views/FormMarkAttendance.vue'),
      meta: { requiresAuth: true}
    },
    {
      path: '/attendances',
      name: 'attendances',
      component: () => import('@/views/Attendances.vue'),
      meta: { requiresAuth: true}
    },
    {
      path: '/importStudents',
      name: 'importStudents',
      component: () => import('@/views/ImportStudents.vue'),
      meta: { requiresAuth: true}
    },
    {
      path: '/students/:id',
      name: 'studentDetails',
      component: () => import('@/views/StudentDetails.vue'),
      meta: { requiresAuth: true}
    },
    {
      path: '/settings',
      name: 'settings',
      component: () => import('@/views/Settings.vue'),
      meta: { requiresAuth: true}
    },
    {
      path: '/justifications/:token',
      name: 'justifications',
      component: () => import('@/views/Justifications.vue'),
      beforeEnter: async (to, _from, next) => {
        const result = await getJustificationFormInfo(to.params.token as string)

        if (result.success) {
          return next()
        }

        return next({
          name: 'justificationsError',
          query: {
            message: result.error.message
          }
        })
      }
    },
    {
      path: '/pendingJustifications',
      name: 'pendingJustifications',
      component: () => import('@/views/JustificationsView.vue'),
      meta: { requiresAuth: true}
    },
    {
      path: '/justifications/error',
      name: 'justificationsError',
      component: () => import('@/views/error/JustificationNotFound.vue'),
    },
    {
      path: '/classes',
      name: 'classes',
      component: () => import('@/views/ClassManager.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/:pathMatch(.*)*',
      component: page404,
    }
  ],
})

router.beforeEach(async (to, _from, next) => {
  const auth = useAuthStore()
  const section = useSectionStore()

  // If route requires auth, ensure store is populated before deciding
  if (to.meta.requiresAuth) {
    if (!auth.isAuthenticated) {
      try {
        await auth.fetchUser()
        await section.fetchSections()
      } catch (e) {
        // ignore - fetchUser handles errors and keeps user=null
      }
    }

    if (!auth.isAuthenticated) {
      return next({ name: 'login' })
    }
  }

  return next()
})

export default router