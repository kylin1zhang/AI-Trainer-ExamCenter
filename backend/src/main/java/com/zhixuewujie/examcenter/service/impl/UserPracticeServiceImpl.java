package com.zhixuewujie.examcenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhixuewujie.examcenter.common.Constants;
import com.zhixuewujie.examcenter.dto.SubmitAnswerDTO;
import com.zhixuewujie.examcenter.entity.Question;
import com.zhixuewujie.examcenter.entity.UserAnswer;
import com.zhixuewujie.examcenter.entity.UserFavorite;
import com.zhixuewujie.examcenter.entity.UserNote;
import com.zhixuewujie.examcenter.mapper.*;
import com.zhixuewujie.examcenter.service.IUserPracticeService;
import com.zhixuewujie.examcenter.vo.QuestionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户练习服务实现
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@Service
public class UserPracticeServiceImpl implements IUserPracticeService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserAnswerMapper userAnswerMapper;

    @Autowired
    private UserFavoriteMapper userFavoriteMapper;

    @Autowired
    private UserNoteMapper userNoteMapper;

    @Override
    public Map<String, Object> submitAnswer(SubmitAnswerDTO dto, Long userId) {
        // 获取题目
        Question question = questionMapper.selectById(dto.getQuestionId());
        if (question == null) {
            throw new RuntimeException("题目不存在");
        }

        // 判断答案是否正确
        boolean isCorrect = checkAnswer(question.getAnswer(), dto.getUserAnswer());

        // 记录答题记录（任何错误都记录）
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setUserId(userId);
        userAnswer.setQuestionId(dto.getQuestionId());
        userAnswer.setBankId(question.getBankId());
        userAnswer.setUserAnswer(dto.getUserAnswer());
        userAnswer.setIsCorrect(isCorrect ? Constants.IsCorrect.YES : Constants.IsCorrect.NO);
        userAnswer.setPracticeMode(dto.getPracticeMode());
        userAnswer.setTimeCost(dto.getTimeCost());
        userAnswer.setSourceType(dto.getSourceType());
        userAnswer.setSourceId(dto.getSourceId());
        userAnswerMapper.insert(userAnswer);

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("isCorrect", isCorrect);
        result.put("correctAnswer", question.getAnswer());
        result.put("explanation", question.getExplanation());
        return result;
    }

    @Override
    public List<QuestionVO> getWrongQuestions(Long userId, Long bankId) {
        List<Map<String, Object>> wrongList = userAnswerMapper.selectWrongQuestions(userId, bankId);
        
        return wrongList.stream().map(item -> {
            Long questionId = Long.valueOf(item.get("question_id").toString());
            Question question = questionMapper.selectById(questionId);
            if (question != null) {
                QuestionVO vo = new QuestionVO();
                BeanUtils.copyProperties(question, vo);
                return vo;
            }
            return null;
        }).filter(vo -> vo != null).collect(Collectors.toList());
    }

    @Override
    public List<QuestionVO> getDifficultQuestions(Long userId, Long bankId) {
        List<Map<String, Object>> difficultList = userAnswerMapper.selectDifficultQuestions(userId, bankId);
        
        return difficultList.stream().map(item -> {
            Long questionId = Long.valueOf(item.get("question_id").toString());
            Question question = questionMapper.selectById(questionId);
            if (question != null) {
                QuestionVO vo = new QuestionVO();
                BeanUtils.copyProperties(question, vo);
                return vo;
            }
            return null;
        }).filter(vo -> vo != null).collect(Collectors.toList());
    }

    @Override
    public void favoriteQuestion(Long userId, Long questionId) {
        Question question = questionMapper.selectById(questionId);
        if (question == null) {
            throw new RuntimeException("题目不存在");
        }

        // 检查是否已收藏
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, userId);
        wrapper.eq(UserFavorite::getQuestionId, questionId);
        UserFavorite exist = userFavoriteMapper.selectOne(wrapper);
        
        if (exist == null) {
            UserFavorite favorite = new UserFavorite();
            favorite.setUserId(userId);
            favorite.setQuestionId(questionId);
            favorite.setBankId(question.getBankId());
            userFavoriteMapper.insert(favorite);
        }
    }

    @Override
    public void unfavoriteQuestion(Long userId, Long questionId) {
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, userId);
        wrapper.eq(UserFavorite::getQuestionId, questionId);
        userFavoriteMapper.delete(wrapper);
    }

    @Override
    public List<QuestionVO> getFavoriteQuestions(Long userId, Long bankId) {
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, userId);
        if (bankId != null) {
            wrapper.eq(UserFavorite::getBankId, bankId);
        }
        
        List<UserFavorite> favorites = userFavoriteMapper.selectList(wrapper);
        
        return favorites.stream().map(favorite -> {
            Question question = questionMapper.selectById(favorite.getQuestionId());
            if (question != null) {
                QuestionVO vo = new QuestionVO();
                BeanUtils.copyProperties(question, vo);
                vo.setIsFavorite(true);
                return vo;
            }
            return null;
        }).filter(vo -> vo != null).collect(Collectors.toList());
    }

    @Override
    public void addNote(Long userId, Long questionId, String content) {
        // 查询题目信息获取bankId
        Question question = questionMapper.selectById(questionId);
        if (question == null) {
            throw new RuntimeException("题目不存在");
        }

        // 检查是否已有笔记
        LambdaQueryWrapper<UserNote> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserNote::getUserId, userId);
        wrapper.eq(UserNote::getQuestionId, questionId);
        UserNote existingNote = userNoteMapper.selectOne(wrapper);

        if (existingNote != null) {
            // 更新现有笔记
            existingNote.setContent(content);
            userNoteMapper.updateById(existingNote);
        } else {
            // 创建新笔记
            UserNote note = new UserNote();
            note.setUserId(userId);
            note.setQuestionId(questionId);
            note.setBankId(question.getBankId());
            note.setContent(content);
            userNoteMapper.insert(note);
        }
    }

    @Override
    public List<QuestionVO> getNotesWithQuestions(Long userId, Long bankId) {
        // 查询该用户在该题库的所有笔记
        LambdaQueryWrapper<UserNote> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserNote::getUserId, userId);
        wrapper.eq(UserNote::getBankId, bankId);
        wrapper.orderByDesc(UserNote::getUpdateTime);
        List<UserNote> notes = userNoteMapper.selectList(wrapper);

        // 获取题目信息并组装VO
        return notes.stream().map(note -> {
            Question question = questionMapper.selectById(note.getQuestionId());
            if (question != null) {
                QuestionVO vo = new QuestionVO();
                BeanUtils.copyProperties(question, vo);
                vo.setNoteContent(note.getContent());
                vo.setNoteId(note.getId());
                return vo;
            }
            return null;
        }).filter(vo -> vo != null).collect(Collectors.toList());
    }

    /**
     * 检查答案是否正确
     */
    private boolean checkAnswer(String correctAnswer, String userAnswer) {
        if (correctAnswer == null || userAnswer == null) {
            return false;
        }
        
        // 处理多选题（答案可能是 "A,B,C" 格式）
        String correct = correctAnswer.trim().toUpperCase();
        String user = userAnswer.trim().toUpperCase();
        
        // 如果包含逗号，说明是多选题，需要排序后比较
        if (correct.contains(",")) {
            String[] correctArr = correct.split(",");
            String[] userArr = user.split(",");
            if (correctArr.length != userArr.length) {
                return false;
            }
            // 简单比较（实际应该排序后比较）
            java.util.Arrays.sort(correctArr);
            java.util.Arrays.sort(userArr);
            return java.util.Arrays.equals(correctArr, userArr);
        }
        
        // 单选题或判断题
        return correct.equals(user);
    }

}

