package com.zhixuewujie.examcenter.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 题目实体
 * 
 * 对应 data.json 数据结构：
 * {
 *   "id": 1,
 *   "type": "judge",  // judge-判断题, single-单选题, multiple-多选题
 *   "question": "题干内容",
 *   "option_A": "选项A",
 *   "option_B": "选项B",
 *   "option_C": "选项C",  // 判断题时为 null
 *   "option_D": "选项D",
 *   "option_E": "选项E",
 *   "answer": "A",  // 单选/判断：A  多选：A,B,C
 *   "explanation": "详细解析"
 * }
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@Data
@TableName("tb_question")
public class Question {

    /**
     * 题目ID（对应 data.json 中的 id）
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 所属题库ID
     */
    private Long bankId;

    /**
     * 题目类型（对应 data.json 中的 type）
     * judge - 判断题
     * single - 单选题
     * multiple - 多选题
     */
    private String type;

    /**
     * 题干内容（对应 data.json 中的 question）
     */
    private String question;

    /**
     * 选项A（对应 data.json 中的 option_A）
     */
    private String optionA;

    /**
     * 选项B（对应 data.json 中的 option_B）
     */
    private String optionB;

    /**
     * 选项C（对应 data.json 中的 option_C）
     */
    private String optionC;

    /**
     * 选项D（对应 data.json 中的 option_D）
     */
    private String optionD;

    /**
     * 选项E（对应 data.json 中的 option_E）
     */
    private String optionE;

    /**
     * 正确答案（对应 data.json 中的 answer）
     * 判断题：A 或 B
     * 单选题：A、B、C、D、E 之一
     * 多选题：A,B,C 等（逗号分隔）
     */
    private String answer;

    /**
     * 答案解析（对应 data.json 中的 explanation）
     */
    private String explanation;

    /**
     * 题目序号（用于顺序练习）
     */
    private Integer sequenceNumber;

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

