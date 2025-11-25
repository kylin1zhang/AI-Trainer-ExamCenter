<template>
  <div class="practice-container">
    <!-- 左侧主内容区 -->
    <div class="practice-main">
      <!-- 顶部操作栏 -->
      <div class="practice-header">
        <el-button @click="goBack">返回</el-button>
        <div class="practice-info">
          <h3>{{ getModeName() }}</h3>
          <span>题目 {{ currentIndex + 1 }} / {{ questions.length }}</span>
        </div>
        <div class="practice-actions">
          <!-- 模式切换 -->
          <el-switch
            v-model="isReciteMode"
            active-text="背题模式"
            inactive-text="答题模式"
            @change="handleModeChange"
          />
        </div>
      </div>

      <!-- 题目内容 -->
      <el-card v-if="currentQuestion" class="question-card" v-loading="loading">
        <div class="question-content">
          <h3>【{{ getQuestionTypeName(currentQuestion.type) }}】{{ currentQuestion.question }}</h3>
        </div>

        <div class="question-options">
          <el-radio-group 
            v-if="currentQuestion.type === 'judge' || currentQuestion.type === 'single'"
            v-model="userAnswer"
            @change="handleAnswerChange"
          >
            <el-radio 
              v-if="currentQuestion.optionA" 
              label="A"
              :class="{ 'correct-answer': showResult && currentQuestion.answer === 'A', 'wrong-answer': showResult && userAnswer === 'A' && !isCorrect }"
            >
              A. {{ currentQuestion.optionA }}
            </el-radio>
            <el-radio 
              v-if="currentQuestion.optionB" 
              label="B"
              :class="{ 'correct-answer': showResult && currentQuestion.answer === 'B', 'wrong-answer': showResult && userAnswer === 'B' && !isCorrect }"
            >
              B. {{ currentQuestion.optionB }}
            </el-radio>
            <el-radio 
              v-if="currentQuestion.optionC" 
              label="C"
              :class="{ 'correct-answer': showResult && currentQuestion.answer === 'C', 'wrong-answer': showResult && userAnswer === 'C' && !isCorrect }"
            >
              C. {{ currentQuestion.optionC }}
            </el-radio>
            <el-radio 
              v-if="currentQuestion.optionD" 
              label="D"
              :class="{ 'correct-answer': showResult && currentQuestion.answer === 'D', 'wrong-answer': showResult && userAnswer === 'D' && !isCorrect }"
            >
              D. {{ currentQuestion.optionD }}
            </el-radio>
            <el-radio 
              v-if="currentQuestion.optionE" 
              label="E"
              :class="{ 'correct-answer': showResult && currentQuestion.answer === 'E', 'wrong-answer': showResult && userAnswer === 'E' && !isCorrect }"
            >
              E. {{ currentQuestion.optionE }}
            </el-radio>
          </el-radio-group>

          <el-checkbox-group 
            v-else-if="currentQuestion.type === 'multiple'"
            v-model="userAnswerArray"
            @change="handleMultipleAnswerChange"
          >
            <el-checkbox 
              v-if="currentQuestion.optionA" 
              label="A"
              :class="{ 'correct-answer': showResult && currentQuestion.answer.includes('A'), 'wrong-answer': showResult && userAnswerArray.includes('A') && !currentQuestion.answer.includes('A') }"
            >
              A. {{ currentQuestion.optionA }}
            </el-checkbox>
            <el-checkbox 
              v-if="currentQuestion.optionB" 
              label="B"
              :class="{ 'correct-answer': showResult && currentQuestion.answer.includes('B'), 'wrong-answer': showResult && userAnswerArray.includes('B') && !currentQuestion.answer.includes('B') }"
            >
              B. {{ currentQuestion.optionB }}
            </el-checkbox>
            <el-checkbox 
              v-if="currentQuestion.optionC" 
              label="C"
              :class="{ 'correct-answer': showResult && currentQuestion.answer.includes('C'), 'wrong-answer': showResult && userAnswerArray.includes('C') && !currentQuestion.answer.includes('C') }"
            >
              C. {{ currentQuestion.optionC }}
            </el-checkbox>
            <el-checkbox 
              v-if="currentQuestion.optionD" 
              label="D"
              :class="{ 'correct-answer': showResult && currentQuestion.answer.includes('D'), 'wrong-answer': showResult && userAnswerArray.includes('D') && !currentQuestion.answer.includes('D') }"
            >
              D. {{ currentQuestion.optionD }}
            </el-checkbox>
            <el-checkbox 
              v-if="currentQuestion.optionE" 
              label="E"
              :class="{ 'correct-answer': showResult && currentQuestion.answer.includes('E'), 'wrong-answer': showResult && userAnswerArray.includes('E') && !currentQuestion.answer.includes('E') }"
            >
              E. {{ currentQuestion.optionE }}
            </el-checkbox>
          </el-checkbox-group>
        </div>

        <!-- 答题模式：选完立即判断 -->
        <div v-if="!isReciteMode && showResult" class="answer-result">
          <el-alert :type="isCorrect ? 'success' : 'error'" :closable="false">
            <template #title>
              {{ isCorrect ? '回答正确 ✓' : '回答错误 ✗' }}
            </template>
          </el-alert>
          <div class="answer-explanation">
            <h4>正确答案：{{ currentQuestion.answer }}</h4>
            <p v-if="currentQuestion.explanation" v-html="formatExplanation(currentQuestion.explanation)"></p>
          </div>
        </div>

        <!-- 背题模式：直接显示答案 -->
        <div v-if="isReciteMode" class="answer-explanation">
          <h4>正确答案：{{ currentQuestion.answer }}</h4>
          <p v-if="currentQuestion.explanation" v-html="formatExplanation(currentQuestion.explanation)"></p>
        </div>

        <!-- 操作按钮 -->
        <div class="question-actions">
          <el-button @click="prevQuestion" :disabled="currentIndex === 0">上一题</el-button>
          <el-button @click="handleFavorite" :type="currentQuestion.isFavorite ? 'warning' : 'default'">
            {{ currentQuestion.isFavorite ? '已收藏' : '收藏' }}
          </el-button>
          <el-button @click="showNoteDialog = true">记笔记</el-button>
          <el-button type="primary" @click="nextQuestion" :disabled="currentIndex === questions.length - 1">
            下一题
          </el-button>
        </div>
      </el-card>
    </div>

    <!-- 右侧答题卡 -->
    <div class="answer-card-panel">
      <!-- 上半部分：答题卡（可滚动） -->
      <div class="card-scroll-area">
        <div class="card-header">
          <h3>答题卡</h3>
          <el-button link size="small" @click="clearAnswers">清空答题记录</el-button>
        </div>

        <!-- 题型分类显示 -->
        <div class="question-types">
          <!-- 判断题 -->
          <div v-if="judgeQuestions.length > 0" class="type-section">
            <div class="type-title">
              <el-tag type="info" size="small">判断题</el-tag>
            </div>
            <div class="question-grid">
              <div
                v-for="(q, index) in judgeQuestions"
                :key="q.id"
                :class="['question-num', getQuestionClass(q)]"
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
            </div>
            <div class="question-grid">
              <div
                v-for="(q, index) in singleQuestions"
                :key="q.id"
                :class="['question-num', getQuestionClass(q)]"
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
            </div>
            <div class="question-grid">
              <div
                v-for="(q, index) in multipleQuestions"
                :key="q.id"
                :class="['question-num', getQuestionClass(q)]"
                @click="jumpToQuestion(q.originalIndex)"
              >
                {{ index + 1 }}
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 下半部分：统计信息和设置（固定） -->
      <div class="card-fixed-area">
        <!-- 统计信息 -->
        <div class="answer-stats">
          <div class="stat-item">
            <span class="stat-label">答对：</span>
            <span class="stat-value correct">{{ correctCount }}题</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">答错：</span>
            <span class="stat-value wrong">{{ wrongCount }}题</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">正确率：</span>
            <span class="stat-value">{{ accuracyRate }}</span>
          </div>
        </div>

        <!-- 查看成绩 -->
        <div v-if="hasAnswered" class="card-footer">
          <el-button type="primary" @click="showResults" style="width: 100%">查看练习成绩</el-button>
        </div>

        <!-- 设置项 -->
        <div class="answer-settings">
          <div class="settings-title">设置</div>
          <div class="setting-item">
            <span>答对自动下一题</span>
            <el-switch v-model="autoNext" size="small" />
          </div>
          <div class="setting-item">
            <span>背题模式</span>
            <el-switch v-model="isReciteMode" size="small" @change="handleModeChange" />
          </div>
        </div>
      </div>
    </div>

    <!-- 笔记对话框 -->
    <el-dialog v-model="showNoteDialog" title="添加笔记" width="500px">
      <el-input v-model="noteContent" type="textarea" :rows="6" placeholder="请输入笔记内容" />
      <template #footer>
        <el-button @click="showNoteDialog = false">取消</el-button>
        <el-button type="primary" @click="saveNote">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { usePracticeStore } from '@/store/practice'
