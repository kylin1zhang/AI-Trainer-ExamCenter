package com.zhixuewujie.examcenter.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 题库视图对象
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@Data
public class QuestionBankVO {

    private Long id;
    private String name;
    private String description;
    private String coverImage;
    private Integer questionCount;
    private Integer sortOrder;
    private LocalDateTime createTime;
    
    // 扩展字段
    private LocalDateTime lastPracticeTime; // 最近练习时间

}

