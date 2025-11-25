# AI Trainer ExamCenter - 人工智能训练师题库系统

> 上海智学无界教育科技有限公司

## 项目简介

这是一个专为**人工智能训练师（三级/高级）**考试设计的在线题库练习系统，采用前后端分离架构。系统包含完整的题库管理、练习模式、错题收藏、笔记功能和模拟考试等功能。

- **后端**：Java 17 + Spring Boot 3.2 + MyBatis Plus
- **前端**：Vue 3 + TypeScript + Element Plus + Vite
- **数据库**：MySQL 8.0
- **题库容量**：900+ 题目（判断题 + 单选题 + 多选题）

---

## 🚀 快速开始（3步启动）

### 📋 前置要求

| 软件 | 版本要求 | 说明 |
|------|---------|------|
| **Java** | 17+ | 后端运行环境 |
| **Node.js** | 16+ | 前端运行环境 |
| **MySQL** | 8.0+ | 数据库 |
| **Maven** | 3.6+ | 后端依赖管理 |

### 第一步：创建数据库并导入数据

```bash
# 1. 登录 MySQL
mysql -u root -p

# 2. 创建数据库
CREATE DATABASE exam_center CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 3. 退出 MySQL
exit;

# 4. 导入数据库表结构
mysql -u root -p --default-character-set=utf8mb4 exam_center < backend/src/main/resources/sql/schema.sql

# 5. 导入测试用户（密码：123456）
mysql -u root -p --default-character-set=utf8mb4 exam_center < backend/src/main/resources/sql/init_user.sql

# 6. 导入题库数据（可选，使用Python脚本）
cd scripts
python import_questions_simple.py
```

**重要提示：**
- 如果您的MySQL密码不是 `Asdf1234`，请修改 `backend/src/main/resources/application.yml` 中的数据库密码
- 导入题库数据需要先安装Python依赖：`pip install mysql-connector-python`
- 确保 `data.json` 文件在项目根目录

### 第二步：启动后端

```bash
# 进入后端目录
cd backend

# Windows PowerShell（修改为您的Java 17路径）:
$env:JAVA_HOME="E:\Java\jdk-17.0.17"
mvn spring-boot:run

# Linux/Mac:
export JAVA_HOME=/path/to/jdk-17
mvn spring-boot:run
```

**后端启动成功标志：**
- ✅ 控制台显示：`Started ExamCenterApplication in X seconds`
- ✅ 访问 http://localhost:8080 无报错
- ✅ Swagger文档：http://localhost:8080/doc.html

### 第三步：启动前端

```bash
# 进入前端目录（新开一个终端）
cd frontend

# 安装依赖（首次运行）
npm install

# 启动开发服务器
npm run dev
```

**前端启动成功标志：**
- ✅ 控制台显示：`Local: http://localhost:5173/`
- ✅ 浏览器自动打开登录页面

### 🎉 开始使用

1. 打开浏览器访问：**http://localhost:5173**
2. 使用测试账号登录：
   - 用户名：`test` 或 `admin`
   - 密码：`123456`
3. 开始练习！

---

## 📦 测试账号

系统提供以下测试账号（密码均为 `123456`）：

| 用户名 | 密码 | 昵称 | 说明 |
|--------|------|------|------|
| `admin` | `123456` | 管理员 | 管理员账号 |
| `test` | `123456` | 测试用户 | 普通测试账号 |

**注意：** 
- 这些账号通过 `init_user.sql` 导入
- 密码使用 BCrypt 加密存储
- 生产环境请务必修改默认密码

---

## ✨ 功能特性

### 1. 用户系统
- ✅ 用户登录（账号 + 密码）
- ✅ JWT Token 认证维持会话
- ✅ 前端路由登录拦截
- ✅ 用户注册功能

### 2. 题库管理
- ✅ 题库列表展示
- ✅ 当前题库：人工智能训练师（三级/高级）
- ✅ 显示题库名称、题目总数、最近练习时间
- ✅ 支持多题库扩展

### 3. 练习模式

#### 我的练习
- ✅ **我的错题** - 所有答错的题目，支持重新练习
- ✅ **我的收藏** - 收藏的题目列表，快速复习
- ✅ **我的笔记** - 查看带笔记的题目