import { getRandomQuestions, getSequenceQuestions, getQuestionsByType } from '@/api/question'
import { submitAnswer, getWrongQuestions, getFavoriteQuestions, getDifficultQuestions } from '@/api/practice'
import { favoriteQuestion, unfavoriteQuestion, addNote } from '@/api/practice'
import type { Question } from '@/types/question'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const practiceStore = usePracticeStore()

// 练习状态
const loading = ref(false)
const questions = ref<Question[]>([])
const currentIndex = ref(0)
const isReciteMode = ref(false)
const userAnswer = ref('')
const userAnswerArray = ref<string[]>([])
const showResult = ref(false)
const isCorrect = ref(false)
const answerSheet = ref<Record<number, string>>({})
const answerResults = ref<Record<number, { isCorrect: boolean }>>({})
const autoNext = ref(true)

// UI 状态
const showNoteDialog = ref(false)
const noteContent = ref('')

const currentQuestion = computed(() => {
  return questions.value[currentIndex.value]
})

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

// 统计信息
const correctCount = computed(() => {
  return Object.values(answerResults.value).filter(r => r.isCorrect).length
})

const wrongCount = computed(() => {
  return Object.values(answerResults.value).filter(r => !r.isCorrect).length
})

const accuracyRate = computed(() => {
  const total = Object.keys(answerResults.value).length
  if (total === 0) return '0 %'
  return ((correctCount.value / total) * 100).toFixed(2) + ' %'
})

