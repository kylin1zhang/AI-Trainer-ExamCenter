package com.zhixuewujie.examcenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhixuewujie.examcenter.entity.QuestionBank;
import com.zhixuewujie.examcenter.mapper.QuestionBankMapper;
import com.zhixuewujie.examcenter.service.IQuestionBankService;
import com.zhixuewujie.examcenter.vo.QuestionBankVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 题库服务实现
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@Service
public class QuestionBankServiceImpl implements IQuestionBankService {

    @Autowired
    private QuestionBankMapper questionBankMapper;

    @Override
    public List<QuestionBankVO> getQuestionBanks(Long userId) {
        LambdaQueryWrapper<QuestionBank> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QuestionBank::getStatus, 0);
        wrapper.orderByAsc(QuestionBank::getSortOrder);
        
        List<QuestionBank> banks = questionBankMapper.selectList(wrapper);
        
        return banks.stream().map(bank -> {
            QuestionBankVO vo = new QuestionBankVO();
            BeanUtils.copyProperties(bank, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public QuestionBankVO getQuestionBankDetail(Long id) {
        QuestionBank bank = questionBankMapper.selectById(id);
        if (bank == null) {
            throw new RuntimeException("题库不存在");
        }

        QuestionBankVO vo = new QuestionBankVO();
        BeanUtils.copyProperties(bank, vo);
        return vo;
    }

}

