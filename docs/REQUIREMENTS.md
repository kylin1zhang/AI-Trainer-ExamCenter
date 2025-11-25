# 详细需求说明

> AI Trainer Exam Center - 人工智能训练师考试中心详细需求文档

## 一、整体功能概述

### 核心对象：题库

当前题库：**人工智能训练师（三级/高级）**

题库内容：
- ✅ 判断题
- ✅ 单选题
- ✅ 多选题
- ✅ 题目解析

### 数据来源

题库数据已整理在 `data.json` 文件中，数据结构如下：

```json
{
  "id": 1,
  "type": "judge",  // judge-判断题, single-单选题, multiple-多选题
  "question": "题干内容",
  "option_A": "选项A",
  "option_B": "选项B",
  "option_C": "选项C",  // 判断题时为 null
  "option_D": "选项D",
  "option_E": "选项E",
  "answer": "A",  // 单选/判断：A  多选：A,B,C
  "explanation": "详细解析"
}
```

---

## 二、核心模块

### 1. 用户系统

#### 1.1 登录页面

**功能：**
- 账号 + 密码登录
- 登录后使用 JWT Token 维持会话
- 前端路由做登录拦截（未登录跳转登录页）

**技术实现：**
- 后端：Spring Security + JWT
- 前端：Vue Router 路由守卫
- Token 存储：localStorage

#### 1.2 注册功能

**实现方式：**
- 前端可以只做登录页面
- 注册可用后端接口或脚本造数据
- 初始化账户接口

---

### 2. 我的题库页面

**功能：**
- 显示当前可用题库列表
- 当前只有一个：人工智能训练师（三级/高级）

**展示信息：**
- 题库名称
- 题目总数
- 最近练习时间
- 练习进度

**交互：**
- 点击题库卡片 → 进入题库详情页

---

### 3. 题库详情页（练习主页）

分为两大区域：

#### 3.1 我的练习

| 功能模块 | 说明 |
|---------|------|
| **我的错题** | 展示该用户所有错过的题目，按题目ID去重，支持再练习 |
| **我的收藏** | 展示收藏的题目列表 |
| **我的笔记** | 展示有笔记的题目列表 |

#### 3.2 专项练习

| 功能模块 | 说明 |
|---------|------|
| **顺序练习** | 按题目顺序依次练习 |
| **随机练习** | 打乱题目顺序练习 |
| **题型练习** | 按题型分类练习（判断题/单选题/多选题） |
| **模拟考试** | 完整模拟真实考试流程 |
| **易错题** | 展示错误次数 ≥ 2 次的题目 |

---

### 4. 做题页面（练习 & 模拟考试通用）

#### 4.1 页面布局

```
┌────────────────────────────────────────┐
│  顶部操作栏                             │
│  [返回] [模式切换] [答题卡]             │
├────────────────────────────────────────┤
│                                        │
│  题目区域                               │
│  【判断题/单选题/多选题】 第 1/100 题   │
│  题干：...                             │
│                                        │
├────────────────────────────────────────┤
│                                        │
│  选项区域                               │
│  ○ A. 选项A                            │
│  ○ B. 选项B                            │
│  ○ C. 选项C                            │
│  ○ D. 选项D                            │
│                                        │
├────────────────────────────────────────┤
│                                        │
│  答案区域（根据模式显示）                │
│  正确答案：A                            │
│                                        │
├────────────────────────────────────────┤
│                                        │
│  解析区域（根据模式显示）                │
│  详细解析：...                         │
│                                        │
├────────────────────────────────────────┤
│  底部操作栏                             │
│  [上一题] [收藏] [笔记] [下一题]        │
└────────────────────────────────────────┘
```

#### 4.2 题目区域

**显示内容：**
- 题型标识（判断题/单选题/多选题）
- 当前题号（第 X 题 / 共 Y 题）
- 题干内容

#### 4.3 选项区域

