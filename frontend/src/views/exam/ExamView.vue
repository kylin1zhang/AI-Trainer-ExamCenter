<template>
  <div class="exam-container">
    <!-- 左侧主内容区 -->
    <div class="exam-main">
      <!-- 顶部操作栏 -->
      <div class="exam-header">
        <el-button @click="handleBack" :disabled="isSubmitting">返回</el-button>
        <div class="exam-info">
          <h3>{{ examTitle }}</h3>
        </div>
        <div class="exam-timer" :class="{ 'timer-warning': remainingMinutes < 10 }">
          <el-icon><Timer /></el-icon>
          <span>剩余时间：{{ formatTime(remainingSeconds) }}</span>
        </div>
      </div>

      <!-- 题目内容 -->
      <el-card v-if="currentQuestion" class="question-card" v-loading="loading">
        <div class="question-header-info">
          <el-tag :type="getQuestionTypeColor(currentQuestion.type)">
            {{ getQuestionTypeName(currentQuestion.type) }}
          </el-tag>
          <span>题目 {{ currentIndex + 1 }} / {{ questions.length }}</span>
        </div>

        <div class="question-content">
          <h3>{{ currentQuestion.question }}</h3>
        </div>

        <div class="question-options">
          <el-radio-group 
            v-if="currentQuestion.type === 'judge' || currentQuestion.type === 'single'"
            v-model="userAnswer"
            @change="handleAnswerChange"
          >
            <el-radio v-if="currentQuestion.optionA" label="A">
              A. {{ currentQuestion.optionA }}
            </el-radio>
            <el-radio v-if="currentQuestion.optionB" label="B">
              B. {{ currentQuestion.optionB }}
            </el-radio>
            <el-radio v-if="currentQuestion.optionC" label="C">
              C. {{ currentQuestion.optionC }}
            </el-radio>
            <el-radio v-if="currentQuestion.optionD" label="D">
              D. {{ currentQuestion.optionD }}
            </el-radio>
            <el-radio v-if="currentQuestion.optionE" label="E">
              E. {{ currentQuestion.optionE }}
            </el-radio>
          </el-radio-group>

          <el-checkbox-group 
            v-else-if="currentQuestion.type === 'multiple'"
            v-model="userAnswerArray"
            @change="handleMultipleAnswerChange"
          >
            <el-checkbox v-if="currentQuestion.optionA" label="A">
              A. {{ currentQuestion.optionA }}
            </el-checkbox>
            <el-checkbox v-if="currentQuestion.optionB" label="B">
              B. {{ currentQuestion.optionB }}
            </el-checkbox>
            <el-checkbox v-if="currentQuestion.optionC" label="C">
              C. {{ currentQuestion.optionC }}
            </el-checkbox>
            <el-checkbox v-if="currentQuestion.optionD" label="D">
              D. {{ currentQuestion.optionD }}
            </el-checkbox>
            <el-checkbox v-if="currentQuestion.optionE" label="E">
              E. {{ currentQuestion.optionE }}
            </el-checkbox>
          </el-checkbox-group>
        </div>

        <!-- 操作按钮 -->
        <div class="question-actions">
          <el-button @click="prevQuestion" :disabled="currentIndex === 0">上一题</el-button>
          <el-button type="primary" @click="nextQuestion" :disabled="currentIndex === questions.length - 1">
            下一题
          </el-button>
        </div>
      </el-card>
    </div>

    <!-- 右侧答题卡 -->
    <div class="answer-card-panel">
      <!-- 可滚动的答题卡区域 -->
      <div class="card-scroll-area">
        <div class="card-header">
          <h3>答题卡</h3>
        </div>

        <!-- 题型分类显示 -->
        <div class="question-types">
          <!-- 判断题 -->
          <div v-if="judgeQuestions.length > 0" class="type-section">
            <div class="type-title">
              <el-tag type="info" size="small">判断题</el-tag>
              <span class="count-text">({{ judgeAnsweredCount }}/{{ judgeQuestions.length }})</span>
            </div>
            <div class="question-grid">
              <div
                v-for="(q, index) in judgeQuestions"
                :key="q.id"
                :class="['question-num', getQuestionClass(q), { 'active': currentIndex === q.originalIndex }]"
                @click="jumpToQuestion(q.originalIndex)"
              >
                {{ index + 1 }}
              </div>
            </div>
          </div>

          <!-- 单选题 -->
          <div v-if="singleQuestions.length > 0" class="type-section">
            <div class="type-title">
              <el-tag type="success" size="small">单选题</el-tag>
              <span class="count-text">({{ singleAnsweredCount }}/{{ singleQuestions.length }})</span>
            </div>
            <div class="question-grid">
              <div
                v-for="(q, index) in singleQuestions"
                :key="q.id"
                :class="['question-num', getQuestionClass(q), { 'active': currentIndex === q.originalIndex }]"
                @click="jumpToQuestion(q.originalIndex)"
              >
                {{ index + 1 }}
              </div>
            </div>
          </div>

          <!-- 多选题 -->
          <div v-if="multipleQuestions.length > 0" class="type-section">
            <div class="type-title">
              <el-tag type="warning" size="small">多选题</el-tag>
              <span class="count-text">({{ multipleAnsweredCount }}/{{ multipleQuestions.length }})</span>
            </div>
            <div class="question-grid">
              <div
                v-for="(q, index) in multipleQuestions"
                :key="q.id"
                :class="['question-num', getQuestionClass(q), { 'active': currentIndex === q.originalIndex }]"
                @click="jumpToQuestion(q.originalIndex)"
              >
                {{ index + 1 }}
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 固定的统计和提交区域 -->
      <div class="card-fixed-area">
        <!-- 答题统计 -->
        <div class="answer-stats">
          <div class="stat-item">
            <span class="stat-label">已答题：</span>
            <span class="stat-value">{{ answeredCount }}/{{ questions.length }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">未答题：</span>
            <span class="stat-value">{{ questions.length - answeredCount }}</span>
          </div>
        </div>

        <!-- 提交按钮 -->
        <div class="submit-area">
          <el-button 
            type="primary" 
            @click="handleSubmit" 
            :loading="isSubmitting"
            :disabled="isSubmitting"
            style="width: 100%"
            size="large"
          >
            {{ isSubmitting ? '提交中...' : '交卷' }}
          </el-button>
        </div>
      </div>
    </div>
  </div>

  <!-- 成绩结果对话框 -->
  <el-dialog 
    v-model="showResultDialog" 
    title="考试成绩" 
    width="600px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :show-close="false"
  >
    <div class="result-content">
      <div class="result-score">
        <div class="score-circle" :class="{ 'passed': examResult?.isPassed }">
          <span class="score-value">{{ examResult?.score }}</span>
          <span class="score-total">/ 100分</span>
        </div>
        <div class="result-status" :class="{ 'passed': examResult?.isPassed }">
          {{ examResult?.isPassed ? '恭喜通过！' : '未通过' }}
        </div>
      </div>

      <div class="result-details">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="总题数">{{ questions.length }}</el-descriptions-item>
          <el-descriptions-item label="答对">
            <span style="color: #67c23a; font-weight: bold;">{{ examResult?.correctCount }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="答错">
            <span style="color: #f56c6c; font-weight: bold;">{{ examResult?.wrongCount }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="正确率">
            <span style="font-weight: bold;">{{ (examResult?.accuracy * 100).toFixed(2) }}%</span>
          </el-descriptions-item>
          <el-descriptions-item label="总分">100分</el-descriptions-item>
          <el-descriptions-item label="及格分">60分</el-descriptions-item>
        </el-descriptions>
      </div>
    </div>

    <template #footer>
      <el-button @click="handleBackToBank">返回题库</el-button>
      <el-button type="primary" @click="viewExamRecord">查看答题记录</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Timer } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getExamDetail, getExamQuestions, submitExam } from '@/api/exam'
import type { Question } from '@/types/question'

const router = useRouter()
const route = useRoute()

// 考试信息 - 安全获取 examId 和 bankId
const examId = computed(() => {
  const id = route.params.examId || route.params.id
  const numId = Number(id)
  if (isNaN(numId) || numId <= 0) {
    console.error('无效的考试ID:', id, 'route.params:', route.params)
    return null
  }
  return numId
})

const bankId = computed(() => {
  const id = route.query.bankId
  const numId = Number(id)
  if (isNaN(numId) || numId <= 0) {
    console.warn('无效的题库ID:', id, '使用默认值1')
    return 1 // 默认题库ID
  }
  return numId
})

const examTitle = ref('模拟考试')
const examDuration = ref(90) // 考试时长（分钟）

// 题目数据
const loading = ref(false)
const questions = ref<Question[]>([])
const currentIndex = ref(0)
const userAnswer = ref('')
const userAnswerArray = ref<string[]>([])
const answerSheet = ref<Record<number, string>>({})

// 倒计时
const remainingSeconds = ref(0)
let timerInterval: number | null = null

// 提交状态
const isSubmitting = ref(false)
const showResultDialog = ref(false)
const examResult = ref<any>(null)

// 当前题目
const currentQuestion = computed(() => questions.value[currentIndex.value])

// 按题型分类
const judgeQuestions = computed(() => {
  return questions.value
    .map((q, index) => ({ ...q, originalIndex: index }))
    .filter(q => q.type === 'judge')
})

const singleQuestions = computed(() => {
  return questions.value
    .map((q, index) => ({ ...q, originalIndex: index }))
    .filter(q => q.type === 'single')
})

const multipleQuestions = computed(() => {
  return questions.value
    .map((q, index) => ({ ...q, originalIndex: index }))
    .filter(q => q.type === 'multiple')
})

// 答题统计
const answeredCount = computed(() => Object.keys(answerSheet.value).length)

const judgeAnsweredCount = computed(() => {
  return judgeQuestions.value.filter(q => answerSheet.value[q.originalIndex]).length
})

const singleAnsweredCount = computed(() => {
  return singleQuestions.value.filter(q => answerSheet.value[q.originalIndex]).length
})

const multipleAnsweredCount = computed(() => {
  return multipleQuestions.value.filter(q => answerSheet.value[q.originalIndex]).length
})

const remainingMinutes = computed(() => Math.floor(remainingSeconds.value / 60))

// 加载考试数据
const loadExam = async () => {
  loading.value = true
  try {
    const currentExamId = examId.value
    if (!currentExamId) {
      ElMessage.error('考试ID无效')
      router.back()
      return
    }

    // 获取考试信息
    const examResponse = await getExamDetail(currentExamId)
    if (examResponse.code === 200 && examResponse.data) {
      examTitle.value = examResponse.data.title || '模拟考试'
      examDuration.value = examResponse.data.duration || 90
      
      // 计算剩余时间
      const startTime = new Date(examResponse.data.startTime).getTime()
      const elapsed = Math.floor((Date.now() - startTime) / 1000)
      remainingSeconds.value = Math.max(0, examDuration.value * 60 - elapsed)
    }

    // 获取题目列表
    const questionsResponse = await getExamQuestions(currentExamId)
    if (questionsResponse.code === 200 && questionsResponse.data) {
      questions.value = questionsResponse.data
      
      if (questions.value.length === 0) {
        ElMessage.warning('考试暂无题目')
        return
      }

      // 启动倒计时
      startTimer()
    }
  } catch (error: any) {
    ElMessage.error(error.message || '加载考试失败')
    router.back()
  } finally {
    loading.value = false
  }
}

// 启动倒计时
const startTimer = () => {
  if (timerInterval) {
    clearInterval(timerInterval)
  }

  timerInterval = window.setInterval(() => {
    if (remainingSeconds.value > 0) {
      remainingSeconds.value--
    } else {
      // 时间到，自动提交
      clearInterval(timerInterval!)
      handleAutoSubmit()
    }
  }, 1000)
}

// 格式化时间显示
const formatTime = (seconds: number) => {
  const hours = Math.floor(seconds / 3600)
  const minutes = Math.floor((seconds % 3600) / 60)
  const secs = seconds % 60
  
  if (hours > 0) {
    return `${hours}:${String(minutes).padStart(2, '0')}:${String(secs).padStart(2, '0')}`
  }
  return `${minutes}:${String(secs).padStart(2, '0')}`
}

// 获取题型名称
const getQuestionTypeName = (type: string) => {
  const typeMap: Record<string, string> = {
    judge: '判断题',
    single: '单选题',
    multiple: '多选题'
  }
  return typeMap[type] || type
}

// 获取题型颜色
const getQuestionTypeColor = (type: string) => {
  const colorMap: Record<string, string> = {
    judge: 'info',
    single: 'success',
    multiple: 'warning'
  }
  return colorMap[type] || 'info'
}

// 处理单选/判断题答案变化
const handleAnswerChange = (value: string) => {
  if (!currentQuestion.value) return
  userAnswer.value = value
  answerSheet.value[currentIndex.value] = value
}

// 处理多选题答案变化
const handleMultipleAnswerChange = (values: string[]) => {
  if (!currentQuestion.value) return
  userAnswerArray.value = values
  answerSheet.value[currentIndex.value] = values.sort().join(',')
}

// 获取题目样式类
const getQuestionClass = (question: any) => {
  const hasAnswer = answerSheet.value[question.originalIndex]
  return hasAnswer ? 'answered' : ''
}

// 跳转到指定题目
const jumpToQuestion = (index: number) => {
  currentIndex.value = index
  resetQuestionState()
}

// 上一题
const prevQuestion = () => {
  if (currentIndex.value > 0) {
    jumpToQuestion(currentIndex.value - 1)
  }
}

// 下一题
const nextQuestion = () => {
  if (currentIndex.value < questions.value.length - 1) {
    jumpToQuestion(currentIndex.value + 1)
  }
}

// 重置题目状态
const resetQuestionState = () => {
  if (currentQuestion.value) {
    const savedAnswer = answerSheet.value[currentIndex.value]
    
    if (currentQuestion.value.type === 'multiple') {
      userAnswerArray.value = savedAnswer ? savedAnswer.split(',') : []
    } else {
      userAnswer.value = savedAnswer || ''
    }
  }
}

// 提交考试
const handleSubmit = async () => {
  // 检查是否还有未答题
  const unansweredCount = questions.value.length - answeredCount.value
  if (unansweredCount > 0) {
    try {
      await ElMessageBox.confirm(
        `还有 ${unansweredCount} 题未作答，确定要提交吗？`,
        '提示',
        {
          confirmButtonText: '确定提交',
          cancelButtonText: '继续答题',
          type: 'warning'
        }
      )
    } catch {
      return
    }
  } else {
    try {
      await ElMessageBox.confirm(
        '确定要提交试卷吗？',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        }
      )
    } catch {
      return
    }
  }

  await doSubmitExam()
}

