import request from './request'
import type { 
  CreateExamParams, 
  Exam, 
  ExamResult,
  SubmitExamParams 
} from '@/types/exam'

/**
 * 模拟考试 API
 */

// 创建模拟考试
export const createExam = (data: CreateExamParams) => {
  return request.post<Exam>('/exams', data)
}

// 获取考试详情
export const getExamDetail = (examId: number) => {
  return request.get<Exam>(`/exams/${examId}`)
}

// 提交考试
export const submitExam = (examId: number, data: SubmitExamParams) => {
  return request.post<ExamResult>(`/exams/${examId}/submit`, data)
}

// 获取考试记录
export const getExamRecords = (bankId: number) => {
  return request.get<Exam[]>('/exams/records', { 
    params: { bankId } 
  })
}

// 获取考试题目列表
export const getExamQuestions = (examId: number) => {
  return request.get<any[]>(`/exams/${examId}/questions`)
}


