package com.zhixuewujie.examcenter.controller;

import com.zhixuewujie.examcenter.common.Result;
import com.zhixuewujie.examcenter.dto.SubmitAnswerDTO;
import com.zhixuewujie.examcenter.service.IUserPracticeService;
import com.zhixuewujie.examcenter.util.JwtUtil;
import com.zhixuewujie.examcenter.vo.QuestionVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户练习记录控制器
 * 
 * 功能：
 * - 我的错题
 * - 我的收藏
 * - 我的笔记
 * - 易错题（错误次数 ≥ 2 次）
 * - 提交答案
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@RestController
@RequestMapping("/api/user-practice")
public class UserPracticeController {

    @Autowired
    private IUserPracticeService userPracticeService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 提交答案
     * POST /api/user-practice/submit
     */
    @PostMapping("/submit")
    public Result<Map<String, Object>> submitAnswer(@RequestBody SubmitAnswerDTO dto, HttpServletRequest request) {
        try {
            Long userId = getUserId(request);
            Map<String, Object> result = userPracticeService.submitAnswer(dto, userId);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取我的错题
     * GET /api/user-practice/wrong-questions?bankId={bankId}
     */
    @GetMapping("/wrong-questions")
    public Result<List<QuestionVO>> getWrongQuestions(@RequestParam Long bankId, HttpServletRequest request) {
        try {
            Long userId = getUserId(request);
            List<QuestionVO> questions = userPracticeService.getWrongQuestions(userId, bankId);
            return Result.success(questions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取易错题（错误次数 ≥ 2 次）
     * GET /api/user-practice/difficult-questions?bankId={bankId}
     */
    @GetMapping("/difficult-questions")
    public Result<List<QuestionVO>> getDifficultQuestions(@RequestParam Long bankId, HttpServletRequest request) {
        try {
            Long userId = getUserId(request);
            List<QuestionVO> questions = userPracticeService.getDifficultQuestions(userId, bankId);
            return Result.success(questions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 收藏题目
     * POST /api/user-practice/favorites/{questionId}
     */
    @PostMapping("/favorites/{questionId}")
    public Result<?> favoriteQuestion(@PathVariable Long questionId, HttpServletRequest request) {
        try {
            Long userId = getUserId(request);
            userPracticeService.favoriteQuestion(userId, questionId);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 取消收藏
     * DELETE /api/user-practice/favorites/{questionId}
     */
    @DeleteMapping("/favorites/{questionId}")
    public Result<?> unfavoriteQuestion(@PathVariable Long questionId, HttpServletRequest request) {
        try {
            Long userId = getUserId(request);
            userPracticeService.unfavoriteQuestion(userId, questionId);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取我的收藏
     * GET /api/user-practice/favorites?bankId={bankId}
     */
    @GetMapping("/favorites")
    public Result<List<QuestionVO>> getFavoriteQuestions(@RequestParam Long bankId, HttpServletRequest request) {
        try {
            Long userId = getUserId(request);
            List<QuestionVO> questions = userPracticeService.getFavoriteQuestions(userId, bankId);
            return Result.success(questions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 添加笔记
     * POST /api/user-practice/notes
     */
    @PostMapping("/notes")
    public Result<?> addNote(@RequestBody Map<String, Object> request, HttpServletRequest httpRequest) {
        try {
            Long userId = getUserId(httpRequest);
            Long questionId = Long.valueOf(request.get("questionId").toString());
            String content = request.get("content").toString();
            
            userPracticeService.addNote(userId, questionId, content);
            
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取我的笔记（包含题目信息）
     * GET /api/user-practice/notes?bankId={bankId}
     */
    @GetMapping("/notes")
    public Result<List<QuestionVO>> getNotes(@RequestParam Long bankId, HttpServletRequest request) {
        try {
            Long userId = getUserId(request);
            List<QuestionVO> notes = userPracticeService.getNotesWithQuestions(userId, bankId);
            return Result.success(notes);
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
        throw new RuntimeException("未登录");
    }

}

