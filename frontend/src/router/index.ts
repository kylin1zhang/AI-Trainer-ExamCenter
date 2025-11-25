import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/store/user'

/**
 * 路由配置
 * 
 * 页面结构：
 * - /login - 登录页
 * - /register - 注册页
 * - / - 主页（题库列表）
 * - /banks/:id - 题库详情页
 *   - /banks/:id/my-practice - 我的练习（错题/收藏/笔记）
 *   - /banks/:id/practice - 专项练习
 * - /practice - 做题页面
 * - /exam/:examId - 考试页面
 */

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/LoginView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/auth/RegisterView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/layouts/MainLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/home/HomeView.vue')
      },
      {
        path: 'banks/:id',
        name: 'BankDetail',
        component: () => import('@/views/bank/BankDetailView.vue')
      },
      {
        path: 'banks/:id/my-practice',
        name: 'MyPractice',
        component: () => import('@/views/practice/MyPracticeView.vue')
      },
      {
        path: 'banks/:id/practice',
        name: 'SpecialPractice',
        component: () => import('@/views/practice/SpecialPracticeView.vue')
      },
      {
        path: 'practice',
        name: 'Practice',
        component: () => import('@/views/practice/PracticeView.vue')
      },
      {
        path: 'exam/:examId',
        name: 'Exam',
        component: () => import('@/views/exam/ExamView.vue')
      },
      {
        path: 'notes',
        name: 'Notes',
        component: () => import('@/views/practice/NotesView.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫：检查登录状态
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const requiresAuth = to.meta.requiresAuth !== false

  if (requiresAuth && !userStore.isLoggedIn) {
    next('/login')
  } else if (to.path === '/login' && userStore.isLoggedIn) {
    next('/')
  } else {
    next()
  }
})

export default router


