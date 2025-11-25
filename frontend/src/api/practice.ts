import request from './request'
import type { 
  SubmitAnswerParams, 
  SubmitAnswerResponse,
  Question,
  UserNote 
} from '@/types/practice'
import type { ApiResponse } from '@/types/api'

/**
 * 用户练习记录 API
 */

// 提交答案
export const submitAnswer = (data: SubmitAnswerParams) => {
  return request.post<ApiResponse<SubmitAnswerResponse>>('/user-practice/submit', data)
}

// 获取我的错题
export const getWrongQuestions = (bankId: number) => {
  return request.get<ApiResponse<Question[]>>('/user-practice/wrong-questions', { 
    params: { bankId } 
  })
}

// 收藏题目
export const favoriteQuestion = (questionId: number) => {
  return request.post<ApiResponse<any>>(`/user-practice/favorites/${questionId}`)
}

// 取消收藏
export const unfavoriteQuestion = (questionId: number) => {
  return request.delete<ApiResponse<any>>(`/user-practice/favorites/${questionId}`)
}

// 获取我的收藏
export const getFavoriteQuestions = (bankId: number) => {
  return request.get<ApiResponse<Question[]>>('/user-practice/favorites', { 
    params: { bankId } 
  })
}

// 添加笔记
export const addNote = (data: { questionId: number; content: string }) => {
  return request.post<ApiResponse<any>>('/user-practice/notes', data)
}

// 获取我的笔记
export const getNotes = (bankId: number) => {
  return request.get<ApiResponse<UserNote[]>>('/user-practice/notes', { 
    params: { bankId } 
  })
}

// 获取易错题
export const getDifficultQuestions = (bankId: number) => {
  return request.get<ApiResponse<Question[]>>('/user-practice/difficult-questions', { 
    params: { bankId } 
  })
}

