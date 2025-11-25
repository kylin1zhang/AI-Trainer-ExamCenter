package com.zhixuewujie.examcenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhixuewujie.examcenter.entity.Question;
import com.zhixuewujie.examcenter.mapper.QuestionMapper;
import com.zhixuewujie.examcenter.service.IQuestionService;
import com.zhixuewujie.examcenter.vo.QuestionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目服务实现
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@Service
public class QuestionServiceImpl implements IQuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public List<QuestionVO> getSequenceQuestions(Long bankId, Integer page, Integer size) {
        Page<Question> pageParam = new Page<>(page != null ? page : 1, size != null ? size : 20);
        
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getBankId, bankId);
        wrapper.orderByAsc(Question::getSequenceNumber);
        
        Page<Question> result = questionMapper.selectPage(pageParam, wrapper);
        
        return result.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public List<QuestionVO> getRandomQuestions(Long bankId, Integer count) {
        List<Question> questions = questionMapper.selectRandomQuestions(bankId, null, count != null ? count : 20);
        return questions.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public List<QuestionVO> getQuestionsByType(Long bankId, String type, Integer page, Integer size) {
        Page<Question> pageParam = new Page<>(page != null ? page : 1, size != null ? size : 20);
        
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getBankId, bankId);
        wrapper.eq(Question::getType, type);
        wrapper.orderByAsc(Question::getSequenceNumber);
        
        Page<Question> result = questionMapper.selectPage(pageParam, wrapper);
        
        return result.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public QuestionVO getQuestionDetail(Long id, Long userId) {
        Question question = questionMapper.selectById(id);
        if (question == null) {
            throw new RuntimeException("题目不存在");
        }

        return convertToVO(question);
    }

    private QuestionVO convertToVO(Question question) {
        QuestionVO vo = new QuestionVO();
        BeanUtils.copyProperties(question, vo);
        return vo;
    }

}

