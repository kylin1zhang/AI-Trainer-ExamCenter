package com.zhixuewujie.examcenter.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 题库实体
 * 
 * 当前题库：人工智能训练师（三级/高级）
 * 未来可扩展：其他职业资格考试题库
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@Data
@TableName("tb_question_bank")
public class QuestionBank {

    /**
     * 题库ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 题库名称，如：人工智能训练师（三级/高级）
     */
    private String name;

    /**
     * 题库描述
     */
    private String description;

    /**
     * 题库封面图
     */
    private String coverImage;

    /**
     * 题目总数
     */
    private Integer questionCount;

    /**
     * 排序字段
     */
    private Integer sortOrder;

    /**
     * 状态：0-正常，1-禁用
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;

}