**显示内容：**
- A/B/C/D/E 选项
- 判断题只显示 A/B（正确/错误）
- 单选题显示所有非空选项
- 多选题显示所有非空选项（支持多选）

#### 4.4 答案区域

**显示规则：**
- 答题模式：用户选择后显示
- 背题模式：直接显示

**显示内容：**
- 标准答案
- 判断对错（答题模式）

#### 4.5 解析区域

**显示规则：**
- 答题模式：用户选择后显示
- 背题模式：直接显示

**显示内容：**
- 详细的答案解析

#### 4.6 答题卡区域

**功能：**
- 展示本次练习/考试所有题号
- 显示作答状态：
  - 未作答：灰色
  - 已作答：蓝色
  - 正确：绿色
  - 错误：红色
- 点击题号快速跳转

#### 4.7 模式切换

**两种模式：**

1. **答题模式（ANSWER）**
   - 用户选择选项后立即判断对错
   - 正确：选项显示绿色
   - 错误：选项显示红色
   - 同时展示标准答案和解析

2. **背题模式（RECITE）**
   - 进入页面时就直接展示答案和解析
   - 无需点击选项
   - 适合快速记忆

#### 4.8 操作按钮

| 按钮 | 功能 |
|------|------|
| **上一题** | 跳转到上一题 |
| **下一题** | 跳转到下一题 |
| **收藏** | 收藏当前题目 |
| **笔记** | 弹出框记录文字笔记 |
| **标记错题** | 手动标记为错题（可选功能） |

---

### 5. 错题与易错题逻辑

#### 5.1 错题记录规则

**重要：任何错误都需要记录**

- 无论是练习还是考试
- 只要用户提交答案错误
- 就在后台 `tb_user_answer` 表记录一条答题记录
- 记录字段：`is_correct = 0`

#### 5.2 我的错题

**查询逻辑：**
1. 查询该用户所有 `is_correct = 0` 的答题记录
2. 按题目ID去重（同一题可能错多次，只显示一次）
3. 按最近错误时间倒序排列
4. 显示题目及错误次数

**功能：**
- 支持再次练习
- 可以移除错题（标记为已掌握）
- 显示错误次数统计

#### 5.3 易错题

**查询逻辑：**
1. 统计该用户对每道题的错误次数
2. 筛选出错误次数 ≥ 2 次的题目
3. 按错误次数降序排列

**显示信息：**
- 题目内容
- 错误次数
- 总作答次数
- 错误率

**说明：**
- 只统计该用户自己的易错题
- 不是平台全局易错题

---

### 6. 模拟考试

#### 6.1 考试规则（人工智能训练师三级）

| 题型 | 题目数量 | 每题分数 | 小计分数 |
|------|----------|---------|---------|
| 判断题 | 40 题 | 0.5 分 | 20 分 |
| 单选题 | 140 题 | 0.5 分 | 70 分 |
| 多选题 | 10 题 | 1 分 | 10 分 |
| **总计** | **190 题** | - | **100 分** |

**及格标准：** ≥ 60 分

**考试时长：** 90 分钟倒计时

#### 6.2 考试流程

**1. 创建考试**
- 从题库中随机抽取题目：
  - 判断题 40 题
  - 单选题 140 题
  - 多选题 10 题
- 生成考试记录
- 记录开始时间

**2. 考试过程**
- 显示倒计时
- 使用答题卡
- 支持快速跳转
- 默认使用"答题模式"（不立即显示答案）
- 可以随时查看答题卡

**3. 考试结束**

**触发条件：**
- 时间到（自动交卷）
- 用户主动交卷

**结束后操作：**
1. 计算总分
2. 判断是否及格（≥ 60 分）
3. 保存考试记录：
   - 开始时间
   - 结束时间
   - 得分
   - 正确率
   - 正确题数
   - 错误题数
4. 记录所有答题记录到 `tb_user_answer` 表
5. 展示考试结果

**4. 查看试卷解析**
- 批量展示每题答案与解析
- 高亮显示错题
- 支持查看详细解析

