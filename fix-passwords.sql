-- 修复密码问题的 SQL
-- 方案：删除有问题的旧用户，然后通过前端注册功能重新创建

USE exam_center;

-- 删除旧的有问题的用户（如果存在）
DELETE FROM tb_user WHERE username IN ('admin', 'test');

-- 现在请使用前端的注册功能，或者使用 API 创建新用户：
-- 方法1: 前端注册 http://localhost:3000
-- 方法2: 使用 API
--   POST http://localhost:8080/api/auth/register
--   Body: {"username":"admin","password":"123456","confirmPassword":"123456","nickname":"管理员"}

-- 或者使用已创建的测试用户：
--   用户名: testuser / admin2 / test2
--   密码: 123456

