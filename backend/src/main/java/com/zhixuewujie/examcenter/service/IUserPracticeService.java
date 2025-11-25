package com.zhixuewujie.examcenter.service;

import com.zhixuewujie.examcenter.dto.SubmitAnswerDTO;
import com.zhixuewujie.examcenter.vo.QuestionVO;

import java.util.List;
import java.util.Map;

/**
 * 用户练习服务接口
 *
 * @author 智学无界
 * @since 2025-11-24
 */
public interface IUserPracticeService {

    /**
     * 提交答案
     */
    Map<String, Object> submitAnswer(SubmitAnswerDTO dto, Long userId);

    /**
     * 获取我的错题
     */
    List<QuestionVO> getWrongQuestions(Long userId, Long bankId);

    /**
     * 获取易错题（错误次数 >= 2）
     */
    List<QuestionVO> getDifficultQuestions(Long userId, Long bankId);

    /**
     * 收藏题目
     */
    void favoriteQuestion(Long userId, Long questionId);

    /**
     * 取消收藏
     */
    void unfavoriteQuestion(Long userId, Long questionId);

    /**
     * 获取我的收藏
     */
    List<QuestionVO> getFavoriteQuestions(Long userId, Long bankId);

    /**
     * 添加笔记
     */
    void addNote(Long userId, Long questionId, String content);

    /**
     * 获取我的笔记列表（包含题目信息）
     */
    List<QuestionVO> getNotesWithQuestions(Long userId, Long bankId);

}

