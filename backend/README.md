# AI Trainer Exam Center - Backend

> 上海智学无界教育科技有限公司 - 在线题库系统后端

## 技术栈

- **Java**: 17
- **Spring Boot**: 3.2.0
- **Spring Security + JWT**: 认证授权
- **MyBatis Plus**: 3.5.5（ORM 框架）
- **MySQL**: 8.0+
- **Redis**: 已禁用（可选，如需使用请安装 Redis 并修改配置）
- **Knife4j**: API 文档（Swagger 3.0）
- **Lombok**: 简化代码
- **Hutool**: 工具类库

## 项目结构

```
backend/
├── src/main/java/com/zhixuewujie/examcenter/
│   ├── ExamCenterApplication.java          # 主启动类
│   ├── controller/                         # 控制器层
│   │   ├── AuthController.java            # 认证接口（登录/注册）
│   │   ├── QuestionBankController.java    # 题库管理接口
│   │   ├── QuestionController.java        # 题目查询接口
│   │   ├── ExamController.java            # 模拟考试接口
│   │   └── UserPracticeController.java    # 用户练习记录接口
│   ├── service/                            # 业务逻辑层
│   │   ├── IAuthService.java              # 认证服务
│   │   ├── IQuestionBankService.java      # 题库服务
│   │   ├── IQuestionService.java          # 题目服务
│   │   ├── IExamService.java              # 考试服务
│   │   └── IUserPracticeService.java      # 用户练习服务
│   ├── mapper/                             # 数据访问层（MyBatis）
│   │   ├── UserMapper.java
│   │   ├── QuestionBankMapper.java
│   │   ├── QuestionMapper.java
│   │   ├── UserAnswerMapper.java
│   │   ├── UserFavoriteMapper.java
│   │   ├── UserNoteMapper.java
│   │   └── ExamMapper.java
│   ├── entity/                             # 实体类（对应数据库表）
│   │   ├── User.java                      # 用户
│   │   ├── QuestionBank.java              # 题库
│   │   ├── Question.java                  # 题目
│   │   ├── UserAnswer.java                # 答题记录
│   │   ├── UserFavorite.java              # 收藏记录
│   │   ├── UserNote.java                  # 笔记记录
│   │   └── Exam.java                      # 模拟考试
│   ├── dto/                                # 数据传输对象（请求参数）
│   │   ├── LoginDTO.java                  # 登录请求
│   │   ├── RegisterDTO.java               # 注册请求
│   │   ├── SubmitAnswerDTO.java           # 提交答案请求
│   │   └── CreateExamDTO.java             # 创建考试请求
│   ├── vo/                                 # 视图对象（响应数据）
│   │   ├── UserVO.java                    # 用户信息
│   │   ├── QuestionBankVO.java            # 题库信息
│   │   ├── QuestionVO.java                # 题目信息
│   │   └── ExamResultVO.java              # 考试结果
│   ├── config/                             # 配置类
│   │   ├── SecurityConfig.java            # Spring Security 配置
│   │   ├── RedisConfig.java               # Redis 配置
│   │   ├── MyBatisPlusConfig.java         # MyBatis Plus 配置
│   │   └── Knife4jConfig.java             # API 文档配置
│   ├── security/                           # 安全相关
│   │   ├── JwtTokenProvider.java          # JWT 工具类
│   │   ├── JwtAuthenticationFilter.java   # JWT 过滤器
│   │   └── UserDetailsServiceImpl.java    # 用户详情服务
│   ├── common/                             # 公共类
│   │   ├── Result.java                    # 统一响应结果
│   │   ├── PageResult.java                # 分页结果
│   │   └── Constants.java                 # 常量定义
│   ├── exception/                          # 异常处理
│   │   ├── GlobalExceptionHandler.java    # 全局异常处理器
│   │   ├── BusinessException.java         # 业务异常
│   │   └── ErrorCode.java                 # 错误码枚举
│   └── util/                               # 工具类
│       ├── JwtUtil.java                   # JWT 工具
│       ├── PasswordUtil.java              # 密码加密工具
│       └── RedisUtil.java                 # Redis 工具
├── src/main/resources/
│   ├── application.yml                     # 主配置文件
│   ├── application-dev.yml                 # 开发环境配置
│   ├── application-prod.yml                # 生产环境配置
│   ├── mapper/                             # MyBatis XML 文件
│   └── sql/
│       └── schema.sql                      # 数据库建表脚本
├── pom.xml                                 # Maven 依赖配置
└── README.md                               # 项目说明文档
```

