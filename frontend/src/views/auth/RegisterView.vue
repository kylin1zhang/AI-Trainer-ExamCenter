<template>
  <div class="register-container">
    <div class="register-card">
      <div class="register-header">
        <h1>用户注册</h1>
        <p>AI训练师考试中心</p>
      </div>

      <!-- TODO: 实现注册表单 -->
      <el-form class="register-form">
        <el-form-item>
          <el-input placeholder="用户名" size="large" prefix-icon="User" />
        </el-form-item>
        <el-form-item>
          <el-input type="password" placeholder="密码" size="large" prefix-icon="Lock" />
        </el-form-item>
        <el-form-item>
          <el-input type="password" placeholder="确认密码" size="large" prefix-icon="Lock" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" class="register-btn">注 册</el-button>
        </el-form-item>
        <div class="register-footer">
          <router-link to="/login">已有账号？立即登录</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
// TODO: 实现注册逻辑
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-card {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.register-header {
  text-align: center;
  margin-bottom: 32px;
}

.register-header h1 {
  font-size: 24px;
  color: #303133;
  margin-bottom: 8px;
}

.register-header p {
  font-size: 14px;
  color: #909399;
}

.register-btn {
  width: 100%;
}

.register-footer {
  text-align: center;
  margin-top: 16px;
}

.register-footer a {
  color: #409eff;
  text-decoration: none;
}
</style>


