package com.zhixuewujie.examcenter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码测试工具
 * 用于生成和验证 BCrypt 密码
 */
public class PasswordTest {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 要加密的原始密码
        String rawPassword = "123456";
        
        // 生成新的密码哈希
        String hashedPassword = encoder.encode(rawPassword);
        System.out.println("原始密码: " + rawPassword);
        System.out.println("加密后的密码: " + hashedPassword);
        System.out.println();
        
        // 验证密码
        System.out.println("验证密码是否匹配:");
        boolean matches = encoder.matches(rawPassword, hashedPassword);
        System.out.println("匹配结果: " + matches);
        System.out.println();
        
        // 测试 SQL 文件中的密码哈希
        String sqlHash = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pJ0C";
        System.out.println("测试 init_user.sql 中的密码哈希:");
        System.out.println("密码哈希: " + sqlHash);
        boolean sqlMatches = encoder.matches(rawPassword, sqlHash);
        System.out.println("与 '123456' 匹配: " + sqlMatches);
        System.out.println();
        
        // 生成新的 SQL INSERT 语句
        System.out.println("=== 新的 SQL INSERT 语句 ===");
        String newHash1 = encoder.encode("123456");
        String newHash2 = encoder.encode("123456");
        System.out.println("INSERT INTO tb_user (username, password, nickname, status) VALUES");
        System.out.println("('admin', '" + newHash1 + "', '管理员', 0),");
        System.out.println("('test', '" + newHash2 + "', '测试用户', 0)");
        System.out.println("ON DUPLICATE KEY UPDATE password=VALUES(password);");
    }
}
