/**
 * 题目相关类型定义
 * 
 * 对应 data.json 数据结构：
 * {
 *   "id": 1,
 *   "type": "judge",
 *   "question": "题干内容",
 *   "option_A": "选项A",
 *   "option_B": "选项B",
 *   "option_C": "选项C",
 *   "option_D": "选项D",
 *   "option_E": "选项E",
 *   "answer": "A",
 *   "explanation": "详细解析"
 * }
 */

/**
 * 题目类型
 * judge - 判断题
 * single - 单选题
 * multiple - 多选题
 */
export type QuestionType = 'judge' | 'single' | 'multiple'

/**
 * 题目实体（匹配 data.json 和后端实体）
 */
export interface Question {
  id: number
  bankId: number
  type: QuestionType
  question: string
  optionA: string | null
  optionB: string | null
  optionC: string | null
  optionD: string | null
  optionE: string | null
  answer: string
  explanation: string
  sequenceNumber: number
  // 扩展字段（根据需要返回）
  userAnswer?: string
  isCorrect?: boolean
  isFavorite?: boolean
  hasNote?: boolean
}

/**
 * 题目查询参数
 */
export interface QuestionListParams {
  bankId: number
  page?: number
  size?: number
}

/**
 * 题型统计
 */
export interface QuestionTypeCount {
  judge: number
  single: number
  multiple: number
  total: number
}

