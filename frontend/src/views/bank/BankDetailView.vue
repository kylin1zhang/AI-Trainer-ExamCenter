<template>
  <div class="bank-detail-view">
    <h2 class="page-title">人工智能训练师（三级/高级）</h2>

    <el-tabs v-model="activeTab" class="detail-tabs">
      <!-- 我的练习 -->
      <el-tab-pane label="我的练习" name="my-practice">
        <div class="practice-grid">
          <el-card shadow="hover" class="practice-item">
            <template #header>
              <div class="card-header">
                <el-icon><Document /></el-icon>
                <span>我的错题</span>
              </div>
            </template>
            <p>错题数量: {{ wrongCount }}</p>
            <el-button type="primary" text @click="viewWrongQuestions">开始练习</el-button>
          </el-card>

          <el-card shadow="hover" class="practice-item">
            <template #header>
              <div class="card-header">
                <el-icon><Star /></el-icon>
                <span>我的收藏</span>
              </div>
            </template>
            <p>收藏数量: {{ favoriteCount }}</p>
            <el-button type="primary" text @click="viewFavorites">查看收藏</el-button>
          </el-card>

          <el-card shadow="hover" class="practice-item">
            <template #header>
              <div class="card-header">
                <el-icon><EditPen /></el-icon>
                <span>我的笔记</span>
              </div>
            </template>
            <p>笔记数量: {{ noteCount }}</p>
            <el-button type="primary" text @click="viewNotes">查看笔记</el-button>
          </el-card>
        </div>
      </el-tab-pane>

      <!-- 专项练习 -->
      <el-tab-pane label="专项练习" name="special-practice">
        <div class="practice-grid">
          <el-card shadow="hover" class="practice-item">
            <template #header>
              <div class="card-header">
                <el-icon><List /></el-icon>
                <span>顺序练习</span>
              </div>
            </template>
            <p>按题目顺序依次练习</p>
            <el-button type="primary" text @click="startPractice('sequence')">开始练习</el-button>
          </el-card>

          <el-card shadow="hover" class="practice-item">
            <template #header>
              <div class="card-header">
                <el-icon><Refresh /></el-icon>
                <span>随机练习</span>
              </div>
            </template>
            <p>随机抽取题目练习</p>
            <el-button type="primary" text @click="startPractice('random')">开始练习</el-button>
          </el-card>

          <el-card shadow="hover" class="practice-item">
            <template #header>
              <div class="card-header">
                <el-icon><Menu /></el-icon>
                <span>题型练习</span>
              </div>
            </template>
            <p>按题型分类练习</p>
            <el-button type="primary" text @click="showTypeSelector">选择题型</el-button>
          </el-card>

          <el-card shadow="hover" class="practice-item">
            <template #header>
              <div class="card-header">
                <el-icon><Files /></el-icon>
                <span>模拟考试</span>
              </div>
            </template>
            <p>完整模拟真实考试</p>
            <el-button type="primary" text @click="startExam">开始考试</el-button>
          </el-card>

          <el-card shadow="hover" class="practice-item">
            <template #header>
              <div class="card-header">
                <el-icon><Warning /></el-icon>
                <span>易错题</span>
              </div>
            </template>
            <p>易错题数量: {{ difficultCount }}</p>
            <el-button type="primary" text @click="viewDifficultQuestions">开始练习</el-button>
          </el-card>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 题型选择对话框 -->
    <el-dialog v-model="typeSelectorDialogVisible" title="题型练习" width="400px">
      <div style="padding: 20px 0;">
        <p style="margin-bottom: 20px; color: #606266;">请选择题型：</p>
        <el-radio-group v-model="selectedQuestionType" style="display: flex; flex-direction: column; gap: 16px;">
          <el-radio label="judge" size="large">
            <span style="font-size: 16px;">判断题</span>
          </el-radio>
          <el-radio label="single" size="large">
            <span style="font-size: 16px;">单选题</span>
          </el-radio>
          <el-radio label="multiple" size="large">
            <span style="font-size: 16px;">多选题</span>
          </el-radio>
        </el-radio-group>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="typeSelectorDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="startTypePractice">开始练习</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Document, Star, EditPen, List, Refresh, Menu, Files, Warning } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getWrongQuestions, getFavoriteQuestions, getNotes, getDifficultQuestions } from '@/api/practice'
import { createExam } from '@/api/exam'

const router = useRouter()
const route = useRoute()
const activeTab = ref('special-practice')

