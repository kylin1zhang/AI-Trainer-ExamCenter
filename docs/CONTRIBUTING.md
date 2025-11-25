# 贡献指南

> 感谢您对 AI Trainer Exam Center 项目的关注！

## 如何贡献

我们欢迎任何形式的贡献，包括但不限于：

- 🐛 报告 Bug
- 💡 提出新功能建议
- 📝 改进文档
- 💻 提交代码
- 🧪 编写测试
- 🌍 翻译文档

---

## 开发流程

### 1. Fork 项目

点击项目右上角的 "Fork" 按钮，将项目 Fork 到您的账号下。

### 2. 克隆项目

```bash
git clone https://github.com/your-username/AI-Trainer-ExamCenter.git
cd AI-Trainer-ExamCenter
```

### 3. 创建分支

```bash
# 创建功能分支
git checkout -b feature/your-feature-name

# 或创建修复分支
git checkout -b fix/your-bug-fix
```

### 4. 进行开发

按照项目规范编写代码，并确保：
- ✅ 代码符合项目编码规范
- ✅ 添加必要的注释
- ✅ 编写单元测试（如适用）
- ✅ 测试通过

### 5. 提交代码

```bash
git add .
git commit -m "feat: 添加新功能描述"
```

**提交信息规范（Commit Message）：**

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Type 类型：**
- `feat`: 新功能
- `fix`: 修复 Bug
- `docs`: 文档修改
- `style`: 代码格式调整（不影响功能）
- `refactor`: 代码重构
- `test`: 添加或修改测试
- `chore`: 构建过程或辅助工具的变动

**示例：**

```bash
git commit -m "feat(backend): 添加用户登录接口"
git commit -m "fix(frontend): 修复答题卡显示错误"
git commit -m "docs: 更新 README 文档"
```

### 6. 推送到远程

```bash
git push origin feature/your-feature-name
```

### 7. 创建 Pull Request

1. 在 GitHub 上访问您 Fork 的项目
2. 点击 "New Pull Request"
3. 填写 PR 描述
4. 等待代码审查

---

## 编码规范

### 后端（Java）

**代码风格：**
- 遵循 [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- 使用 4 个空格缩进
- 每行代码不超过 120 个字符

**命名规范：**
- 类名：大驼峰（PascalCase），如 `UserController`
- 方法名：小驼峰（camelCase），如 `getUserInfo`
- 常量：全大写+下划线，如 `MAX_RETRY_COUNT`

**注释规范：**

```java
/**
 * 用户登录
 * 
 * @param username 用户名
 * @param password 密码
 * @return JWT Token
 */
public String login(String username, String password) {
    // 实现逻辑
}
```

### 前端（Vue 3 + TypeScript）

**代码风格：**
- 遵循 [Vue.js Style Guide](https://vuejs.org/style-guide/)
- 使用 2 个空格缩进
- 使用单引号

**命名规范：**
- 组件名：大驼峰，如 `LoginView.vue`
- 文件名：小驼峰或中划线，如 `user-profile.ts`
- 变量/函数：小驼峰，如 `getUserInfo`

**组件结构：**

```vue
<template>
  <!-- HTML 模板 -->
</template>

<script setup lang="ts">
// TypeScript 逻辑
</script>

<style scoped>
/* CSS 样式 */
</style>
```

---

## 分支管理

- `main`: 主分支，保持稳定
- `develop`: 开发分支
- `feature/*`: 功能分支
- `fix/*`: 修复分支
- `hotfix/*`: 紧急修复分支

---

## 测试规范

### 后端测试

```java
@SpringBootTest
class UserServiceTest {
    
    @Autowired
    private UserService userService;
    
    @Test
    void testLogin() {
        // Given
        String username = "test";
        String password = "123456";
        
        // When
        User user = userService.login(username, password);
        
        // Then
        assertNotNull(user);
        assertEquals(username, user.getUsername());
    }
}
```

### 前端测试

```typescript
import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import LoginView from '@/views/auth/LoginView.vue'

describe('LoginView', () => {
  it('renders properly', () => {
    const wrapper = mount(LoginView)
    expect(wrapper.find('h1').text()).toBe('登录')
  })
})
```

---

## 代码审查

所有代码提交都需要经过代码审查（Code Review）：

**审查要点：**
- ✅ 代码功能是否符合需求
- ✅ 代码质量和可读性
- ✅ 是否有潜在的 Bug
- ✅ 是否符合编码规范
- ✅ 测试覆盖率
- ✅ 性能影响

**审查流程：**
1. 提交 Pull Request
2. CI 自动检查（Lint、Test）
3. 至少一位维护者审查
4. 根据反馈修改代码
5. 审查通过后合并

---

## 问题反馈

### 提交 Issue

如果您发现 Bug 或有新功能建议，请：

1. 在 GitHub Issues 中搜索是否已有类似问题
2. 如果没有，创建新的 Issue
3. 使用清晰的标题和详细的描述
4. 提供复现步骤（如果是 Bug）

**Bug 报告模板：**

```markdown
## Bug 描述
简要描述 Bug

## 复现步骤
1. 进入登录页
2. 输入用户名和密码
3. 点击登录按钮
4. 出现错误

## 期望行为
应该成功登录并跳转到首页

## 实际行为
页面报错

## 环境信息
- 操作系统：Windows 11
- 浏览器：Chrome 120
- 项目版本：1.0.0
```

**功能建议模板：**

```markdown
## 功能描述
建议添加题目搜索功能

## 使用场景
用户想快速找到特定的题目

## 期望效果
在题库页面添加搜索框，支持按题目内容搜索
```

---

## 开发环境

### 推荐的 IDE

**后端开发：**
- IntelliJ IDEA（推荐）
- Eclipse

**前端开发：**
- Visual Studio Code（推荐）
- WebStorm

### 推荐的插件

**VS Code 插件：**
- Vue - Official
- ESLint
- Prettier
- Auto Rename Tag
- Path Intellisense

**IDEA 插件：**
- Lombok
- MyBatisX
- Spring Assistant

---

## 文档贡献

文档同样重要！如果您发现文档有误或可以改进，请：

1. 修改相应的 Markdown 文件
2. 提交 Pull Request
3. 在 PR 描述中说明修改内容

---

## 社区准则

- 🤝 尊重他人，友善交流
- 💬 使用清晰、专业的语言
- 📖 遵循项目的编码规范和流程
- 🎯 专注于解决问题，而非争论
- 🙏 感谢每一位贡献者

---

## 获得帮助

如果您在贡献过程中遇到问题：

- 📧 发送邮件：dev@zhixuewujie.com
- 💬 在 Issue 中提问
- 📖 查看项目文档

---

## 贡献者名单

感谢所有为本项目做出贡献的开发者！

<!-- 这里可以添加贡献者列表 -->

---

再次感谢您的贡献！🎉

---

Copyright © 2025 上海智学无界教育科技有限公司


