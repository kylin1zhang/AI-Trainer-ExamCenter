import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'

/**
 * Axios 封装
 * 
 * 功能：
 * - 统一请求拦截（添加 Token）
 * - 统一响应拦截（错误处理）
 * - 统一接口返回格式
 */

// 创建 Axios 实例
const service: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    
    // 添加 Token
    if (userStore.token) {
      config.headers.Authorization = `Bearer ${userStore.token}`
    }
    
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    // 如果响应数据已经是 Result 格式，直接返回
    if (response.data && typeof response.data === 'object' && 'code' in response.data) {
      const { code, message, data } = response.data

      // 成功
      if (code === 200) {
        return response.data
      }

      // Token 失效
      if (code === 401) {
        ElMessage.error('登录已过期，请重新登录')
        const userStore = useUserStore()
        userStore.logout()
        window.location.href = '/login'
        return Promise.reject(new Error(message))
      }

      // 其他错误
      ElMessage.error(message || '请求失败')
      return Promise.reject(new Error(message))
    }
    
    // 直接返回响应数据（包装成Result格式）
    return {
      code: 200,
      message: 'success',
      data: response.data
    }
  },
  (error) => {
    console.error('响应错误:', error)
    
    if (error.response?.status === 401) {
      ElMessage.error('登录已过期，请重新登录')
      const userStore = useUserStore()
      userStore.logout()
      window.location.href = '/login'
    } else {
      ElMessage.error(error.message || '网络错误')
    }
    
    return Promise.reject(error)
  }
)

// 封装请求方法
export default {
  get<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return service.get(url, config)
  },
  post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return service.post(url, data, config)
  },
  put<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return service.put(url, data, config)
  },
  delete<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return service.delete(url, config)
  }
}