// 自动提交（时间到）
const handleAutoSubmit = async () => {
  ElMessage.warning('考试时间已到，系统自动提交试卷')
  await doSubmitExam()
}

// 执行提交
const doSubmitExam = async () => {
  isSubmitting.value = true
  
  try {
    const currentExamId = examId.value
    if (!currentExamId) {
      ElMessage.error('考试ID无效')
      return
    }

    // 构建答案对象
    const answers: Record<string, string> = {}
    questions.value.forEach((q, index) => {
      if (answerSheet.value[index]) {
        answers[q.id.toString()] = answerSheet.value[index]
      }
    })

    const response = await submitExam(currentExamId, { answers })
    
    if (response.code === 200 && response.data) {
      examResult.value = response.data
      showResultDialog.value = true
      
      // 停止倒计时
      if (timerInterval) {
        clearInterval(timerInterval)
      }
    }
  } catch (error: any) {
    ElMessage.error(error.message || '提交失败')
  } finally {
    isSubmitting.value = false
  }
}

// 返回题库
const handleBackToBank = () => {
  router.push(`/banks/${bankId.value}`)
}

// 返回（考试中）
const handleBack = async () => {
  try {
    await ElMessageBox.confirm(
      '考试尚未结束，确定要退出吗？退出后将无法继续作答。',
      '提示',
      {
        confirmButtonText: '确定退出',
        cancelButtonText: '继续考试',
        type: 'warning'
      }
    )
    router.back()
  } catch {
    // 用户取消
  }
}

