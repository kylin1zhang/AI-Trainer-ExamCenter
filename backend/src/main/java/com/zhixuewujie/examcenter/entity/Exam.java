package com.zhixuewujie.examcenter.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 模拟考试记录
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@Data
@TableName("tb_exam")
public class Exam {

    /**
     * 考试ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 题库ID
     */
    private Long bankId;

    /**
     * 考试标题
     */
    private String title;

    /**
     * 题目ID列表（JSON 格式）
     */
    private String questionIds;

    /**
     * 考试时长（分钟）
     * 人工智能训练师三级：90分钟
     */
    private Integer duration;

    /**
     * 判断题数量（人工智能训练师三级：40题）
     */
    private Integer judgeCount;

    /**
     * 单选题数量（人工智能训练师三级：140题）
     */
    private Integer singleCount;

    /**
     * 多选题数量（人工智能训练师三级：10题）
     */
    private Integer multipleCount;

    /**
     * 得分
     * 判断题：40题 × 0.5分 = 20分
     * 单选题：140题 × 0.5分 = 70分
     * 多选题：10题 × 1分 = 10分
     * 总分：100分
     */
    private Double score;

    /**
     * 总分（固定100分）
     */
    private Double totalScore;

    /**
     * 是否及格（≥60分为及格）
     */
    private Integer isPassed;

    /**
     * 正确题数
     */
    private Integer correctCount;

    /**
     * 错误题数
     */
    private Integer wrongCount;

    /**
     * 正确率（0-1之间的小数）
     */
    private Double accuracy;

    /**
     * 考试状态：ONGOING-进行中，FINISHED-已完成
     */
    private String status;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}

