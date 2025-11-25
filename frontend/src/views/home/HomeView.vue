<template>
  <div class="home-view">
    <h2 class="page-title">我的题库</h2>
    
    <div class="bank-list" v-loading="loading">
      <el-card 
        v-for="bank in banks" 
        :key="bank.id" 
        class="bank-card" 
        shadow="hover"
        @click="gotoBankDetail(bank.id)"
      >
        <div class="bank-cover">
          <img :src="bank.coverImage || 'https://via.placeholder.com/300x200'" :alt="bank.name" />
        </div>
        <div class="bank-info">
          <h3>{{ bank.name }}</h3>
          <p class="bank-desc">{{ bank.description }}</p>
          <div class="bank-stats">
            <el-tag>题目总数: {{ bank.questionCount }}</el-tag>
          </div>
          <el-button type="primary" @click.stop="gotoBankDetail(bank.id)">开始练习</el-button>
        </div>
      </el-card>

      <el-empty v-if="!loading && banks.length === 0" description="暂无题库" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getQuestionBanks } from '@/api/bank'
import type { QuestionBank } from '@/types/bank'
import { ElMessage } from 'element-plus'

const router = useRouter()
const banks = ref<QuestionBank[]>([])
const loading = ref(false)

const loadBanks = async () => {
  loading.value = true
  try {
    const response = await getQuestionBanks()
    if (response.code === 200 && response.data) {
      banks.value = response.data
    }
  } catch (error: any) {
    ElMessage.error(error.message || '加载题库失败')
  } finally {
    loading.value = false
  }
}

const gotoBankDetail = (id: number) => {
  router.push(`/banks/${id}`)
}

onMounted(() => {
  loadBanks()
})
</script>

<style scoped>
.home-view {
  max-width: 1200px;
  margin: 0 auto;
}

.page-title {
  font-size: 24px;
  color: #303133;
  margin-bottom: 24px;
}

.bank-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

.bank-card {
  cursor: pointer;
  transition: transform 0.2s;
}

.bank-card:hover {
  transform: translateY(-4px);
}

.bank-cover img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 4px;
}

.bank-info {
  padding: 16px 0;
}

.bank-info h3 {
  font-size: 18px;
  color: #303133;
  margin-bottom: 8px;
}

.bank-desc {
  font-size: 14px;
  color: #909399;
  margin-bottom: 16px;
}

.bank-stats {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}
</style>