## 核心功能模块

### 1. 认证模块（AuthController）
- `POST /api/auth/login` - 用户登录，返回 JWT Token
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/refresh` - 刷新 Token
- `POST /api/auth/logout` - 退出登录（清除 Redis 中的 Token）

### 2. 题库模块（QuestionBankController）
- `GET /api/question-banks` - 获取我的题库列表
- `GET /api/question-banks/{id}` - 获取题库详情
- `GET /api/question-banks/{id}/statistics` - 获取题库统计信息

### 3. 题目模块（QuestionController）
- `GET /api/questions/sequence` - 顺序练习
- `GET /api/questions/random` - 随机练习
- `GET /api/questions/by-type` - 按题型练习（单选/多选/判断）
- `GET /api/questions/{id}` - 获取题目详情

### 4. 用户练习模块（UserPracticeController）
- `POST /api/user-practice/submit` - 提交答案（记录对错）
- `GET /api/user-practice/wrong-questions` - 我的错题
- `POST /api/user-practice/favorites/{questionId}` - 收藏题目
- `GET /api/user-practice/favorites` - 我的收藏
- `POST /api/user-practice/notes` - 添加笔记
- `GET /api/user-practice/notes` - 我的笔记
- `GET /api/user-practice/difficult-questions` - 易错题（全局统计）

### 5. 模拟考试模块（ExamController）
- `POST /api/exams` - 创建模拟考试（随机抽题）
- `GET /api/exams/{examId}` - 获取考试详情
- `POST /api/exams/{examId}/submit` - 提交考试
- `GET /api/exams/records` - 获取考试记录

## 数据库设计

### 核心表结构

| 表名 | 说明 |
|------|------|
| `tb_user` | 用户表 |
| `tb_question_bank` | 题库表（支持多题库扩展） |
| `tb_question` | 题目表 |
| `tb_user_answer` | 用户答题记录 |
| `tb_user_favorite` | 用户收藏 |
| `tb_user_note` | 用户笔记 |
| `tb_exam` | 模拟考试记录 |

详见：`src/main/resources/sql/schema.sql`

## 快速开始

### 1. 环境准备
- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- ~~Redis 6.0+~~（已禁用，不需要）

### 2. 数据库初始化
```bash
# 创建数据库并导入表结构
mysql -u root -p < src/main/resources/sql/schema.sql
```

### 3. 修改配置
编辑 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/exam_center
    username: your_username
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
```

### 4. 启动项目
```bash
mvn spring-boot:run
```

### 5. 访问 API 文档
启动成功后访问：
- Knife4j 文档：http://localhost:8080/doc.html
- Swagger UI：http://localhost:8080/swagger-ui/index.html

## 待实现功能

当前仅创建了项目骨架，以下功能待实现：

- [ ] 实现 JWT 认证逻辑（JwtTokenProvider、JwtAuthenticationFilter）
- [ ] 实现 Service 层业务逻辑
- [ ] 实现 Mapper 层数据访问（MyBatis XML）
- [ ] 完善 DTO/VO 定义
- [ ] 实现全局异常处理
- [ ] 实现 Redis 缓存策略（题目、用户会话）
- [ ] 编写单元测试
- [ ] 配置 CORS 跨域
- [ ] 配置日志（Logback）

## 扩展性设计

### 多题库支持
当前系统已设计为多题库架构：
- 题库通过 `bank_id` 关联
- 所有练习记录、收藏、笔记都关联 `bank_id`
- 未来只需在 `tb_question_bank` 表新增记录即可扩展新题库

### 题目类型扩展
当前支持：
- `SINGLE_CHOICE` - 单选题
- `MULTIPLE_CHOICE` - 多选题
- `TRUE_FALSE` - 判断题

未来可扩展：
- `FILL_BLANK` - 填空题
- `SHORT_ANSWER` - 简答题
- `ESSAY` - 论述题

## 许可证

Copyright © 2025 上海智学无界教育科技有限公司


