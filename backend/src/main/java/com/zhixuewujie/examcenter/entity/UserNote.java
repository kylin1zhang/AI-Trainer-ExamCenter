package com.zhixuewujie.examcenter.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户笔记
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@Data
@TableName("tb_user_note")
public class UserNote {

    /**
     * 笔记ID
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
     * 笔记内容
     */
    private String content;

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

}


