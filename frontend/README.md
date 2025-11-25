# AI Trainer Exam Center - Frontend

> 上海智学无界教育科技有限公司 - 在线题库系统前端

## 技术栈

- **Vue 3**: Composition API + `<script setup>`
- **TypeScript**: 类型安全
- **Vite**: 构建工具
- **Vue Router**: 路由管理
- **Pinia**: 状态管理
- **Element Plus**: UI 组件库
- **Axios**: HTTP 请求

## 项目结构

```
frontend/
├── src/
│   ├── main.ts                         # 应用入口
│   ├── App.vue                         # 根组件
│   ├── api/                            # API 接口
│   │   ├── request.ts                 # Axios 封装
│   │   ├── auth.ts                    # 认证接口
│   │   ├── bank.ts                    # 题库接口
│   │   ├── question.ts                # 题目接口
│   │   ├── practice.ts                # 练习记录接口
│   │   └── exam.ts                    # 考试接口
│   ├── assets/                         # 静态资源
│   │   ├── images/                    # 图片
│   │   └── styles/                    # 全局样式
│   ├── components/                     # 通用组件
│   │   ├── QuestionCard.vue           # 题目卡片
│   │   ├── AnswerCard.vue             # 答题卡
│   │   └── ...
│   ├── layouts/                        # 布局组件
│   │   └── MainLayout.vue             # 主布局（含顶部导航）
│   ├── views/                          # 页面组件
│   │   ├── auth/                      # 认证页面
│   │   │   ├── LoginView.vue         # 登录页
│   │   │   └── RegisterView.vue      # 注册页
│   │   ├── home/                      # 首页
│   │   │   └── HomeView.vue          # 题库列表页
│   │   ├── bank/                      # 题库相关
│   │   │   └── BankDetailView.vue    # 题库详情页
│   │   ├── practice/                  # 练习相关
│   │   │   ├── MyPracticeView.vue    # 我的练习
│   │   │   ├── SpecialPracticeView.vue # 专项练习
│   │   │   └── PracticeView.vue      # 做题页面
│   │   └── exam/                      # 考试相关
│   │       └── ExamView.vue          # 考试页面
│   ├── router/                         # 路由配置
│   │   └── index.ts
│   ├── store/                          # 状态管理
│   │   ├── user.ts                    # 用户状态
│   │   └── practice.ts                # 练习状态
│   ├── types/                          # TypeScript 类型定义
│   │   ├── user.ts                    # 用户类型
│   │   ├── bank.ts                    # 题库类型
│   │   ├── question.ts                # 题目类型
│   │   ├── practice.ts                # 练习类型
│   │   └── exam.ts                    # 考试类型
│   └── utils/                          # 工具函数
│       ├── storage.ts                 # 本地存储
│       └── format.ts                  # 格式化工具
├── index.html                          # HTML 模板
├── package.json                        # 依赖配置
├── tsconfig.json                       # TypeScript 配置
├── vite.config.ts                      # Vite 配置
└── README.md                           # 项目说明
```

## 页面路由

| 路由 | 组件 | 说明 |
|------|------|------|
| `/login` | LoginView | 登录页 |
| `/register` | RegisterView | 注册页 |
| `/` | HomeView | 首页（题库列表） |
| `/banks/:id` | BankDetailView | 题库详情页 |
| `/banks/:id/my-practice` | MyPracticeView | 我的练习（错题/收藏/笔记） |
| `/banks/:id/practice` | SpecialPracticeView | 专项练习选择 |
| `/practice` | PracticeView | 做题页面 |
| `/exam/:examId` | ExamView | 模拟考试页面 |

## 核心功能

### 1. 用户认证
- 登录/注册
- JWT Token 管理（localStorage）
- 路由守卫（自动跳转到登录页）

### 2. 题库管理
- 展示所有可用题库
- 题库统计信息（题目总数、完成进度、正确率）

### 3. 我的练习
- **我的错题**: 展示历史错题
- **我的收藏**: 展示收藏的题目
- **我的笔记**: 展示笔记列表

### 4. 专项练习
- **顺序练习**: 按题目顺序练习
- **随机练习**: 随机抽取题目
- **题型练习**: 按题型筛选（单选/多选/判断）
- **模拟考试**: 完整模拟考试流程
- **易错题**: 平台易错题统计

### 5. 做题页面
- **答题模式**: 选完立即判断对错（绿/红），显示答案和解析
- **背题模式**: 直接显示答案和解析
- 答题卡：展示所有题目，支持快速跳转
- 收藏题目
- 记笔记

## 快速开始

### 1. 安装依赖
```bash
npm install
```

### 2. 启动开发服务器
```bash
npm run dev
```

访问：http://localhost:3000

### 3. 构建生产版本
```bash
npm run build
```

### 4. 预览生产版本
```bash
npm run preview
```

## 配置说明

### API 代理
在 `vite.config.ts` 中配置了 API 代理：
```typescript
proxy: {
  '/api': {
    target: 'http://localhost:8080',  // 后端地址
    changeOrigin: true
  }
}
```

### 路径别名
使用 `@` 作为 `src` 目录的别名：
```typescript
import Component from '@/components/Component.vue'
```

## 待实现功能

当前仅创建了项目骨架，以下功能待实现：

- [ ] 完善所有页面的数据获取逻辑
- [ ] 实现答题逻辑（答题模式 / 背题模式）
- [ ] 实现答题卡组件
- [ ] 实现题目收藏功能
- [ ] 实现笔记功能
- [ ] 实现模拟考试完整流程
- [ ] 实现错题统计
- [ ] 优化移动端适配
- [ ] 添加加载动画和骨架屏
- [ ] 实现题目搜索功能
- [ ] 添加题目难度筛选

## Element Plus 按需引入

当前使用全量引入，如需按需引入可参考 [Element Plus 文档](https://element-plus.org/zh-CN/guide/quickstart.html#%E6%8C%89%E9%9C%80%E5%AF%BC%E5%85%A5)。

## 许可证

Copyright © 2025 上海智学无界教育科技有限公司