#### 专项练习
- ✅ **顺序练习** - 按题目顺序依次练习（900+题）
- ✅ **随机练习** - 打乱题目顺序练习
- ✅ **题型练习** - 按题型分类（判断题/单选题/多选题）
- ✅ **模拟考试** - 完整模拟真实考试流程
- ✅ **易错题** - 错误次数 ≥ 2 次的题目

### 4. 做题页面

#### 页面布局
- **左侧主内容区**：
  - 题目内容（题干 + 选项）
  - 答案和解析（根据模式显示）
  - 操作按钮（上一题/下一题/收藏/记笔记）
- **右侧答题卡**：
  - 题号网格（按题型分类）
  - 答题状态（绿色=正确，红色=错误）
  - 统计信息（答对/答错/正确率）
  - 设置项（自动下一题/背题模式）

#### 答题模式
- ✅ **答题模式** - 选择答案后立即判断对错，显示解析
- ✅ **背题模式** - 直接显示答案和解析，快速记忆

#### 特色功能
- ✅ 答题卡右侧固定显示，支持快速跳题
- ✅ 题号区域独立滚动，统计和设置固定可见
- ✅ 实时显示答题统计（答对/答错/正确率）
- ✅ 答对自动下一题（可开关）
- ✅ 支持题目收藏和笔记记录

### 5. 笔记功能
- ✅ 做题时添加笔记
- ✅ 笔记列表页面（显示题目完整信息）
- ✅ 支持笔记编辑和查看
- ✅ 一题一笔记（自动更新）

### 6. 模拟考试

#### 考试规则（人工智能训练师三级）
- ✅ **判断题**：40 题 × 0.5 分 = 20 分
- ✅ **单选题**：140 题 × 0.5 分 = 70 分
- ✅ **多选题**：10 题 × 1 分 = 10 分
- ✅ **总分**：100 分，60 分及格
- ✅ **考试时长**：90 分钟倒计时

#### 考试流程
1. 创建考试 - 随机抽取题目
2. 开始考试 - 启动倒计时
3. 答题过程 - 使用答题卡快速跳转
4. 考试结束 - 时间到或提交试卷
5. 查看成绩 - 总分、正确率、及格状态
6. 查看解析 - 每题答案和详细解析

---

## 💻 开发环境配置

### 数据库配置

修改 `backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/exam_center?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root          # 修改为您的MySQL用户名
    password: Asdf1234      # 修改为您的MySQL密码
```

### 端口配置

**后端端口**（默认8080）：
```yaml
# backend/src/main/resources/application.yml
server:
  port: 8080  # 修改为其他端口
```

**前端端口**（默认5173）：
```typescript
// frontend/vite.config.ts
server: {
  port: 5173,  // 修改为其他端口
}
```

### API 地址配置

如果修改了后端端口，需要同步修改前端API地址：

```typescript
// frontend/src/api/request.ts
const request = axios.create({
  baseURL: 'http://localhost:8080',  // 修改为后端地址
  timeout: 10000
})
```

---

## 📁 项目结构

```
AI-Trainer-ExamCenter/
├── backend/                    # 后端项目（Spring Boot）
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/zhixuewujie/examcenter/
│   │   │   │   ├── controller/       # 控制器层
│   │   │   │   ├── service/          # 业务逻辑层
│   │   │   │   ├── mapper/           # 数据访问层
│   │   │   │   ├── entity/           # 实体类
│   │   │   │   ├── dto/              # 数据传输对象
│   │   │   │   ├── vo/               # 视图对象
│   │   │   │   ├── common/           # 公共类
│   │   │   │   ├── config/           # 配置类
│   │   │   │   └── util/             # 工具类
│   │   │   └── resources/
│   │   │       ├── mapper/           # MyBatis XML
│   │   │       ├── sql/              # SQL脚本
│   │   │       │   ├── schema.sql    # 表结构
│   │   │       │   └── init_user.sql # 测试用户
│   │   │       └── application.yml   # 配置文件
│   │   └── pom.xml                   # Maven配置
│   ├── start.bat                     # Windows启动脚本
│   └── README.md                     # 后端说明
├── frontend/                   # 前端项目（Vue 3）
│   ├── src/
│   │   ├── api/               # API接口
│   │   ├── components/        # 组件
│   │   ├── views/             # 页面
│   │   │   ├── auth/         # 登录/注册
│   │   │   ├── home/         # 首页
│   │   │   ├── bank/         # 题库详情
│   │   │   ├── practice/     # 练习/笔记
│   │   │   └── exam/         # 考试
│   │   ├── router/           # 路由配置
│   │   ├── store/            # 状态管理（Pinia）
│   │   ├── types/            # TypeScript类型
│   │   ├── layouts/          # 布局组件
│   │   └── App.vue           # 根组件
│   ├── package.json          # 依赖配置
│   ├── vite.config.ts        # Vite配置
│   └── README.md             # 前端说明
├── scripts/                  # 工具脚本
│   ├── import_questions_simple.py  # 题库导入脚本
│   └── README.md             # 脚本说明
├── data.json                 # 题库数据（900题）
└── README.md                 # 本文件
```

