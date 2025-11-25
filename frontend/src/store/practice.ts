import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { PracticeMode } from '@/types/practice'

/**
 * 练习状态管理
 * 
 * 功能：
 * - 存储当前练习模式（答题模式 / 背题模式）
 * - 存储当前题目列表
 * - 存储答题卡状态
 */
export const usePracticeStore = defineStore('practice', () => {
  // 练习模式：ANSWER-答题模式，RECITE-背题模式
  const practiceMode = ref<PracticeMode>('ANSWER')

  // 当前题目列表
  const questionList = ref<number[]>([])

  // 当前题目索引
  const currentQuestionIndex = ref(0)

  // 答题卡：题目ID -> 用户答案
  const answerSheet = ref<Record<number, string>>({})

  // 切换练习模式
  const toggleMode = () => {
    practiceMode.value = practiceMode.value === 'ANSWER' ? 'RECITE' : 'ANSWER'
  }

  // 设置题目列表
  const setQuestionList = (list: number[]) => {
    questionList.value = list
    currentQuestionIndex.value = 0
    answerSheet.value = {}
  }

  // 跳转到指定题目
  const goToQuestion = (index: number) => {
    if (index >= 0 && index < questionList.value.length) {
      currentQuestionIndex.value = index
    }
  }

  // 记录答案
  const recordAnswer = (questionId: number, answer: string) => {
    answerSheet.value[questionId] = answer
  }

  // 重置状态
  const reset = () => {
    questionList.value = []
    currentQuestionIndex.value = 0
    answerSheet.value = {}
  }

  return {
    practiceMode,
    questionList,
    currentQuestionIndex,
    answerSheet,
    toggleMode,
    setQuestionList,
    goToQuestion,
    recordAnswer,
    reset
  }
})


