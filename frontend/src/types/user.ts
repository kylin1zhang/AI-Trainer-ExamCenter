/**
 * 用户相关类型定义
 */

export interface User {
  id: number
  username: string
  nickname: string
  email?: string
  phone?: string
  avatar?: string
  createTime: string
}

export interface LoginParams {
  username: string
  password: string
}

export interface RegisterParams {
  username: string
  password: string
  confirmPassword: string
  email?: string
  phone?: string
}

export interface LoginResponse {
  token: string
  user: User
}