// 获取题库ID
const bankId = computed(() => {
  // 优先从 params.id 获取，其次从 query.bankId 获取
  let id = route.params.id
  if (!id || id === 'undefined' || id === 'null') {
    id = route.query.bankId
  }
  
  // 如果还是没有，尝试从路径中提取
  if (!id || id === 'undefined' || id === 'null') {
    const pathMatch = route.path.match(/\/banks\/(\d+)/)
    if (pathMatch) {
      id = pathMatch[1]
    }
  }
  
  const numId = Number(id)
  if (isNaN(numId) || numId <= 0) {
    console.error('无效的题库ID:', id, 'route.params:', route.params, 'route.query:', route.query)
    return 1 // 默认题库ID为1
  }
  return numId
})

// 统计数据
const wrongCount = ref(0)
const favoriteCount = ref(0)
const noteCount = ref(0)
const difficultCount = ref(0)

// 加载统计数据
const loadStats = async () => {
  try {
    const currentBankId = bankId.value
    if (!currentBankId || isNaN(currentBankId)) {
      return
    }

    // 获取错题数量
    const wrongRes = await getWrongQuestions(currentBankId)
    if (wrongRes.code === 200 && wrongRes.data) {
      wrongCount.value = wrongRes.data.length
    }

    // 获取收藏数量
    const favoriteRes = await getFavoriteQuestions(currentBankId)
    if (favoriteRes.code === 200 && favoriteRes.data) {
      favoriteCount.value = favoriteRes.data.length
    }

    // 获取笔记数量
    const noteRes = await getNotes(currentBankId)
    if (noteRes.code === 200 && noteRes.data) {
      noteCount.value = noteRes.data.length || 0
    }

    // 获取易错题数量
    const difficultRes = await getDifficultQuestions(currentBankId)
    if (difficultRes.code === 200 && difficultRes.data) {
      difficultCount.value = difficultRes.data.length || 0
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

// 查看我的错题
const viewWrongQuestions = () => {
  router.push({
    path: '/practice',
    query: {
      bankId: bankId.value,
      mode: 'wrong'
    }
  })
}

// 查看我的收藏
const viewFavorites = () => {
  router.push({
    path: '/practice',
    query: {
      bankId: bankId.value,
      mode: 'favorite'
    }
  })
}

// 查看我的笔记
const viewNotes = () => {
  router.push({
    path: '/notes',
    query: {
      bankId: bankId.value
    }
  })
}

// 开始练习
const startPractice = (mode: string) => {
  router.push({
    path: '/practice',
    query: {
      bankId: bankId.value,
      mode: mode
    }
  })
}

// 题型选择对话框
const typeSelectorDialogVisible = ref(false)
const selectedQuestionType = ref('judge')

// 显示题型选择器
const showTypeSelector = () => {
  typeSelectorDialogVisible.value = true
}

// 开始题型练习
const startTypePractice = () => {
  if (!selectedQuestionType.value) {
    ElMessage.warning('请选择一个题型')
    return
  }
  router.push({
    path: '/practice',
    query: {
      bankId: bankId.value,
      mode: 'type',
      type: selectedQuestionType.value
    }
  })
  typeSelectorDialogVisible.value = false
}

// 查看易错题
const viewDifficultQuestions = () => {
  router.push({
    path: '/practice',
    query: {
      bankId: bankId.value,
      mode: 'difficult'
    }
  })
}

// 开始模拟考试
const startExam = async () => {
  try {
    const currentBankId = bankId.value
    
    // 详细验证
    console.log('当前 bankId:', currentBankId, '类型:', typeof currentBankId)
    console.log('route.params:', route.params)
    console.log('route.query:', route.query)
    
    if (!currentBankId || isNaN(currentBankId) || currentBankId <= 0) {
      ElMessage.error('题库ID无效，请重新进入题库页面')
      console.error('无效的 bankId:', currentBankId)
      return
    }

    // 确保传递的是数字类型
    const bankIdToSend = Number(currentBankId)
    if (isNaN(bankIdToSend)) {
      ElMessage.error('题库ID格式错误')
      return
    }

    console.log('发送的 bankId:', bankIdToSend)
    
    // 创建考试
    const response = await createExam({ bankId: bankIdToSend })
    if (response.code === 200 && response.data) {
      // 跳转到考试页面
      router.push({
        path: `/exam/${response.data.id}`,
        query: { bankId: currentBankId }
      })
    }
  } catch (error: any) {
    ElMessage.error(error.message || '创建考试失败')
  }
}

// 页面加载时获取统计数据
onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.bank-detail-view {
  max-width: 1200px;
  margin: 0 auto;
}

.page-title {
  font-size: 24px;
  color: #303133;
  margin-bottom: 24px;
}

.detail-tabs {
  background: #fff;
  padding: 24px;
  border-radius: 8px;
}

.practice-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 24px;
  margin-top: 24px;
}

.practice-item {
  cursor: pointer;
  transition: transform 0.2s;
}

.practice-item:hover {
  transform: translateY(-4px);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
}
</style>


