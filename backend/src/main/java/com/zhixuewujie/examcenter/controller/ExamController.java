package com.zhixuewujie.examcenter.controller;

import com.zhixuewujie.examcenter.common.Result;
import com.zhixuewujie.examcenter.entity.Exam;
import com.zhixuewujie.examcenter.service.IExamService;
import com.zhixuewujie.examcenter.util.JwtUtil;
import com.zhixuewujie.examcenter.vo.QuestionVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 考试（模拟考试）控制器
 * 
 * 功能：
 * - 创建模拟考试
 * - 获取考试题目
 * - 提交考试
 * - 查看考试结果
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private IExamService examService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 创建模拟考试
     * POST /api/exams
     */
    @PostMapping
    public Result<Exam> createExam(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        try {
            Long userId = getUserId(request);
            if (userId == null) {
                return Result.error("用户未登录");
            }

            // 安全地获取 bankId
            Object bankIdObj = params.get("bankId");
            if (bankIdObj == null) {
                return Result.error("题库ID不能为空");
            }
            
            Long bankId;
            try {
                if (bankIdObj instanceof Number) {
                    bankId = ((Number) bankIdObj).longValue();
                } else {
                    String bankIdStr = bankIdObj.toString().trim();
                    if (bankIdStr.isEmpty() || "NaN".equals(bankIdStr) || "null".equals(bankIdStr)) {
                        return Result.error("题库ID无效");
                    }
                    bankId = Long.parseLong(bankIdStr);
                }
            } catch (NumberFormatException e) {
                return Result.error("题库ID格式错误: " + bankIdObj);
            }

            if (bankId <= 0) {
                return Result.error("题库ID必须大于0");
            }

            Integer judgeCount = params.get("judgeCount") != null ? 
                Integer.valueOf(params.get("judgeCount").toString()) : null;
            Integer singleCount = params.get("singleCount") != null ? 
                Integer.valueOf(params.get("singleCount").toString()) : null;
            Integer multipleCount = params.get("multipleCount") != null ? 
                Integer.valueOf(params.get("multipleCount").toString()) : null;

            Exam exam = examService.createExam(userId, bankId, judgeCount, singleCount, multipleCount);
            return Result.success(exam);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取考试详情
     * GET /api/exams/{examId}
     */
    @GetMapping("/{examId}")
    public Result<Exam> getExamDetail(@PathVariable Long examId, HttpServletRequest request) {
        try {
            Long userId = getUserId(request);
            Exam exam = examService.getExamDetail(examId, userId);
            return Result.success(exam);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取考试题目列表
     * GET /api/exams/{examId}/questions
     */
    @GetMapping("/{examId}/questions")
    public Result<List<QuestionVO>> getExamQuestions(@PathVariable Long examId, HttpServletRequest request) {
        try {
            Long userId = getUserId(request);
            List<QuestionVO> questions = examService.getExamQuestions(examId, userId);
            return Result.success(questions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 提交考试
     * POST /api/exams/{examId}/submit
     */
    @PostMapping("/{examId}/submit")
    public Result<Map<String, Object>> submitExam(
            @PathVariable Long examId,
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        try {
            Long userId = getUserId(request);
            @SuppressWarnings("unchecked")
            Map<String, String> answersMap = (Map<String, String>) params.get("answers");
            
            Map<Long, String> answers = new java.util.HashMap<>();
            for (Map.Entry<String, String> entry : answersMap.entrySet()) {
                answers.put(Long.valueOf(entry.getKey()), entry.getValue());
            }

            Map<String, Object> result = examService.submitExam(examId, userId, answers);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取考试记录
     * GET /api/exams/records?bankId={bankId}
     */
    @GetMapping("/records")
    public Result<List<Exam>> getExamRecords(
            @RequestParam(required = false) Long bankId,
            HttpServletRequest request) {
        try {
            Long userId = getUserId(request);
            List<Exam> exams = examService.getExamRecords(userId, bankId);
            return Result.success(exams);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    private Long getUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtUtil.getUserIdFromToken(token);
        }
        return null;
    }
}

