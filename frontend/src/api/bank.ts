import request from './request'
import type { QuestionBank, QuestionBankStatistics } from '@/types/bank'
import type { ApiResponse } from '@/types/api'

/**
 * 题库相关 API
 */

// 获取题库列表
export const getQuestionBanks = () => {
  return request.get<ApiResponse<QuestionBank[]>>('/question-banks')
}

// 获取题库详情
export const getQuestionBankDetail = (id: number) => {
  return request.get<ApiResponse<QuestionBank>>(`/question-banks/${id}`)
}

// 获取题库统计信息
export const getQuestionBankStatistics = (id: number) => {
  return request.get<ApiResponse<QuestionBankStatistics>>(`/question-banks/${id}/statistics`)
}

