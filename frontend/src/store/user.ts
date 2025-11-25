import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User } from '@/types/user'
import { login as loginApi, getUserInfo } from '@/api/auth'

/**
 * 用户状态管理
 * 
 * 功能：
 * - 用户登录/登出
 * - 存储用户信息
 * - 存储 JWT Token
 * - 检查登录状态
 */
export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref<string | null>(localStorage.getItem('token'))
  const userInfo = ref<User | null>(null)

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)

  // 登录
  const login = async (username: string, password: string) => {
    const response = await loginApi({ username, password })
    if (response.code === 200 && response.data) {
      token.value = response.data.token
      localStorage.setItem('token', token.value)
      userInfo.value = response.data.user
      return true
    }
    throw new Error(response.message || '登录失败')
  }

  // 加载用户信息
  const loadUserInfo = async () => {
    try {
      const response = await getUserInfo()
      if (response.code === 200 && response.data) {
        userInfo.value = response.data
      }
    } catch (error) {
      console.error('加载用户信息失败:', error)
      logout()
    }
  }

  // 登出
  const logout = () => {
    token.value = null
    userInfo.value = null
    localStorage.removeItem('token')
  }

  // 初始化时加载用户信息
  if (token.value) {
    loadUserInfo()
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    login,
    logout,
    loadUserInfo
  }
})

