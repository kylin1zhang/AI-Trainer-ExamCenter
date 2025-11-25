package com.zhixuewujie.examcenter.vo;

import lombok.Data;

/**
 * 题目视图对象
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@Data
public class QuestionVO {

    private Long id;
    private Long bankId;
    private String type; // judge, single, multiple
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String optionE;
    private String answer;
    private String explanation;
    private Integer sequenceNumber;
    
    // 扩展字段
    private String userAnswer;
    private Boolean isCorrect;
    private Boolean isFavorite;
    
    // 笔记相关字段
    private Long noteId;
    private String noteContent;

}