const hasAnswered = computed(() => {
  return Object.keys(answerResults.value).length > 0
})

// 获取题目样式类
const getQuestionClass = (question: any) => {
  const record = answerResults.value[question.originalIndex]
  if (!record) return ''
  return record.isCorrect ? 'correct' : 'wrong'
}

// 跳转到指定题目
const jumpToQuestion = (index: number) => {
  currentIndex.value = index
  resetQuestionState()
}

// 加载题目
const loadQuestions = async () => {
  loading.value = true
  try {
    const bankId = Number(route.query.bankId)
    const type = route.query.type as string
    const mode = route.query.mode as string

    let response
    if (mode === 'random') {
      response = await getRandomQuestions(bankId, 1000) // 增加到1000题
    } else if (mode === 'sequence') {
      response = await getSequenceQuestions({ bankId, page: 1, size: 1000 }) // 增加到1000题
    } else if (mode === 'wrong') {
      response = await getWrongQuestions(bankId)
    } else if (mode === 'favorite') {
      response = await getFavoriteQuestions(bankId)
    } else if (mode === 'difficult') {
      // 易错题（错误次数 >= 2）
      response = await getDifficultQuestions(bankId)
    } else if (type) {
      response = await getQuestionsByType({ bankId, type, page: 1, size: 1000 }) // 增加到1000题
    } else {
      response = await getRandomQuestions(bankId, 1000) // 增加到1000题
    }

    if (response.code === 200 && response.data) {
      questions.value = response.data
      if (questions.value.length === 0) {
        ElMessage.warning('暂无题目')
      }
      if (isReciteMode.value) {
        showResult.value = true
      }
    }
  } catch (error: any) {
    ElMessage.error(error.message || '加载题目失败')
  } finally {
    loading.value = false
  }
}

// 获取模式名称
const getModeName = () => {
  const mode = route.query.mode as string
  const type = route.query.type as string
  const modeMap: Record<string, string> = {
    sequence: '顺序练习',
    random: '随机练习',
    wrong: '我的错题',
    favorite: '我的收藏',
    difficult: '易错题',
    type: '题型练习'
  }
  
  if (mode && modeMap[mode]) {
    if (mode === 'type' && type) {
      const typeMap: Record<string, string> = {
        judge: '判断题',
        single: '单选题',
        multiple: '多选题'
      }
      return `${typeMap[type] || type}练习`
    }
    return modeMap[mode]
  }
  return '练习'
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

// 格式化解析（支持换行）
const formatExplanation = (text: string) => {
  return text.replace(/\n/g, '<br>')
}

// 处理答案变化（单选/判断）
const handleAnswerChange = async (value: string) => {
  if (!currentQuestion.value) return
  
  userAnswer.value = value
  answerSheet.value[currentQuestion.value.id] = value

  if (!isReciteMode.value) {
    await submitAnswerToServer()
  }
}

// 处理多选题答案变化
const handleMultipleAnswerChange = async (values: string[]) => {
  if (!currentQuestion.value) return
  
  userAnswerArray.value = values
  const answer = values.sort().join(',')
  answerSheet.value[currentQuestion.value.id] = answer

  if (!isReciteMode.value) {
    await submitAnswerToServer()
  }
}

// 提交答案到服务器
const submitAnswerToServer = async () => {
  if (!currentQuestion.value) return

  try {
    const response = await submitAnswer({
      questionId: currentQuestion.value.id,
      userAnswer: currentQuestion.value.type === 'multiple' 
        ? userAnswerArray.value.sort().join(',')
        : userAnswer.value,
      timeCost: 0,
      practiceMode: isReciteMode.value ? 'RECITE' : 'ANSWER'
    })

    if (response.code === 200 && response.data) {
      isCorrect.value = response.data.isCorrect
      showResult.value = true
      answerResults.value[currentIndex.value] = {
        isCorrect: response.data.isCorrect
      }

      // 答对且开启自动下一题
      if (response.data.isCorrect && autoNext.value) {
        setTimeout(() => {
          nextQuestion()
        }, 1000)
      }
    }
  } catch (error: any) {
    ElMessage.error(error.message || '提交答案失败')
  }
}

// 模式切换
const handleModeChange = () => {
  if (isReciteMode.value) {
    showResult.value = true
  } else {
    showResult.value = false
  }
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
  } else {
    ElMessage.info('已经是最后一题了')
  }
}