#### 6.3 考试页面特殊要求

**考试过程中：**
- 默认使用"答题模式"
- 选择答案后不立即显示对错（可配置）
- 答题卡显示已答/未答状态

**交卷后：**
- 显示考试结果（分数、正确率、是否及格）
- 切换到"解析模式"，展示所有题目的答案和解析
- 高亮显示错题

---

## 三、技术实现要点

### 1. 数据导入

**data.json 导入数据库：**

```sql
-- 示例：插入题目数据
INSERT INTO tb_question (
    id, bank_id, type, question, 
    option_a, option_b, option_c, option_d, option_e,
    answer, explanation, sequence_number
) VALUES (
    1, 1, 'judge', '题干内容',
    '正确', '错误', NULL, NULL, NULL,
    'A', '详细解析', 1
);
```

**建议：**
- 编写数据导入脚本（Python/Java）
- 读取 data.json 文件
- 批量插入到数据库
- 设置 `bank_id = 1`（人工智能训练师题库）

### 2. 答题记录

**重要：任何错误都需要记录**

```java
// 伪代码示例
public void submitAnswer(SubmitAnswerDTO dto) {
    // 1. 判断答案是否正确
    boolean isCorrect = checkAnswer(dto.getQuestionId(), dto.getUserAnswer());
    
    // 2. 记录答题记录（无论对错都记录）
    UserAnswer record = new UserAnswer();
    record.setUserId(getCurrentUserId());
    record.setQuestionId(dto.getQuestionId());
    record.setUserAnswer(dto.getUserAnswer());
    record.setIsCorrect(isCorrect ? 1 : 0);
    record.setPracticeMode(dto.getPracticeMode());
    userAnswerMapper.insert(record);
    
    // 3. 返回结果
    return SubmitAnswerResponse.builder()
        .isCorrect(isCorrect)
        .correctAnswer(getCorrectAnswer())
        .explanation(getExplanation())
        .build();
}
```

### 3. 错题查询

**我的错题（去重）：**

```sql
-- 查询我的错题（按题目ID去重）
SELECT 
    question_id,
    COUNT(*) as wrong_count,
    MAX(create_time) as last_wrong_time
FROM tb_user_answer
WHERE user_id = ? 
  AND bank_id = ?
  AND is_correct = 0
GROUP BY question_id
ORDER BY last_wrong_time DESC;
```

**易错题（错误次数 ≥ 2）：**

```sql
-- 查询易错题（错误次数 ≥ 2）
SELECT 
    question_id,
    SUM(CASE WHEN is_correct = 0 THEN 1 ELSE 0 END) as wrong_count,
    COUNT(*) as total_attempts
FROM tb_user_answer
WHERE user_id = ? 
  AND bank_id = ?
GROUP BY question_id
HAVING wrong_count >= 2
ORDER BY wrong_count DESC;
```

### 4. 模拟考试评分

```java
// 伪代码示例
public ExamResult calculateScore(Long examId, Map<Long, String> answers) {
    // 1. 获取考试信息
    Exam exam = examMapper.selectById(examId);
    List<Question> questions = getExamQuestions(exam.getQuestionIds());
    
    // 2. 统计各题型正确数
    int judgeCorrect = 0;
    int singleCorrect = 0;
    int multipleCorrect = 0;
    
    for (Question question : questions) {
        String userAnswer = answers.get(question.getId());
        boolean isCorrect = question.getAnswer().equals(userAnswer);
        
        if (isCorrect) {
            switch (question.getType()) {
                case "judge": judgeCorrect++; break;
                case "single": singleCorrect++; break;
                case "multiple": multipleCorrect++; break;
            }
        }
        
        // 记录答题记录
        recordUserAnswer(userId, question.getId(), userAnswer, isCorrect, "EXAM", examId);
    }
    
    // 3. 计算总分
    double score = judgeCorrect * 0.5 + singleCorrect * 0.5 + multipleCorrect * 1.0;
    boolean isPassed = score >= 60;
    
    // 4. 更新考试记录
    exam.setScore(score);
    exam.setIsPassed(isPassed ? 1 : 0);
    exam.setCorrectCount(judgeCorrect + singleCorrect + multipleCorrect);
    exam.setStatus("FINISHED");
    examMapper.updateById(exam);
    
    // 5. 返回结果
    return ExamResult.builder()
        .score(score)
        .isPassed(isPassed)
        .build();
}
```

