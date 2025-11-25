/**
 * 练习相关类型定义
 */

import type { Question } from './question'

/**
 * 练习模式
 * ANSWER - 答题模式：用户选择选项后立即判断对错（绿色/红色），显示答案和解析
 * RECITE - 背题模式：进入页面时直接展示答案和解析
 */
export type PracticeMode = 'ANSWER' | 'RECITE'

/**
 * 提交答案参数
 */
export interface SubmitAnswerParams {
  questionId: number
  userAnswer: string
  timeCost: number
  practiceMode: PracticeMode
  sourceType?: 'PRACTICE' | 'EXAM'
  sourceId?: number
}

/**
 * 提交答案响应
 */
export interface SubmitAnswerResponse {
  isCorrect: boolean
  correctAnswer: string
  explanation: string
}

/**
 * 用户笔记
 */
export interface UserNote {
  id: number
  questionId: number
  bankId: number
  content: string
  createTime: string
  updateTime: string
  question?: Question
}

/**
 * 我的错题（按题目ID去重）
 */
export interface WrongQuestion {
  questionId: number
  wrongCount: number
  lastWrongTime: string
  question?: Question
}

/**
 * 易错题（错误次数 ≥ 2 次）
 */
export interface DifficultQuestion {
  questionId: number
  wrongCount: number
  totalAttempts: number
  errorRate: number
  question?: Question
}

export { Question }

