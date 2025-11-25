package com.zhixuewujie.examcenter.controller;

import com.zhixuewujie.examcenter.common.Result;
import com.zhixuewujie.examcenter.service.IQuestionService;
import com.zhixuewujie.examcenter.util.JwtUtil;
import com.zhixuewujie.examcenter.vo.QuestionVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 题目控制器
 * 
 * 功能：
 * - 顺序练习
 * - 随机练习
 * - 题型练习（单选、多选、判断等）
 * - 获取题目详情
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 顺序练习 - 获取题目列表
     * GET /api/questions/bank/{bankId}/sequential?page={page}&size={size}
     */
    @GetMapping("/bank/{bankId}/sequential")
    public Result<List<QuestionVO>> getSequentialQuestions(
            @PathVariable Long bankId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        try {
            List<QuestionVO> questions = questionService.getSequenceQuestions(bankId, page, size);
            return Result.success(questions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 顺序练习 - 旧版URL兼容
     * GET /api/questions/sequence?bankId={bankId}&page={page}&size={size}
     */
    @GetMapping("/sequence")
    public Result<List<QuestionVO>> getSequenceQuestions(
            @RequestParam Long bankId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        try {
            List<QuestionVO> questions = questionService.getSequenceQuestions(bankId, page, size);
            return Result.success(questions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 随机练习 - 获取随机题目
     * GET /api/questions/bank/{bankId}/random?count={count}
     */
    @GetMapping("/bank/{bankId}/random")
    public Result<List<QuestionVO>> getRandomQuestionsRestful(
            @PathVariable Long bankId,
            @RequestParam(required = false, defaultValue = "20") Integer count) {
        try {
            List<QuestionVO> questions = questionService.getRandomQuestions(bankId, count);
            return Result.success(questions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 随机练习 - 旧版URL兼容
     * GET /api/questions/random?bankId={bankId}&count={count}
     */
    @GetMapping("/random")
    public Result<List<QuestionVO>> getRandomQuestions(
            @RequestParam Long bankId,
            @RequestParam(required = false, defaultValue = "20") Integer count) {
        try {
            List<QuestionVO> questions = questionService.getRandomQuestions(bankId, count);
            return Result.success(questions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 题型练习 - 按题型获取题目
     * GET /api/questions/bank/{bankId}/type/{type}?page={page}&size={size}
     */
    @GetMapping("/bank/{bankId}/type/{type}")
    public Result<List<QuestionVO>> getQuestionsByTypeRestful(
            @PathVariable Long bankId,
            @PathVariable String type,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        try {
            List<QuestionVO> questions = questionService.getQuestionsByType(bankId, type, page, size);
            return Result.success(questions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 题型练习 - 旧版URL兼容
     * GET /api/questions/by-type?bankId={bankId}&type={type}&page={page}
     */
    @GetMapping("/by-type")
    public Result<List<QuestionVO>> getQuestionsByType(
            @RequestParam Long bankId,
            @RequestParam String type,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        try {
            List<QuestionVO> questions = questionService.getQuestionsByType(bankId, type, page, size);
            return Result.success(questions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取题目详情
     * GET /api/questions/{id}
     */
    @GetMapping("/{id}")
    public Result<QuestionVO> getQuestionDetail(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long userId = getUserId(request);
            QuestionVO question = questionService.getQuestionDetail(id, userId);
            return Result.success(question);
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