---

## 四、前端页面路由

| 路由 | 页面 | 说明 |
|------|------|------|
| `/login` | 登录页 | 账号密码登录 |
| `/` | 我的题库 | 题库列表页 |
| `/banks/:id` | 题库详情 | 我的练习 + 专项练习 |
| `/practice` | 做题页面 | 练习/考试通用页面 |
| `/exam/:examId` | 考试页面 | 模拟考试专用页面 |

---

## 五、数据库表关系

```
tb_user（用户表）
    ↓ 1:N
tb_user_answer（答题记录）← 用于统计错题和易错题
    ↓ N:1
tb_question（题目表）← 对应 data.json
    ↓ N:1
tb_question_bank（题库表）

tb_user（用户表）
    ↓ 1:N
tb_exam（考试记录）
    ↓ 1:N
tb_user_answer（答题记录）

tb_user（用户表）
    ↓ 1:N
tb_user_favorite（收藏记录）
    ↓ N:1
tb_question（题目表）

tb_user（用户表）
    ↓ 1:N
tb_user_note（笔记记录）
    ↓ N:1
tb_question（题目表）
```

---

## 六、核心业务逻辑总结

### 1. 答题流程

```
用户选择答案
    ↓
提交答案到后端
    ↓
判断答案是否正确
    ↓
记录答题记录（任何错误都记录）
    ↓
返回结果（是否正确、正确答案、解析）
    ↓
前端展示结果（根据模式显示）
```

### 2. 错题统计流程

```
用户查看我的错题
    ↓
查询 tb_user_answer 表（is_correct = 0）
    ↓
按题目ID去重
    ↓
关联 tb_question 表获取题目信息
    ↓
返回错题列表（含错误次数）
```

### 3. 易错题统计流程

```
用户查看易错题
    ↓
统计 tb_user_answer 表中每道题的错误次数
    ↓
筛选错误次数 ≥ 2 的题目
    ↓
按错误次数降序排列
    ↓
返回易错题列表（含错误次数、总作答次数、错误率）
```

### 4. 模拟考试流程

```
创建考试
    ↓
随机抽取题目（判断40 + 单选140 + 多选10）
    ↓
开始考试（记录开始时间）
    ↓
答题过程（90分钟倒计时）
    ↓
交卷（时间到或主动交卷）
    ↓
批量记录答题记录
    ↓
计算分数和正确率
    ↓
判断是否及格
    ↓
保存考试记录
    ↓
展示考试结果和试卷解析
```

---

## 七、开发优先级

### 第一阶段：核心功能（高优先级）

1. ✅ 用户登录/注册
2. ✅ 题库列表展示
3. ✅ 数据导入（data.json → 数据库）
4. ✅ 题库详情页（两大区域展示）
5. ✅ 做题页面（答题模式/背题模式）
6. ✅ 答题记录保存
7. ✅ 我的错题（去重展示）
8. ✅ 模拟考试（完整流程）

### 第二阶段：增强功能（中优先级）

1. ✅ 易错题统计（错误次数 ≥ 2）
2. ✅ 收藏功能
3. ✅ 笔记功能
4. ✅ 答题卡快速跳转
5. ✅ 考试结果统计
6. ✅ 学习进度跟踪

### 第三阶段：优化功能（低优先级）

1. 移动端适配
2. 题目搜索
3. 学习报告
4. 数据可视化
5. 社区讨论

---

Copyright © 2025 上海智学无界教育科技有限公司