// 重置题目状态
const resetQuestionState = () => {
  showResult.value = isReciteMode.value
  if (currentQuestion.value) {
    const savedAnswer = answerSheet.value[currentQuestion.value.id]
    const savedResult = answerResults.value[currentIndex.value]
    
    if (currentQuestion.value.type === 'multiple') {
      userAnswerArray.value = savedAnswer ? savedAnswer.split(',') : []
    } else {
      userAnswer.value = savedAnswer || ''
    }
    
    if (savedResult) {
      showResult.value = true
      isCorrect.value = savedResult.isCorrect
    }
  }
}

// 清空答题记录
const clearAnswers = () => {
  answerSheet.value = {}
  answerResults.value = {}
  userAnswer.value = ''
  userAnswerArray.value = []
  showResult.value = isReciteMode.value
  currentIndex.value = 0
  ElMessage.success('已清空答题记录')
}

// 查看练习成绩
const showResults = () => {
  const total = questions.value.length
  const answered = Object.keys(answerResults.value).length
  ElMessage({
    message: `总题数：${total}\n已答题：${answered}\n答对：${correctCount.value}\n答错：${wrongCount.value}\n正确率：${accuracyRate.value}`,
    type: 'info',
    duration: 5000,
    showClose: true
  })
}

// 收藏题目
const handleFavorite = async () => {
  if (!currentQuestion.value) return

  try {
    if (currentQuestion.value.isFavorite) {
      await unfavoriteQuestion(currentQuestion.value.id)
      currentQuestion.value.isFavorite = false
      ElMessage.success('已取消收藏')
    } else {
      await favoriteQuestion(currentQuestion.value.id)
      currentQuestion.value.isFavorite = true
      ElMessage.success('已收藏')
    }
  } catch (error: any) {
    ElMessage.error(error.message || '操作失败')
  }
}

// 保存笔记
const saveNote = async () => {
  if (!currentQuestion.value) return

  try {
    await addNote({
      questionId: currentQuestion.value.id,
      content: noteContent.value
    })
    ElMessage.success('笔记保存成功')
    showNoteDialog.value = false
    noteContent.value = ''
  } catch (error: any) {
    ElMessage.error(error.message || '保存笔记失败')
  }
}

const goBack = () => {
  router.push({
    path: `/banks/${route.query.bankId}`
  })
}

onMounted(() => {
  loadQuestions()
})
</script>

<style scoped>
.practice-container {
  display: flex;
  gap: 20px;
  padding: 20px;
  height: calc(100vh - 60px);
  max-width: 1800px;
  margin: 0 auto;
}

.practice-main {
  flex: 1;
  overflow-y: auto;
  min-width: 0;
}

.practice-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 16px 24px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.practice-info {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.practice-actions {
  display: flex;
  gap: 16px;
  align-items: center;
}

.question-card {
  padding: 24px;
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

.correct-answer {
  background: #f0f9ff !important;
  color: #67c23a !important;
  font-weight: 600;
}

.wrong-answer {
  background: #fef0f0 !important;
  color: #f56c6c !important;
  font-weight: 600;
}

.answer-result {
  margin: 24px 0;
}

.answer-explanation {
  margin-top: 16px;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 4px;
}

.answer-explanation h4 {
  color: #409eff;
  margin-bottom: 8px;
}

.answer-explanation p {
  color: #606266;
  line-height: 1.8;
}

.question-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}

/* 右侧答题卡面板 */
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

/* 上半部分：可滚动的答题卡区域 */
.card-scroll-area {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  padding: 16px;
  padding-bottom: 8px;
}

/* 下半部分：固定的统计和设置区域 */
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
  gap: 6px;
  font-weight: 500;
  font-size: 14px;
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
  box-shadow: 0 2px 6px rgba(64, 158, 255, 0.3);
}

.question-num.correct {
  background: #67c23a;
  color: white;
  border-color: #67c23a;
}

.question-num.wrong {
  background: #f56c6c;
  color: white;
  border-color: #f56c6c;
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

.stat-value.correct {
  color: #67c23a;
}

.stat-value.wrong {
  color: #f56c6c;
}

.answer-settings {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.settings-title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.setting-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  color: #606266;
}

.card-footer {
  margin-top: 12px;
  margin-bottom: 8px;
}
</style>
