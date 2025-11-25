package com.zhixuewujie.examcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 上海智学无界教育科技有限公司
 * AI 训练师考试中心 - 主启动类
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@SpringBootApplication
public class ExamCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExamCenterApplication.class, args);
        System.out.println("====================================");
        System.out.println("  AI Trainer Exam Center Started!");
        System.out.println("  上海智学无界教育科技有限公司");
        System.out.println("====================================");
    }

}


