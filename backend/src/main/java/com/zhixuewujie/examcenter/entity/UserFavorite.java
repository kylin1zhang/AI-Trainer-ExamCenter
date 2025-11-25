package com.zhixuewujie.examcenter.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户收藏题目
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@Data
@TableName("tb_user_favorite")
public class UserFavorite {

    /**
     * 收藏ID
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
     * 收藏时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}