// 查看答题记录
const viewExamRecord = () => {
  // TODO: 实现查看答题记录功能
  ElMessage.info('查看答题记录功能开发中...')
}

// 页面加载
onMounted(() => {
  loadExam()
})

// 页面卸载时清除定时器
onUnmounted(() => {
  if (timerInterval) {
    clearInterval(timerInterval)
  }
})
</script>

<style scoped>
.exam-container {
  display: flex;
  gap: 20px;
  padding: 20px;
  height: calc(100vh - 60px);
  max-width: 1800px;
  margin: 0 auto;
}

.exam-main {
  flex: 1;
  overflow-y: auto;
  min-width: 0;
}

.exam-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 16px 24px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.exam-info h3 {
  margin: 0;
  font-size: 18px;
  color: #303133;
  font-weight: 600;
}

.exam-timer {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  color: #409eff;
  font-weight: 600;
}

.exam-timer.timer-warning {
  color: #f56c6c;
  animation: pulse 1s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.6; }
}

.question-card {
  padding: 24px;
}

.question-header-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid #ebeef5;
}

.question-content h3 {
  font-size: 18px;
  color: #303133;
  margin-bottom: 24px;
  line-height: 1.8;
}

.question-options {
  margin-bottom: 24px;
}

.question-options :deep(.el-radio),
.question-options :deep(.el-checkbox) {
  display: block;
  margin-bottom: 12px;
  padding: 12px;
  border-radius: 4px;
  transition: all 0.2s;
}

