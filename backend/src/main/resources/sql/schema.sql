-- ======================================
-- 上海智学无界教育科技有限公司
-- AI 训练师考试中心 - 数据库结构
-- ======================================

CREATE DATABASE IF NOT EXISTS exam_center DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE exam_center;

-- 用户表
CREATE TABLE tb_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名（登录账号）',
    password VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
    nickname VARCHAR(50) COMMENT '昵称',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像URL',
    status INT DEFAULT 0 COMMENT '状态：0-正常，1-禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    INDEX idx_username (username),
    INDEX idx_email (email)
) COMMENT='用户表';

-- 题库表
CREATE TABLE tb_question_bank (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '题库ID',
    name VARCHAR(100) NOT NULL COMMENT '题库名称',
    description TEXT COMMENT '题库描述',
    cover_image VARCHAR(255) COMMENT '题库封面图',
    question_count INT DEFAULT 0 COMMENT '题目总数',
    sort_order INT DEFAULT 0 COMMENT '排序字段',
    status INT DEFAULT 0 COMMENT '状态：0-正常，1-禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_status (status)
) COMMENT='题库表';

-- 题目表（对应 data.json 数据结构）
CREATE TABLE tb_question (
    id BIGINT PRIMARY KEY COMMENT '题目ID（对应data.json中的id）',
    bank_id BIGINT NOT NULL COMMENT '所属题库ID',
    type VARCHAR(20) NOT NULL COMMENT '题目类型：judge-判断题，single-单选题，multiple-多选题',
    question TEXT NOT NULL COMMENT '题干内容',
    option_a VARCHAR(500) COMMENT '选项A',
    option_b VARCHAR(500) COMMENT '选项B',
    option_c VARCHAR(500) COMMENT '选项C',
    option_d VARCHAR(500) COMMENT '选项D',
    option_e VARCHAR(500) COMMENT '选项E',
    answer VARCHAR(50) NOT NULL COMMENT '正确答案（单选/判断：A，多选：A,B,C）',
    explanation TEXT COMMENT '答案解析',
    sequence_number INT COMMENT '题目序号（用于顺序练习）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_bank_id (bank_id),
    INDEX idx_type (type),
    INDEX idx_sequence (bank_id, sequence_number),
    FOREIGN KEY (bank_id) REFERENCES tb_question_bank(id)
) COMMENT='题目表（匹配data.json结构）';

-- 用户答题记录表（重要：任何错误都需要记录）
CREATE TABLE tb_user_answer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    question_id BIGINT NOT NULL COMMENT '题目ID',
    bank_id BIGINT NOT NULL COMMENT '题库ID',
    user_answer VARCHAR(50) COMMENT '用户答案',
    is_correct INT COMMENT '是否正确：1-正确，0-错误（任何错误都记录）',
    practice_mode VARCHAR(20) COMMENT '答题模式：ANSWER-答题模式，RECITE-背题模式',
    time_cost INT COMMENT '答题耗时（秒）',
    source_type VARCHAR(20) COMMENT '来源类型：PRACTICE-练习，EXAM-考试',
    source_id BIGINT COMMENT '来源ID：练习记录ID或考试ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '答题时间',
    INDEX idx_user_question (user_id, question_id),
    INDEX idx_user_bank (user_id, bank_id),
    INDEX idx_is_correct (is_correct),
    INDEX idx_source (source_type, source_id),
    FOREIGN KEY (user_id) REFERENCES tb_user(id),
    FOREIGN KEY (question_id) REFERENCES tb_question(id),
    FOREIGN KEY (bank_id) REFERENCES tb_question_bank(id)
) COMMENT='用户答题记录表（用于错题和易错题统计）';

-- 用户收藏表
CREATE TABLE tb_user_favorite (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    question_id BIGINT NOT NULL COMMENT '题目ID',
    bank_id BIGINT NOT NULL COMMENT '题库ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    UNIQUE KEY uk_user_question (user_id, question_id),
    INDEX idx_user_bank (user_id, bank_id),
    FOREIGN KEY (user_id) REFERENCES tb_user(id),
    FOREIGN KEY (question_id) REFERENCES tb_question(id),
    FOREIGN KEY (bank_id) REFERENCES tb_question_bank(id)
) COMMENT='用户收藏表';

-- 用户笔记表
CREATE TABLE tb_user_note (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '笔记ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    question_id BIGINT NOT NULL COMMENT '题目ID',
    bank_id BIGINT NOT NULL COMMENT '题库ID',
    content TEXT NOT NULL COMMENT '笔记内容',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_question (user_id, question_id),
    INDEX idx_user_bank (user_id, bank_id),
    FOREIGN KEY (user_id) REFERENCES tb_user(id),
    FOREIGN KEY (question_id) REFERENCES tb_question(id),
    FOREIGN KEY (bank_id) REFERENCES tb_question_bank(id)
) COMMENT='用户笔记表';

-- 模拟考试表
-- 人工智能训练师三级考试规则：
-- 判断题：40题 × 0.5分 = 20分
-- 单选题：140题 × 0.5分 = 70分
-- 多选题：10题 × 1分 = 10分
-- 总分：100分，60分及格
-- 考试时长：90分钟
CREATE TABLE tb_exam (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '考试ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    bank_id BIGINT NOT NULL COMMENT '题库ID',
    title VARCHAR(100) COMMENT '考试标题',
    question_ids JSON NOT NULL COMMENT '题目ID列表（JSON格式）',
    duration INT DEFAULT 90 COMMENT '考试时长（分钟，默认90分钟）',
    judge_count INT DEFAULT 40 COMMENT '判断题数量',
    single_count INT DEFAULT 140 COMMENT '单选题数量',
    multiple_count INT DEFAULT 10 COMMENT '多选题数量',
    score DECIMAL(5,2) COMMENT '得分',
    total_score DECIMAL(5,2) DEFAULT 100.00 COMMENT '总分（固定100分）',
    is_passed INT COMMENT '是否及格：1-及格（≥60分），0-不及格',
    correct_count INT COMMENT '正确题数',
    wrong_count INT COMMENT '错误题数',
    accuracy DECIMAL(5,4) COMMENT '正确率（0-1之间）',
    status VARCHAR(20) DEFAULT 'ONGOING' COMMENT '考试状态：ONGOING-进行中，FINISHED-已完成',
    start_time DATETIME COMMENT '开始时间',
    submit_time DATETIME COMMENT '提交时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_bank (user_id, bank_id),
    INDEX idx_status (status),
    INDEX idx_is_passed (is_passed),
    FOREIGN KEY (user_id) REFERENCES tb_user(id),
    FOREIGN KEY (bank_id) REFERENCES tb_question_bank(id)
) COMMENT='模拟考试表';

-- 初始化数据：创建默认题库
INSERT INTO tb_question_bank (name, description, question_count, sort_order) VALUES
('人工智能训练师（三级/高级）', '人工智能训练师职业技能等级认证三级/高级题库', 0, 1);