---

## 🔧 常见问题

### 1. 数据库连接失败

**错误信息：** `Access denied for user 'root'@'localhost'`

**解决方案：**
- 检查 `application.yml` 中的数据库用户名和密码
- 确认 MySQL 服务已启动
- 确认数据库 `exam_center` 已创建

### 2. 后端启动失败 - 端口被占用

**错误信息：** `Port 8080 was already in use`

**解决方案：**
```bash
# Windows: 查找并杀死占用端口的进程
netstat -ano | findstr :8080
taskkill /F /PID <进程ID>

# Linux/Mac:
lsof -i :8080
kill -9 <进程ID>
```

### 3. 前端页面空白或报错

**解决方案：**
- 确认后端已启动（访问 http://localhost:8080）
- 检查浏览器控制台错误信息
- 清除浏览器缓存后重试
- 检查 `frontend/src/api/request.ts` 中的 baseURL

### 4. 登录提示"用户名或密码错误"

**解决方案：**
- 确认已导入 `init_user.sql`
- 检查用户是否存在：
  ```sql
  SELECT * FROM tb_user WHERE username = 'test';
  ```
- 如果不存在，重新导入 `init_user.sql`

### 5. 题库无数据

**解决方案：**
- 确认 `data.json` 在项目根目录
- 运行导入脚本：
  ```bash
  cd scripts
  python import_questions_simple.py
  ```
- 检查题目数量：
  ```sql
  SELECT COUNT(*) FROM tb_question;
  ```

### 6. Maven编译错误 - Java版本不匹配

**解决方案：**
```bash
# 检查Java版本
java -version

# 设置JAVA_HOME（修改为您的Java 17路径）
# Windows:
$env:JAVA_HOME="E:\Java\jdk-17.0.17"

# Linux/Mac:
export JAVA_HOME=/path/to/jdk-17
```

### 7. npm install 失败

**解决方案：**
```bash
# 清除npm缓存
npm cache clean --force

# 使用淘宝镜像
npm install --registry=https://registry.npmmirror.com
```

---

## 📚 API 文档

系统集成了 Knife4j（Swagger）API 文档。

**访问地址：** http://localhost:8080/doc.html

### 主要API接口

#### 用户认证 (`/api/auth`)
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `GET /api/auth/current` - 获取当前用户

#### 题库管理 (`/api/banks`)
- `GET /api/banks` - 获取题库列表
- `GET /api/banks/{id}` - 获取题库详情

#### 题目练习 (`/api/questions`)
- `GET /api/questions/sequence` - 顺序练习（1000题）
- `GET /api/questions/random` - 随机练习（1000题）
- `GET /api/questions/by-type` - 题型练习
- `GET /api/questions/{id}` - 获取题目详情

#### 用户练习 (`/api/user-practice`)
- `POST /api/user-practice/answer` - 提交答案
- `GET /api/user-practice/wrong` - 我的错题
- `GET /api/user-practice/favorites` - 我的收藏
- `POST /api/user-practice/favorite` - 收藏题目
- `DELETE /api/user-practice/favorite/{id}` - 取消收藏
- `POST /api/user-practice/notes` - 添加笔记
- `GET /api/user-practice/notes` - 获取笔记列表

#### 模拟考试 (`/api/exams`)
- `POST /api/exams` - 创建考试
- `POST /api/exams/{id}/submit` - 提交考试
- `GET /api/exams/records` - 考试记录

---

