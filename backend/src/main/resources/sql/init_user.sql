-- 初始化测试用户
-- 密码：123456（BCrypt加密后的值）

-- 注意：实际使用时，密码应该通过后端注册接口生成
-- 这里提供一个测试用户，密码是 "123456" 的BCrypt加密值

INSERT INTO tb_user (username, password, nickname, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pJ0C', '管理员', 0),
('test', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pJ0C', '测试用户', 0)
ON DUPLICATE KEY UPDATE username=username;

