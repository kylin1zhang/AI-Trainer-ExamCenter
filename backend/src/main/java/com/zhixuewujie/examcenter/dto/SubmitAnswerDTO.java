package com.zhixuewujie.examcenter.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

/**
 * 提交答案DTO
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@Data
public class SubmitAnswerDTO {

    @NotNull(message = "题目ID不能为空")
    private Long questionId;

    private String userAnswer;

    private Integer timeCost;

    private String practiceMode; // ANSWER 或 RECITE

    private String sourceType; // PRACTICE 或 EXAM

    private Long sourceId;

}