.question-options :deep(.el-radio:hover),
.question-options :deep(.el-checkbox:hover) {
  background: #f5f7fa;
}

.question-actions {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  margin-top: 24px;
}

/* 右侧答题卡 */
.answer-card-panel {
  width: 300px;
  flex-shrink: 0;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  height: calc(100vh - 100px);
  position: sticky;
  top: 20px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.card-scroll-area {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  padding-bottom: 8px;
}

.card-fixed-area {
  flex-shrink: 0;
  padding: 0 16px 16px 16px;
  background: white;
  border-top: 2px solid #f0f0f0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 2px solid #f0f0f0;
}

.card-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.question-types {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding-bottom: 8px;
}

.type-section {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.type-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-weight: 500;
  font-size: 14px;
}

.count-text {
  color: #909399;
  font-size: 12px;
}

.question-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 8px;
}

.question-num {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  font-weight: 500;
  transition: all 0.2s;
  background: white;
  color: #606266;
}

.question-num:hover {
  border-color: #409eff;
  color: #409eff;
  transform: scale(1.05);
}

.question-num.answered {
  background: #e6f7ff;
  border-color: #409eff;
  color: #409eff;
}

.question-num.active {
  background: #409eff;
  color: white;
  border-color: #409eff;
}

.answer-stats {
  margin-top: 12px;
  padding: 12px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e9ecef 100%);
  border-radius: 6px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
}

.stat-label {
  color: #606266;
  font-weight: 500;
}

.stat-value {
  font-weight: bold;
  color: #303133;
  font-size: 14px;
}

.submit-area {
  margin-top: 16px;
}

/* 成绩对话框 */
.result-content {
  padding: 20px 0;
}

.result-score {
  text-align: center;
  margin-bottom: 30px;
}

.score-circle {
  width: 180px;
  height: 180px;
  border-radius: 50%;
  background: linear-gradient(135deg, #f56c6c 0%, #ff8787 100%);
  display: inline-flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
  margin-bottom: 20px;
  box-shadow: 0 8px 24px rgba(245, 108, 108, 0.3);
}

.score-circle.passed {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  box-shadow: 0 8px 24px rgba(103, 194, 58, 0.3);
}

.score-value {
  font-size: 52px;
  font-weight: bold;
  line-height: 1;
}

.score-total {
  font-size: 18px;
  margin-top: 8px;
}

.result-status {
  font-size: 28px;
  font-weight: bold;
  color: #f56c6c;
}

.result-status.passed {
  color: #67c23a;
}

.result-details {
  margin-top: 20px;
}
</style>
