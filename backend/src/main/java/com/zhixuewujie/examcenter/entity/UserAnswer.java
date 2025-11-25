package com.zhixuewujie.examcenter.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户答题记录
 * 
 * 记录用户每次答题的情况，用于：
 * - 错题统计
 * - 练习历史
 * - 易错题分析
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@Data
@TableName("tb_user_answer")
public class UserAnswer {

    /**
     * 记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 题目ID
     */
    private Long questionId;

    /**
     * 题库ID
     */
    private Long bankId;

    /**
     * 用户答案
     */
    private String userAnswer;

    /**
     * 是否正确：1-正确，0-错误
     * 
     * 重要：任何错误都需要记录（用于"我的错题"和"易错题"统计）
     */
    private Integer isCorrect;

    /**
     * 答题模式：ANSWER-答题模式，RECITE-背题模式
     */
    private String practiceMode;

    /**
     * 答题耗时（秒）
     */
    private Integer timeCost;

    /**
     * 来源类型：PRACTICE-练习，EXAM-考试
     */
    private String sourceType;

    /**
     * 来源ID：练习记录ID 或 考试ID
     */
    private Long sourceId;

    /**
     * 答题时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}

