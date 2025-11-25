/**
 * 考试相关类型定义
 * 
 * 人工智能训练师三级考试规则：
 * - 判断题：40题 × 0.5分 = 20分
 * - 单选题：140题 × 0.5分 = 70分
 * - 多选题：10题 × 1分 = 10分
 * - 总分：100分，60分及格
 * - 考试时长：90分钟
 */

import type { Question } from './question'

/**
 * 创建考试参数
 */
export interface CreateExamParams {
  bankId: number
  judgeCount?: number   // 判断题数量，默认 40
  singleCount?: number  // 单选题数量，默认 140
  multipleCount?: number // 多选题数量，默认 10
  duration?: number     // 考试时长（分钟），默认 90
}

/**
 * 考试实体
 */
export interface Exam {
  id: number
  userId: number
  bankId: number
  title: string
  questionIds: number[]
  duration: number
  judgeCount: number
  singleCount: number
  multipleCount: number
  score?: number
  totalScore: number
  isPassed?: number  // 1-及格（≥60分），0-不及格
  correctCount?: number
  wrongCount?: number
  accuracy?: number
  status: 'ONGOING' | 'FINISHED'
  startTime: string
  submitTime?: string
  createTime: string
  questions?: Question[]
}

/**
 * 提交考试参数
 */
export interface SubmitExamParams {
  answers: Record<number, string>  // 题目ID -> 用户答案
}

/**
 * 考试结果
 */
export interface ExamResult {
  examId: number
  score: number
  totalScore: number
  isPassed: boolean
  accuracy: number
  correctCount: number
  wrongCount: number
  wrongQuestions: Question[]
}

/**
 * 考试配置（人工智能训练师三级）
 */
export const EXAM_CONFIG = {
  judgeCount: 40,       // 判断题数量
  judgeScore: 0.5,      // 判断题每题分数
  singleCount: 140,     // 单选题数量
  singleScore: 0.5,     // 单选题每题分数
  multipleCount: 10,    // 多选题数量
  multipleScore: 1,     // 多选题每题分数
  totalScore: 100,      // 总分
  passScore: 60,        // 及格分
  duration: 90          // 考试时长（分钟）
}

