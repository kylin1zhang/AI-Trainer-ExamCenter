import request from './request'
import type { LoginParams, RegisterParams, LoginResponse, User } from '@/types/user'
import type { ApiResponse } from '@/types/api'

/**
 * 认证相关 API
 */

// 登录
export const login = (data: LoginParams) => {
  return request.post<ApiResponse<LoginResponse>>('/auth/login', data)
}

// 注册
export const register = (data: RegisterParams) => {
  return request.post<ApiResponse<User>>('/auth/register', data)
}

// 获取用户信息
export const getUserInfo = () => {
  return request.get<ApiResponse<User>>('/auth/user')
}

// 刷新 Token
export const refreshToken = () => {
  return request.post<ApiResponse<any>>('/auth/refresh')
}

// 登出
export const logout = () => {
  return request.post<ApiResponse<any>>('/auth/logout')
}

