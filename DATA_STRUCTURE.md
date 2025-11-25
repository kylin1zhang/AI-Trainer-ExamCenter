# data.json 数据结构说明

> AI Trainer Exam Center - 题目数据结构文档

## 数据来源

题库数据已整理在 `data.json` 文件中，包含人工智能训练师（三级/高级）的所有题目。

## 数据结构

### JSON 格式

```json
{
  "id": 1,
  "type": "judge",
  "question": "道德评价的关键是看其行为是否符合社会道德规范。",
  "option_A": "正确",
  "option_B": "错误",
  "option_C": null,
  "option_D": null,
  "option_E": null,
  "answer": "A",
  "explanation": "[答案依据]：题干明确指出..."
}
```

### 字段说明

| 字段名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| `id` | Number | ✅ | 题目唯一标识 |
| `type` | String | ✅ | 题目类型：`judge`（判断题）、`single`（单选题）、`multiple`（多选题） |
| `question` | String | ✅ | 题干内容 |
| `option_A` | String/null | ✅ | 选项A（判断题：正确/错误） |
| `option_B` | String/null | ✅ | 选项B |
| `option_C` | String/null | ❌ | 选项C（判断题时为 null） |
| `option_D` | String/null | ❌ | 选项D（判断题时为 null） |
| `option_E` | String/null | ❌ | 选项E（可选） |
| `answer` | String | ✅ | 正确答案（单选/判断：`A`、`B`等；多选：`A,B,C`逗号分隔） |
| `explanation` | String | ✅ | 答案解析（详细说明） |

## 题目类型

### 1. 判断题（judge）

**特点：**
- 只有 A、B 两个选项
- option_C、option_D、option_E 为 null
- 答案通常为 A（正确）或 B（错误）

**示例：**

```json
{
  "id": 1,
  "type": "judge",
  "question": "道德评价的关键是看其行为是否符合社会道德规范。",
  "option_A": "正确",
  "option_B": "错误",
  "option_C": null,
  "option_D": null,
  "option_E": null,
  "answer": "A",
  "explanation": "[答案依据]：题干明确指出..."
}
```

**考试规则：**
- 考试题数：40 题
- 每题分数：0.5 分
- 小计：20 分

### 2. 单选题（single）

**特点：**
- 有 A、B、C、D 四个或更多选项
- option_E 可能为 null
- 答案为单个选项，如 A、B、C、D

**示例：**

```json
{
  "id": 100,
  "type": "single",
  "question": "以下哪项是人工智能训练师的主要职责？",
  "option_A": "设计算法",
  "option_B": "数据标注",
  "option_C": "模型训练",
  "option_D": "系统维护",
  "option_E": null,
  "answer": "C",
  "explanation": "[答案依据]：人工智能训练师主要负责..."
}
```

**考试规则：**
- 考试题数：140 题
- 每题分数：0.5 分
- 小计：70 分

### 3. 多选题（multiple）

**特点：**
- 有 A、B、C、D、E 多个选项
- 答案为多个选项，用逗号分隔，如 A,B,C

**示例：**

```json
{
  "id": 200,
  "type": "multiple",
  "question": "以下哪些属于人工智能训练师的职业道德？（多选）",
  "option_A": "保护用户隐私",
  "option_B": "确保数据质量",
  "option_C": "随意使用数据",
  "option_D": "对模型负责",
  "option_E": "忽视偏见问题",
  "answer": "A,B,D",
  "explanation": "[答案依据]：人工智能训练师应当..."
}
```

**考试规则：**
- 考试题数：10 题
- 每题分数：1 分
- 小计：10 分

## 数据导入

### 方式一：Python 脚本（推荐）

创建 `scripts/import_data.py`：

```python
import json
import mysql.connector

# 连接数据库
conn = mysql.connector.connect(
    host='localhost',
    user='root',
    password='your_password',
    database='exam_center'
)
cursor = conn.cursor()

# 读取 data.json
with open('data.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

# 批量插入
for item in data:
    sql = """
    INSERT INTO tb_question (
        id, bank_id, type, question, 
        option_a, option_b, option_c, option_d, option_e,
        answer, explanation, sequence_number
    ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
    """
    values = (
        item['id'], 
        1,  # bank_id 固定为 1（人工智能训练师题库）
        item['type'],
        item['question'],
        item['option_A'],
        item['option_B'],
        item['option_C'],
        item['option_D'],
        item['option_E'],
        item['answer'],
        item['explanation'],
        item['id']  # sequence_number 使用 id
    )
    cursor.execute(sql, values)

conn.commit()
cursor.close()
conn.close()

print(f"成功导入 {len(data)} 条题目数据")
```

运行：

```bash
python scripts/import_data.py
```

### 方式二：Java 程序

创建 `DataImporter.java`：

```java
package com.zhixuewujie.examcenter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhixuewujie.examcenter.entity.Question;
import java.io.File;
import java.util.List;

public class DataImporter {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<Question> questions = mapper.readValue(
            new File("data.json"), 
            mapper.getTypeFactory().constructCollectionType(List.class, Question.class)
        );
        
        // 批量插入数据库
        for (Question question : questions) {
            question.setBankId(1L); // 设置题库ID
            question.setSequenceNumber(question.getId().intValue());
            questionMapper.insert(question);
        }
        
        System.out.println("成功导入 " + questions.size() + " 条题目数据");
    }
}
```

### 方式三：SQL 脚本

如果数据量不大，也可以手动编写 SQL 脚本。

## 数据统计

根据 `data.json` 文件统计：

- **总题目数**：约 10802 行（需确认实际题目数量）
- **判断题数量**：待统计
- **单选题数量**：待统计
- **多选题数量**：待统计

## 注意事项

1. **ID 唯一性**：题目 ID 必须唯一，不能重复
2. **类型规范**：type 字段只能是 `judge`、`single`、`multiple` 三种
3. **答案格式**：
   - 单选/判断：单个字母，如 `A`
   - 多选：逗号分隔，如 `A,B,C`
4. **选项处理**：判断题的 option_C、option_D、option_E 为 null
5. **字符编码**：使用 UTF-8 编码，确保中文正常显示

## 数据验证

导入数据后，建议执行以下 SQL 验证：

```sql
-- 验证题目总数
SELECT COUNT(*) as total FROM tb_question WHERE bank_id = 1;

-- 统计各题型数量
SELECT type, COUNT(*) as count 
FROM tb_question 
WHERE bank_id = 1 
GROUP BY type;

-- 检查是否有重复ID
SELECT id, COUNT(*) as count 
FROM tb_question 
WHERE bank_id = 1 
GROUP BY id 
HAVING count > 1;

-- 检查答案格式是否正确
SELECT id, type, answer 
FROM tb_question 
WHERE bank_id = 1 
  AND (
    (type = 'judge' AND answer NOT IN ('A', 'B'))
    OR (type = 'single' AND LENGTH(answer) > 1)
    OR (type = 'multiple' AND answer NOT LIKE '%,%')
  );
```

---

Copyright © 2025 上海智学无界教育科技有限公司


