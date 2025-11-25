# 快速启动指南

> 5分钟快速启动 AI Trainer Exam Center

## 准备工作

确保已安装：
- ✅ JDK 17+
- ✅ Maven 3.8+
- ✅ MySQL 8.0+
- ✅ Redis 6.0+
- ✅ Node.js 18+

---

## 第一步：克隆项目

```bash
git clone https://github.com/your-org/AI-Trainer-ExamCenter.git
cd AI-Trainer-ExamCenter
```

---

## 第二步：初始化数据库

```bash
# 登录 MySQL
mysql -u root -p

# 执行建表脚本
source backend/src/main/resources/sql/schema.sql

# 或者直接导入
mysql -u root -p < backend/src/main/resources/sql/schema.sql
```

---

## 第三步：启动 Redis

```bash
# Linux/Mac
redis-server

# Windows（如果使用 WSL 或安装了 Redis for Windows）
redis-server.exe
```

---

## 第四步：启动后端

```bash
cd backend

# 修改配置文件（如果需要）
# 编辑 src/main/resources/application.yml
# 修改数据库用户名和密码

# 启动后端
mvn spring-boot:run
```

✅ 后端启动成功后访问：
- API 服务：http://localhost:8080
- API 文档：http://localhost:8080/doc.html

---

## 第五步：启动前端

打开**新的终端窗口**：

```bash
cd frontend

# 安装依赖（首次运行）
npm install

# 启动开发服务器
npm run dev
```

✅ 前端启动成功后访问：http://localhost:3000

---

## 第六步：测试系统

1. 打开浏览器访问：http://localhost:3000
2. 点击"注册"创建账号
3. 登录系统
4. 查看题库列表
5. 开始练习！

---

## 常见问题

### 问题 1：后端启动失败 - 数据库连接错误

**原因：** 数据库配置错误或 MySQL 未启动

**解决：**
```bash
# 检查 MySQL 是否启动
sudo systemctl status mysql

# 检查配置文件
cat backend/src/main/resources/application.yml
```

### 问题 2：前端无法访问后端 API

**原因：** 端口被占用或跨域配置问题

**解决：**
```bash
# 检查后端是否启动
curl http://localhost:8080/api/question-banks

# 检查 Vite 代理配置
cat frontend/vite.config.ts
```

### 问题 3：Redis 连接失败

**原因：** Redis 未启动

**解决：**
```bash
# 启动 Redis
redis-server

# 检查 Redis 是否运行
redis-cli ping
# 应该返回 PONG
```

---

## 下一步

- 📖 阅读 [API 文档](./API.md)
- 🚀 查看 [部署指南](./DEPLOYMENT.md)
- 🤝 参与 [贡献代码](./CONTRIBUTING.md)

---

## 快捷命令

### 后端

```bash
# 启动（开发环境）
cd backend && mvn spring-boot:run

# 打包
cd backend && mvn clean package -DskipTests

# 运行测试
cd backend && mvn test
```

### 前端

```bash
# 安装依赖
cd frontend && npm install

# 启动开发服务器
cd frontend && npm run dev

# 构建生产版本
cd frontend && npm run build

# 预览生产版本
cd frontend && npm run preview
```

---

祝您使用愉快！🎉

如有问题，请联系：support@zhixuewujie.com

---

Copyright © 2025 上海智学无界教育科技有限公司


