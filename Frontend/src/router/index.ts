import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/authStore'

import page404 from '../views/error/Error404.vue'


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
      path: '/addParentWithChildren',
      name: 'addParentWithChildren',
      component: () => import('@/views/FormParentWithChildren.vue'),
      meta: { requiresAuth: true}
    },
    {
      path: '/:pathMatch(.*)*',
      component: page404,
    }
  ],
})

router.beforeEach(async (to, from, next) => {
  const auth = useAuthStore()

  // If route requires auth, ensure store is populated before deciding
  if (to.meta.requiresAuth) {
    if (!auth.isAuthenticated) {
      try {
        await auth.fetchUser()
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