# API 接口文档

> AI Trainer Exam Center - 在线题库系统 API 文档

## 接口规范

### 统一响应格式

所有接口返回格式统一为：

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1700000000000
}
```

**字段说明：**
- `code`: 状态码，200 表示成功，其他表示失败
- `message`: 响应消息
- `data`: 响应数据
- `timestamp`: 时间戳

### 常用状态码

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权（Token 失效或未登录） |
| 403 | 无权限访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

### 认证方式

使用 JWT Token 认证，请求头格式：

```
Authorization: Bearer <your-token>
```

---

## 1. 认证接口

### 1.1 用户登录

**接口地址：** `POST /api/auth/login`

**请求参数：**

```json
{
  "username": "test",
  "password": "123456"
}
```

**响应数据：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "user": {
      "id": 1,
      "username": "test",
      "nickname": "测试用户",
      "email": "test@example.com",
      "avatar": "https://..."
    }
  }
}
```

### 1.2 用户注册

**接口地址：** `POST /api/auth/register`

**请求参数：**

```json
{
  "username": "newuser",
  "password": "123456",
  "confirmPassword": "123456",
  "email": "newuser@example.com"
}
```

### 1.3 刷新 Token

**接口地址：** `POST /api/auth/refresh`

**请求头：** 需要携带旧 Token

**响应数据：**

```json
{
  "code": 200,
  "data": {
    "token": "new-token-here"
  }
}
```

### 1.4 退出登录

**接口地址：** `POST /api/auth/logout`

**请求头：** 需要携带 Token

---

## 2. 题库接口

### 2.1 获取题库列表

**接口地址：** `GET /api/question-banks`

**响应数据：**

```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "name": "人工智能训练师（三级/高级）",
      "description": "人工智能训练师职业技能等级认证题库",
      "coverImage": "https://...",
      "questionCount": 1000,
      "sortOrder": 1,
      "createTime": "2025-01-01 00:00:00"
    }
  ]
}
```

### 2.2 获取题库详情

**接口地址：** `GET /api/question-banks/{id}`

**路径参数：** `id` - 题库ID

### 2.3 获取题库统计

**接口地址：** `GET /api/question-banks/{id}/statistics`

**响应数据：**

```json
{
  "code": 200,
  "data": {
    "bankId": 1,
    "totalQuestions": 1000,
    "practiceCount": 250,
    "correctCount": 200,
    "wrongCount": 50,
    "accuracy": 0.80,
    "favoriteCount": 32,
    "noteCount": 28
  }
}
```

---

## 3. 题目接口

### 3.1 顺序练习

**接口地址：** `GET /api/questions/sequence`

**请求参数：**
- `bankId` - 题库ID（必填）
- `page` - 页码（默认 1）
- `size` - 每页数量（默认 20）

### 3.2 随机练习

**接口地址：** `GET /api/questions/random`

**请求参数：**
- `bankId` - 题库ID（必填）
- `count` - 题目数量（默认 20）

### 3.3 题型练习

**接口地址：** `GET /api/questions/by-type`

**请求参数：**
- `bankId` - 题库ID（必填）
- `type` - 题型（SINGLE_CHOICE/MULTIPLE_CHOICE/TRUE_FALSE）
- `page` - 页码
- `size` - 每页数量

### 3.4 获取题目详情

**接口地址：** `GET /api/questions/{id}`

**响应数据：**

```json
{
  "code": 200,
  "data": {
    "id": 1,
    "bankId": 1,
    "questionType": "SINGLE_CHOICE",
    "content": "题目内容...",
    "options": [
      { "key": "A", "value": "选项A" },
      { "key": "B", "value": "选项B" },
      { "key": "C", "value": "选项C" },
      { "key": "D", "value": "选项D" }
    ],
    "correctAnswer": "A",
    "explanation": "答案解析...",
    "difficultyLevel": 2,
    "sequenceNumber": 1
  }
}
```

---

## 4. 用户练习接口

### 4.1 提交答案

**接口地址：** `POST /api/user-practice/submit`

**请求参数：**

```json
{
  "questionId": 1,
  "userAnswer": "A",
  "timeCost": 30,
  "practiceMode": "ANSWER"
}
```

**响应数据：**

```json
{
  "code": 200,
  "data": {
    "isCorrect": true,
    "correctAnswer": "A",
    "explanation": "答案解析..."
  }
}
```

### 4.2 获取我的错题

**接口地址：** `GET /api/user-practice/wrong-questions`

**请求参数：**
- `bankId` - 题库ID

### 4.3 收藏题目

**接口地址：** `POST /api/user-practice/favorites/{questionId}`

### 4.4 取消收藏

**接口地址：** `DELETE /api/user-practice/favorites/{questionId}`

### 4.5 获取我的收藏

**接口地址：** `GET /api/user-practice/favorites`

**请求参数：**
- `bankId` - 题库ID

### 4.6 添加笔记

**接口地址：** `POST /api/user-practice/notes`

**请求参数：**

```json
{
  "questionId": 1,
  "content": "笔记内容..."
}
```

### 4.7 获取我的笔记

**接口地址：** `GET /api/user-practice/notes`

**请求参数：**
- `bankId` - 题库ID

### 4.8 获取易错题

**接口地址：** `GET /api/user-practice/difficult-questions`

**请求参数：**
- `bankId` - 题库ID

---

## 5. 模拟考试接口

### 5.1 创建模拟考试

**接口地址：** `POST /api/exams`

**请求参数：**

```json
{
  "bankId": 1,
  "questionCount": 50,
  "duration": 90
}
```

**响应数据：**

```json
{
  "code": 200,
  "data": {
    "id": 1,
    "title": "模拟考试 2025-01-01",
    "questionIds": [1, 2, 3, ...],
    "duration": 90,
    "startTime": "2025-01-01 10:00:00"
  }
}
```

### 5.2 获取考试详情

**接口地址：** `GET /api/exams/{examId}`

### 5.3 提交考试

**接口地址：** `POST /api/exams/{examId}/submit`

**请求参数：**

```json
{
  "answers": {
    "1": "A",
    "2": "B,C",
    "3": "TRUE"
  }
}
```

**响应数据：**

```json
{
  "code": 200,
  "data": {
    "examId": 1,
    "score": 85,
    "totalScore": 100,
    "accuracy": 0.85,
    "correctCount": 42,
    "wrongCount": 8,
    "wrongQuestions": [...]
  }
}
```

### 5.4 获取考试记录

**接口地址：** `GET /api/exams/records`

**请求参数：**
- `bankId` - 题库ID

---

## 在线文档

启动后端后访问：http://localhost:8080/doc.html

使用 Knife4j 提供的交互式 API 文档。


