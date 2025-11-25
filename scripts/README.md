# 数据导入脚本说明

> AI Trainer Exam Center - 数据导入工具

## 脚本说明

### import_data.py

**功能：** 将 `data.json` 中的题目数据导入到 MySQL 数据库

**特点：**
- ✅ 自动读取和解析 JSON 文件
- ✅ 数据验证和错误处理
- ✅ 批量导入优化性能
- ✅ 进度显示
- ✅ 数据统计分析
- ✅ 导入结果验证
- ✅ 支持重复导入（ON DUPLICATE KEY UPDATE）

---

## 使用方法

### 1. 安装依赖

```bash
pip install mysql-connector-python
```

### 2. 修改配置

编辑 `import_data.py`，修改数据库连接配置：

```python
DB_CONFIG = {
    'host': 'localhost',
    'user': 'root',
    'password': 'your_password',  # 修改为您的数据库密码
    'database': 'exam_center',
    'charset': 'utf8mb4'
}
```

### 3. 确保数据库已创建

确保已执行建表脚本：

```bash
mysql -u root -p < backend/src/main/resources/sql/schema.sql
```

### 4. 运行导入脚本

```bash
python scripts/import_data.py
```

---

## 导入流程

```
1. 读取 data.json 文件
   ↓
2. 分析数据统计（题目总数、各题型数量）
   ↓
3. 连接 MySQL 数据库
   ↓
4. 检查 tb_question 表是否存在
   ↓
5. 询问是否清空现有数据（可选）
   ↓
6. 批量导入题目数据
   ↓
7. 显示导入进度
   ↓
8. 提交事务
   ↓
9. 验证导入结果
   ↓
10. 更新题库表的题目总数
   ↓
11. 完成
```

---

## 示例输出

```
============================================================
  AI Trainer Exam Center - 数据导入工具
  上海智学无界教育科技有限公司
============================================================
✅ 成功读取 data.json，共 10802 条题目

📊 数据统计：
   总题目数：10802
   判断题（judge）：4500 题
   单选题（single）：5200 题
   多选题（multiple）：1102 题

✅ 成功连接到数据库 exam_center
✅ 表 tb_question 存在
⚠️  是否清空现有题目数据？(y/N): n
⏭️  跳过清空数据

开始导入题目...
进度：100/10802 (0.9%)
进度：200/10802 (1.9%)
...
进度：10800/10802 (100.0%)

✅ 导入完成！成功：10802 条，失败：0 条
✅ 数据已提交到数据库

🔍 验证导入结果...
   数据库中题目总数：10802
   判断题：4500 题
   单选题：5200 题
   多选题：1102 题
✅ 验证通过：数据完整
✅ 已更新题库题目总数

============================================================
  🎉 数据导入成功完成！
============================================================

✅ 数据库连接已关闭
```

---

## 常见问题

### 1. 连接数据库失败

**错误信息：**
```
❌ 数据库连接失败：Access denied for user 'root'@'localhost'
```

**解决方法：**
- 检查数据库用户名和密码是否正确
- 确保 MySQL 服务已启动
- 检查数据库是否存在

### 2. 表不存在

**错误信息：**
```
❌ 错误：tb_question 表不存在，请先执行建表脚本
```

**解决方法：**
```bash
mysql -u root -p < backend/src/main/resources/sql/schema.sql
```

### 3. 文件找不到

**错误信息：**
```
❌ 错误：找不到文件 data.json
```

**解决方法：**
- 确保 `data.json` 文件在项目根目录
- 或者在脚本中指定正确的文件路径

### 4. JSON 解析失败

**错误信息：**
```
❌ 错误：JSON 解析失败
```

**解决方法：**
- 检查 `data.json` 文件格式是否正确
- 确保文件编码为 UTF-8

---

## 验证导入结果

导入完成后，可以在数据库中执行以下 SQL 验证：

```sql
-- 查看题目总数
SELECT COUNT(*) FROM tb_question WHERE bank_id = 1;

-- 统计各题型数量
SELECT type, COUNT(*) as count 
FROM tb_question 
WHERE bank_id = 1 
GROUP BY type;

-- 查看前10条题目
SELECT id, type, question, answer 
FROM tb_question 
WHERE bank_id = 1 
LIMIT 10;

-- 检查是否有重复ID
SELECT id, COUNT(*) as count 
FROM tb_question 
WHERE bank_id = 1 
GROUP BY id 
HAVING count > 1;
```

---

## 重复导入说明

脚本使用 `ON DUPLICATE KEY UPDATE` 语法，支持重复导入：

- 如果题目ID已存在，则更新题目内容
- 如果题目ID不存在，则插入新记录
- 不会产生重复数据

---

## 注意事项

1. **数据备份**：导入前建议备份现有数据
2. **编码问题**：确保 data.json 使用 UTF-8 编码
3. **权限问题**：确保数据库用户有 INSERT 和 UPDATE 权限
4. **大文件处理**：如果数据量很大，导入可能需要较长时间

---

## 技术支持

如遇问题，请查阅：
- 数据结构说明：`DATA_STRUCTURE.md`
- 详细需求文档：`docs/REQUIREMENTS.md`

---

Copyright © 2025 上海智学无界教育科技有限公司


