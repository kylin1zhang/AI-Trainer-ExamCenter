import request from './request'
import type { Question, QuestionListParams } from '@/types/question'
import type { ApiResponse } from '@/types/api'

/**
 * 题目相关 API
 */

// 顺序练习 - 获取题目列表
export const getSequenceQuestions = (params: QuestionListParams) => {
  return request.get<ApiResponse<Question[]>>('/questions/sequence', { params })
}

// 随机练习 - 获取随机题目
export const getRandomQuestions = (bankId: number, count: number) => {
  return request.get<ApiResponse<Question[]>>('/questions/random', { 
    params: { bankId, count } 
  })
}

// 题型练习 - 按题型获取题目
export const getQuestionsByType = (params: QuestionListParams & { type: string }) => {
  return request.get<ApiResponse<Question[]>>('/questions/by-type', { params })
}

// 获取题目详情
export const getQuestionDetail = (id: number) => {
  return request.get<ApiResponse<Question>>(`/questions/${id}`)
}

