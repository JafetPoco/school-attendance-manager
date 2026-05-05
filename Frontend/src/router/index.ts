import { createRouter, createWebHistory } from 'vue-router'
import type { RouteLocationNormalized, NavigationGuardNext } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import { useSectionStore } from '@/stores/sectionStore'
import page404 from '@/views/error/Error404.vue'
import { getJustificationFormInfo } from '@/services/justificationsService'

// Type Declarations
declare module 'vue-router' {
  interface RouteMeta {
    requiresAuth?: boolean
  }
}

// Initialize user session and sections on first protected route access
async function initializeUserSession(): Promise<void> {
  const auth = useAuthStore()
  const section = useSectionStore()

  if (auth.isAuthenticated) {
    return // Already initialized
  }

  try {
    await auth.fetchUser()

    if (auth.isAuthenticated && section.sections.length === 0) {
      await section.fetchSections()
    }
  } catch (error) {
    console.error('Failed to initialize user session:', error)
    // Keep user=null, will redirect to login
  }
}

// Authentication guard for protected routes
async function checkAuthGuard(
  to: RouteLocationNormalized,
  next: NavigationGuardNext
): Promise<void> {
  const auth = useAuthStore()

  if (!to.meta.requiresAuth) {
    return next()
  }

  // Initialize session if not authenticated
  if (!auth.isAuthenticated) {
    await initializeUserSession()
  }

  // Verify authentication after initialization attempt
  if (!auth.isAuthenticated) {
    return next({ name: 'login' })
  }

  return next()
}


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
      meta: { requiresAuth: true }
    },
    {
      path: '/addStudent',
      name: 'addStudent',
      component: () => import('@/views/AddStudents.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/students',
      name: 'students',
      component: () => import('@/views/Students.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/markAttendance',
      name: 'markAttendance',
      component: () => import('@/views/FormMarkAttendance.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/attendances',
      name: 'attendances',
      component: () => import('@/views/Attendances.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/importStudents',
      name: 'importStudents',
      component: () => import('@/views/ImportStudents.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/students/:id',
      name: 'studentDetails',
      component: () => import('@/views/StudentDetails.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/settings',
      name: 'settings',
      component: () => import('@/views/Settings.vue'),
      meta: { requiresAuth: true }
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
      meta: { requiresAuth: true }
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
      path: '/createUsers',
      name: 'createUsers',
      component: () => import('@/views/CreateUsers.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/:pathMatch(.*)*',
      component: page404,
    }
  ],
})

router.beforeEach((to, _from, next) => checkAuthGuard(to, next))

export default router