/**
 * 题库相关类型定义
 */

export interface QuestionBank {
  id: number
  name: string
  description: string
  coverImage?: string
  questionCount: number
  sortOrder: number
  status: number
  createTime: string
}

export interface QuestionBankStatistics {
  bankId: number
  totalQuestions: number
  practiceCount: number
  correctCount: number
  wrongCount: number
  accuracy: number
  favoriteCount: number
  noteCount: number
}


