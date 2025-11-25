package com.zhixuewujie.examcenter.service;

import com.zhixuewujie.examcenter.vo.QuestionBankVO;

import java.util.List;

/**
 * 题库服务接口
 *
 * @author 智学无界
 * @since 2025-11-24
 */
public interface IQuestionBankService {

    /**
     * 获取题库列表
     */
    List<QuestionBankVO> getQuestionBanks(Long userId);

    /**
     * 获取题库详情
     */
    QuestionBankVO getQuestionBankDetail(Long id);

}

