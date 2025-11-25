package com.zhixuewujie.examcenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhixuewujie.examcenter.common.Constants;
import com.zhixuewujie.examcenter.common.ExamConfig;
import com.zhixuewujie.examcenter.entity.Exam;
import com.zhixuewujie.examcenter.entity.Question;
import com.zhixuewujie.examcenter.entity.UserAnswer;
import com.zhixuewujie.examcenter.mapper.ExamMapper;
import com.zhixuewujie.examcenter.mapper.QuestionMapper;
import com.zhixuewujie.examcenter.mapper.UserAnswerMapper;
import com.zhixuewujie.examcenter.service.IExamService;
import com.zhixuewujie.examcenter.vo.QuestionVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 考试服务实现
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@Service
public class ExamServiceImpl implements IExamService {

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserAnswerMapper userAnswerMapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public Exam createExam(Long userId, Long bankId, Integer judgeCount, Integer singleCount, Integer multipleCount) {
        // 使用默认值
        int judge = judgeCount != null ? judgeCount : ExamConfig.JUDGE_COUNT;
        int single = singleCount != null ? singleCount : ExamConfig.SINGLE_COUNT;
        int multiple = multipleCount != null ? multipleCount : ExamConfig.MULTIPLE_COUNT;

        // 随机抽取题目
        List<Long> questionIds = new ArrayList<>();
        
        // 抽取判断题
        List<Question> judgeQuestions = questionMapper.selectRandomQuestions(bankId, "judge", judge);
        questionIds.addAll(judgeQuestions.stream().map(Question::getId).collect(Collectors.toList()));
        
        // 抽取单选题
        List<Question> singleQuestions = questionMapper.selectRandomQuestions(bankId, "single", single);
        questionIds.addAll(singleQuestions.stream().map(Question::getId).collect(Collectors.toList()));
        
        // 抽取多选题
        List<Question> multipleQuestions = questionMapper.selectRandomQuestions(bankId, "multiple", multiple);
        questionIds.addAll(multipleQuestions.stream().map(Question::getId).collect(Collectors.toList()));

        // 打乱顺序
        Collections.shuffle(questionIds);

        // 创建考试记录
        Exam exam = new Exam();
        exam.setUserId(userId);
        exam.setBankId(bankId);
        exam.setTitle("模拟考试 " + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        try {
            exam.setQuestionIds(objectMapper.writeValueAsString(questionIds));
        } catch (Exception e) {
            throw new RuntimeException("创建考试失败");
        }
        exam.setDuration(ExamConfig.DURATION);
        exam.setJudgeCount(judge);
        exam.setSingleCount(single);
        exam.setMultipleCount(multiple);
        exam.setTotalScore(ExamConfig.TOTAL_SCORE);
        exam.setStatus(Constants.ExamStatus.ONGOING);
        exam.setStartTime(LocalDateTime.now());

        examMapper.insert(exam);
        return exam;
    }

    @Override
    public Exam getExamDetail(Long examId, Long userId) {
        Exam exam = examMapper.selectById(examId);
        if (exam == null) {
            throw new RuntimeException("考试不存在");
        }
        if (!exam.getUserId().equals(userId)) {
            throw new RuntimeException("无权访问此考试");
        }
        return exam;
    }

    @Override
    @Transactional
    public Map<String, Object> submitExam(Long examId, Long userId, Map<Long, String> answers) {
        Exam exam = getExamDetail(examId, userId);
        if (!Constants.ExamStatus.ONGOING.equals(exam.getStatus())) {
            throw new RuntimeException("考试已结束");
        }

        // 获取题目列表
        List<Long> questionIds;
        try {
            questionIds = objectMapper.readValue(exam.getQuestionIds(), new TypeReference<List<Long>>() {});
        } catch (Exception e) {
            throw new RuntimeException("获取题目列表失败");
        }

        // 统计各题型正确数
        int judgeCorrect = 0;
        int singleCorrect = 0;
        int multipleCorrect = 0;
        int totalCorrect = 0;
        int totalWrong = 0;
        List<QuestionVO> wrongQuestions = new ArrayList<>();

        // 遍历所有题目
        for (Long questionId : questionIds) {
            Question question = questionMapper.selectById(questionId);
            if (question == null) continue;

            String userAnswer = answers.get(questionId);
            boolean isCorrect = checkAnswer(question.getAnswer(), userAnswer != null ? userAnswer : "");

            // 记录答题记录
            UserAnswer userAnswerRecord = new UserAnswer();
            userAnswerRecord.setUserId(userId);
            userAnswerRecord.setQuestionId(questionId);
            userAnswerRecord.setBankId(exam.getBankId());
            userAnswerRecord.setUserAnswer(userAnswer);
            userAnswerRecord.setIsCorrect(isCorrect ? Constants.IsCorrect.YES : Constants.IsCorrect.NO);
            userAnswerRecord.setPracticeMode(Constants.PracticeMode.ANSWER);
            userAnswerRecord.setSourceType(Constants.SourceType.EXAM);
            userAnswerRecord.setSourceId(examId);
            userAnswerMapper.insert(userAnswerRecord);

            // 统计
            if (isCorrect) {
                totalCorrect++;
                switch (question.getType()) {
                    case "judge": judgeCorrect++; break;
                    case "single": singleCorrect++; break;
                    case "multiple": multipleCorrect++; break;
                }
            } else {
                totalWrong++;
                QuestionVO vo = new QuestionVO();
                BeanUtils.copyProperties(question, vo);
                vo.setUserAnswer(userAnswer);
                wrongQuestions.add(vo);
            }
        }

        // 计算分数
        double score = ExamConfig.calculateScore(judgeCorrect, singleCorrect, multipleCorrect);
        boolean isPassed = ExamConfig.isPassed(score);
        double accuracy = questionIds.size() > 0 ? (double) totalCorrect / questionIds.size() : 0;

        // 更新考试记录
        exam.setScore(score);
        exam.setIsPassed(isPassed ? Constants.IsPassed.YES : Constants.IsPassed.NO);
        exam.setCorrectCount(totalCorrect);
        exam.setWrongCount(totalWrong);
        exam.setAccuracy(accuracy);
        exam.setStatus(Constants.ExamStatus.FINISHED);
        exam.setSubmitTime(LocalDateTime.now());
        examMapper.updateById(exam);

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("examId", examId);
        result.put("score", score);
        result.put("totalScore", ExamConfig.TOTAL_SCORE);
        result.put("isPassed", isPassed);
        result.put("accuracy", accuracy);
        result.put("correctCount", totalCorrect);
        result.put("wrongCount", totalWrong);
        result.put("wrongQuestions", wrongQuestions);
        return result;
    }

    @Override
    public List<Exam> getExamRecords(Long userId, Long bankId) {
        LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Exam::getUserId, userId);
        if (bankId != null) {
            wrapper.eq(Exam::getBankId, bankId);
        }
        wrapper.orderByDesc(Exam::getCreateTime);
        return examMapper.selectList(wrapper);
    }

    @Override
    public List<QuestionVO> getExamQuestions(Long examId, Long userId) {
        Exam exam = getExamDetail(examId, userId);
        
        List<Long> questionIds;
        try {
            questionIds = objectMapper.readValue(exam.getQuestionIds(), new TypeReference<List<Long>>() {});
        } catch (Exception e) {
            throw new RuntimeException("获取题目列表失败");
        }

        return questionIds.stream().map(id -> {
            Question question = questionMapper.selectById(id);
            if (question != null) {
                QuestionVO vo = new QuestionVO();
                BeanUtils.copyProperties(question, vo);
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
        
        String correct = correctAnswer.trim().toUpperCase();
        String user = userAnswer.trim().toUpperCase();
        
        if (correct.contains(",")) {
            String[] correctArr = correct.split(",");
            String[] userArr = user.split(",");
            if (correctArr.length != userArr.length) {
                return false;
            }
            Arrays.sort(correctArr);
            Arrays.sort(userArr);
            return Arrays.equals(correctArr, userArr);
        }
        
        return correct.equals(user);
    }
}


