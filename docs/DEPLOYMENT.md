# 部署指南

> AI Trainer Exam Center - 部署文档

## 环境要求

### 服务器环境
- **操作系统**: Linux (Ubuntu 20.04+ / CentOS 7+) 或 Windows Server
- **CPU**: 2核及以上
- **内存**: 4GB 及以上
- **硬盘**: 20GB 及以上

### 软件环境
- **JDK**: 17+
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **Nginx**: 1.18+ (可选，用于前端部署)

---

## 后端部署

### 1. 数据库初始化

```bash
# 登录 MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE exam_center DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入表结构
USE exam_center;
SOURCE /path/to/backend/src/main/resources/sql/schema.sql;
```

### 2. Redis 安装与启动

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install redis-server
sudo systemctl start redis
sudo systemctl enable redis

# CentOS
sudo yum install redis
sudo systemctl start redis
sudo systemctl enable redis
```

### 3. 修改配置文件

复制生产环境配置模板：

```bash
cd backend/src/main/resources
cp application-prod.yml.example application-prod.yml
```

编辑 `application-prod.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/exam_center
    username: your_db_username
    password: your_db_password
  
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password

jwt:
  secret: your-secure-jwt-secret-key
```

### 4. 打包项目

```bash
cd backend
mvn clean package -DskipTests
```

生成的 JAR 文件：`target/examcenter-backend-1.0.0.jar`

### 5. 启动后端服务

**方式一：直接运行**

```bash
java -jar target/examcenter-backend-1.0.0.jar --spring.profiles.active=prod
```

**方式二：使用 systemd 服务（推荐）**

创建服务文件：`/etc/systemd/system/examcenter.service`

```ini
[Unit]
Description=AI Trainer Exam Center Backend
After=syslog.target network.target

[Service]
User=www-data
WorkingDirectory=/opt/examcenter
ExecStart=/usr/bin/java -jar /opt/examcenter/examcenter-backend-1.0.0.jar --spring.profiles.active=prod
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

启动服务：

```bash
sudo systemctl daemon-reload
sudo systemctl start examcenter
sudo systemctl enable examcenter
sudo systemctl status examcenter
```

### 6. 查看日志

```bash
# 查看实时日志
tail -f logs/examcenter.log

# 或者使用 systemd 日志
sudo journalctl -u examcenter -f
```

---

## 前端部署

### 1. 构建生产版本

```bash
cd frontend

# 安装依赖
npm install

# 构建
npm run build
```

生成的静态文件在 `dist/` 目录。

### 2. 部署到 Nginx

**安装 Nginx**

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install nginx

# CentOS
sudo yum install nginx
```

**配置 Nginx**

创建配置文件：`/etc/nginx/sites-available/examcenter`

```nginx
server {
    listen 80;
    server_name your-domain.com;

    # 前端静态文件
    root /var/www/examcenter;
    index index.html;

    # 前端路由支持
    location / {
        try_files $uri $uri/ /index.html;
    }

    # 代理后端 API
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # 静态资源缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

**部署文件**

```bash
# 创建部署目录
sudo mkdir -p /var/www/examcenter

# 复制构建文件
sudo cp -r frontend/dist/* /var/www/examcenter/

# 设置权限
sudo chown -R www-data:www-data /var/www/examcenter

# 启用配置
sudo ln -s /etc/nginx/sites-available/examcenter /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

### 3. HTTPS 配置（推荐）

使用 Let's Encrypt 免费证书：

```bash
# 安装 Certbot
sudo apt install certbot python3-certbot-nginx

# 获取证书
sudo certbot --nginx -d your-domain.com

# 自动续期
sudo certbot renew --dry-run
```

---

## Docker 部署（可选）

### 后端 Dockerfile

创建 `backend/Dockerfile`：

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/examcenter-backend-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
```

### 前端 Dockerfile

创建 `frontend/Dockerfile`：

```dockerfile
FROM nginx:alpine

COPY dist/ /usr/share/nginx/html/

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
```

### Docker Compose

创建 `docker-compose.yml`：

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root_password
      MYSQL_DATABASE: exam_center
    volumes:
      - mysql-data:/var/lib/mysql
      - ./backend/src/main/resources/sql/schema.sql:/docker-entrypoint-initdb.d/schema.sql
    ports:
      - "3306:3306"

  redis:
    image: redis:6-alpine
    ports:
      - "6379:6379"

  backend:
    build: ./backend
    depends_on:
      - mysql
      - redis
    environment:
      SPRING_PROFILES_ACTIVE: prod
    ports:
      - "8080:8080"

  frontend:
    build: ./frontend
    ports:
      - "80:80"
    depends_on:
      - backend

volumes:
  mysql-data:
```

启动服务：

```bash
docker-compose up -d
```

---

## 性能优化

### 1. 后端优化

**JVM 参数调优：**

```bash
java -Xms1024m -Xmx2048m \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -jar examcenter-backend-1.0.0.jar
```

**数据库连接池配置：**

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 10
      connection-timeout: 30000
```

### 2. 前端优化

- 开启 Gzip 压缩
- 配置 CDN 加速
- 使用浏览器缓存
- 图片懒加载

### 3. Redis 缓存策略

- 题目详情缓存（1小时）
- 题库列表缓存（10分钟）
- 用户信息缓存（30分钟）

---

## 监控与维护

### 1. 日志管理

- 定期清理日志文件
- 使用日志分析工具（ELK Stack）
- 设置日志告警

### 2. 数据库备份

```bash
# 每日自动备份
0 2 * * * /usr/bin/mysqldump -u root -p'password' exam_center > /backup/exam_center_$(date +\%Y\%m\%d).sql
```

### 3. 监控工具

- **Spring Boot Actuator**: 应用健康检查
- **Prometheus + Grafana**: 性能监控
- **Nginx 日志分析**: 访问统计

---

## 故障排查

### 后端无法启动

1. 检查数据库连接是否正常
2. 检查 Redis 是否启动
3. 查看日志文件：`logs/examcenter.log`

### 前端页面空白

1. 检查浏览器控制台错误
2. 确认 API 地址配置正确
3. 检查 Nginx 配置和日志

### 数据库连接超时

1. 检查防火墙设置
2. 增加数据库连接池大小
3. 优化慢查询

---

## 安全建议

1. **修改默认密码**: 数据库、Redis、JWT 密钥
2. **开启防火墙**: 只开放必要端口
3. **使用 HTTPS**: 加密传输数据
4. **定期更新**: 保持依赖包最新版本
5. **备份数据**: 定期备份数据库
6. **限流防护**: 防止恶意请求

---

## 联系支持

如遇部署问题，请联系技术支持：
- 邮箱：support@zhixuewujie.com
- 文档：查看项目 README.md

---

Copyright © 2025 上海智学无界教育科技有限公司


