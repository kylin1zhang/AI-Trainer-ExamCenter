package com.zhixuewujie.examcenter.service;

import com.zhixuewujie.examcenter.entity.Exam;
import com.zhixuewujie.examcenter.vo.QuestionVO;

import java.util.List;
import java.util.Map;

/**
 * 考试服务接口
 *
 * @author 智学无界
 * @since 2025-11-24
 */
public interface IExamService {

    /**
     * 创建模拟考试
     */
    Exam createExam(Long userId, Long bankId, Integer judgeCount, Integer singleCount, Integer multipleCount);

    /**
     * 获取考试详情
     */
    Exam getExamDetail(Long examId, Long userId);

    /**
     * 提交考试
     */
    Map<String, Object> submitExam(Long examId, Long userId, Map<Long, String> answers);

    /**
     * 获取考试记录
     */
    List<Exam> getExamRecords(Long userId, Long bankId);

    /**
     * 获取考试题目列表
     */
    List<QuestionVO> getExamQuestions(Long examId, Long userId);
}