## 🚀 生产部署

### 后端部署

```bash
# 1. 打包
cd backend
mvn clean package -DskipTests

# 2. 运行
java -jar target/examcenter-backend-1.0.0.jar

# 3. 后台运行（Linux）
nohup java -jar target/examcenter-backend-1.0.0.jar > app.log 2>&1 &
```

### 前端部署

```bash
# 1. 构建
cd frontend
npm run build

# 2. 部署dist目录到nginx或其他Web服务器
```

### Nginx 配置示例

```nginx
server {
    listen 80;
    server_name your-domain.com;

    # 前端静态文件
    location / {
        root /path/to/frontend/dist;
        try_files $uri $uri/ /index.html;
    }

    # 后端API代理
    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

---

## 🛠️ 技术栈详情

### 后端技术栈
- **Java 17** - 编程语言
- **Spring Boot 3.2** - 应用框架
- **Spring Security** - 安全框架
- **JWT** - 身份认证
- **MyBatis Plus 3.5.5** - ORM框架
- **MySQL 8.0** - 关系型数据库
- **Knife4j** - API文档（Swagger 3）
- **Hutool** - Java工具库
- **Maven** - 项目管理

### 前端技术栈
- **Vue 3** - 渐进式框架
- **TypeScript** - 类型安全
- **Vite 5.0** - 构建工具
- **Element Plus 2.5** - UI组件库
- **Vue Router 4.2** - 路由管理
- **Pinia 2.1** - 状态管理
- **Axios** - HTTP客户端

---

## 🎯 系统特点

1. **前后端分离** - 独立开发、独立部署
2. **JWT认证** - 安全的用户身份验证
3. **题库管理** - 支持多题库扩展（当前900+题）
4. **多种练习模式** - 顺序、随机、题型、错题练习
5. **答题模式切换** - 答题模式和背题模式
6. **实时统计** - 答题正确率实时显示
7. **收藏和笔记** - 支持题目收藏和笔记记录
8. **模拟考试** - 完整的考试流程和成绩统计
9. **响应式设计** - 适配不同屏幕尺寸
10. **API文档** - 集成Swagger文档

---

## 📝 开发说明

### 添加新题库

1. 在 `tb_question_bank` 表中插入新题库信息
2. 准备题目数据（JSON格式，参考 `data.json`）
3. 使用导入脚本导入题目
4. 前端会自动显示新题库

### 自定义考试规则

修改 `backend/src/main/java/com/zhixuewujie/examcenter/common/ExamConfig.java`

---

## 🤝 贡献指南

欢迎贡献代码！请遵循以下步骤：

1. Fork 本仓库
2. 创建特性分支：`git checkout -b feature/your-feature`
3. 提交更改：`git commit -am 'Add some feature'`
4. 推送到分支：`git push origin feature/your-feature`
5. 提交 Pull Request

---

## 📄 许可证

MIT License

Copyright © 2025 上海智学无界教育科技有限公司

---

## 📞 联系方式

**公司**：上海智学无界教育科技有限公司  
**项目**：人工智能训练师题库系统  
**开发者**：智学无界团队

---

## ⚠️ 免责声明

本系统仅供学习和练习使用，题库内容仅供参考。考试请以官方发布的内容为准。

---

## 📅 更新日志

### v1.0.0 (2025-11-25)

#### 核心功能
- ✅ 完成基础题库系统
- ✅ 实现用户认证（JWT）
- ✅ 实现顺序、随机、题型练习模式
- ✅ 实现错题、收藏、笔记功能
- ✅ 实现模拟考试功能

#### UI优化
- ✅ 实现答题卡右侧固定显示
- ✅ 优化答题卡布局（题号可滚动，统计和设置固定）
- ✅ 题号方块优化（40x40px，5列网格）
- ✅ 支持900+题的完整题库

#### 功能完善
- ✅ 实现答题模式和背题模式切换
- ✅ 实现笔记列表页面（完整题目信息展示）
- ✅ 答对自动下一题功能
- ✅ 实时答题统计（答对/答错/正确率）

#### 技术优化
- ✅ 禁用Redis依赖（简化部署）
- ✅ 优化答题记录存储逻辑
- ✅ 前端热更新优化

---

**祝您练习顺利，考试成功！** 🎉✨
