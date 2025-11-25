<template>
  <div class="notes-view">
    <div class="notes-header">
      <h2>我的笔记</h2>
      <el-button @click="goBack">返回</el-button>
    </div>

    <el-empty v-if="!loading && notes.length === 0" description="暂无笔记" />

    <div v-loading="loading" class="notes-list">
      <el-card v-for="note in notes" :key="note.id" class="note-card" shadow="hover">
        <!-- 题目信息 -->
        <div class="question-header">
          <el-tag :type="getQuestionTypeColor(note.type)">
            {{ getQuestionTypeName(note.type) }}
          </el-tag>
          <span class="question-id">题目 #{{ note.id }}</span>
        </div>

        <!-- 题目内容 -->
        <div class="question-content">
          <h3>{{ note.question }}</h3>
        </div>

        <!-- 选项 -->
        <div v-if="note.type !== 'judge'" class="question-options">
          <div v-if="note.optionA" class="option">A. {{ note.optionA }}</div>
          <div v-if="note.optionB" class="option">B. {{ note.optionB }}</div>
          <div v-if="note.optionC" class="option">C. {{ note.optionC }}</div>
          <div v-if="note.optionD" class="option">D. {{ note.optionD }}</div>
          <div v-if="note.optionE" class="option">E. {{ note.optionE }}</div>
        </div>

        <el-divider />

        <!-- 答案 -->
        <div class="question-answer">
          <strong>答案：</strong>
          <el-tag type="success">{{ note.answer }}</el-tag>
        </div>

        <!-- 解析 -->
        <div class="question-explanation">
          <strong>解析：</strong>
          <div v-html="formatExplanation(note.explanation)"></div>
        </div>

        <el-divider />

        <!-- 笔记内容 -->
        <div class="note-content">
          <div class="note-header-row">
            <strong><el-icon><EditPen /></el-icon> 我的笔记：</strong>
            <el-button link type="primary" size="small" @click="editNote(note)">
              编辑
            </el-button>
          </div>
          <div class="note-text">{{ note.noteContent }}</div>
        </div>

        <!-- 操作按钮 -->
        <div class="note-actions">
          <el-button size="small" @click="practiceThisQuestion(note)">
            练习此题
          </el-button>
        </div>
      </el-card>
    </div>

    <!-- 编辑笔记对话框 -->
    <el-dialog v-model="showEditDialog" title="编辑笔记" width="500px">
      <el-input
        v-model="editingContent"
        type="textarea"
        :rows="6"
        placeholder="请输入笔记内容"
      />
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getNotes, addNote } from '@/api/practice'
import type { Question } from '@/types/question'
import { ElMessage } from 'element-plus'
import { EditPen } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const notes = ref<Question[]>([])
const showEditDialog = ref(false)
const editingContent = ref('')
const editingNote = ref<Question | null>(null)

// 获取题库ID
const bankId = Number(route.query.bankId)

// 加载笔记列表
const loadNotes = async () => {
  loading.value = true
  try {
    const response = await getNotes(bankId)
    if (response.code === 200 && response.data) {
      notes.value = response.data
    }
  } catch (error: any) {
    ElMessage.error(error.message || '加载笔记失败')
  } finally {
    loading.value = false
  }
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

// 格式化解析
const formatExplanation = (text: string) => {
  if (!text) return ''
  return text.replace(/\n/g, '<br>')
}

// 编辑笔记
const editNote = (note: Question) => {
  editingNote.value = note
  editingContent.value = note.noteContent || ''
  showEditDialog.value = true
}

// 保存编辑
const saveEdit = async () => {
  if (!editingNote.value) return

  try {
    await addNote({
      questionId: editingNote.value.id,
      content: editingContent.value
    })
    ElMessage.success('笔记已更新')
    showEditDialog.value = false
    // 更新列表中的笔记内容
    const index = notes.value.findIndex(n => n.id === editingNote.value!.id)
    if (index !== -1) {
      notes.value[index].noteContent = editingContent.value
    }
  } catch (error: any) {
    ElMessage.error(error.message || '保存失败')
  }
}

// 练习此题
const practiceThisQuestion = (note: Question) => {
  router.push({
    path: '/practice',
    query: {
      bankId: bankId,
      questionId: note.id
    }
  })
}

// 返回
const goBack = () => {
  router.push({
    path: `/banks/${bankId}`
  })
}

onMounted(() => {
  loadNotes()
})
</script>

<style scoped>
.notes-view {
  max-width: 1000px;
  margin: 0 auto;
  padding: 24px;
}

.notes-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.notes-header h2 {
  margin: 0;
  font-size: 24px;
  color: #303133;
}

.notes-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.note-card {
  transition: all 0.3s;
}

.note-card:hover {
  transform: translateY(-2px);
}

.question-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.question-id {
  color: #909399;
  font-size: 14px;
}

.question-content h3 {
  margin: 0 0 16px 0;
  font-size: 16px;
  line-height: 1.6;
  color: #303133;
}

.question-options {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-top: 16px;
}

.option {
  padding: 8px 12px;
  background-color: #f5f7fa;
  border-radius: 4px;
  font-size: 14px;
  color: #606266;
}

.question-answer {
  margin: 16px 0;
  font-size: 14px;
  color: #606266;
}

.question-answer strong {
  margin-right: 8px;
}

.question-explanation {
  margin: 16px 0;
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
}

.question-explanation strong {
  display: block;
  margin-bottom: 8px;
}

.note-content {
  background-color: #fff9e6;
  border-left: 4px solid #e6a23c;
  padding: 16px;
  border-radius: 4px;
  margin: 16px 0;
}

.note-header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.note-header-row strong {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #e6a23c;
  font-size: 15px;
}

.note-text {
  color: #606266;
  line-height: 1.6;
  white-space: pre-wrap;
}

.note-actions {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>

