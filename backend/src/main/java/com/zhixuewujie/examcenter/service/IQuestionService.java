package com.zhixuewujie.examcenter.service;

import com.zhixuewujie.examcenter.vo.QuestionVO;

import java.util.List;

/**
 * 题目服务接口
 *
 * @author 智学无界
 * @since 2025-11-24
 */
public interface IQuestionService {

    /**
     * 顺序练习 - 获取题目列表
     */
    List<QuestionVO> getSequenceQuestions(Long bankId, Integer page, Integer size);

    /**
     * 随机练习 - 获取随机题目
     */
    List<QuestionVO> getRandomQuestions(Long bankId, Integer count);

    /**
     * 题型练习 - 按题型获取题目
     */
    List<QuestionVO> getQuestionsByType(Long bankId, String type, Integer page, Integer size);

    /**
     * 获取题目详情
     */
    QuestionVO getQuestionDetail(Long id, Long userId);

}

