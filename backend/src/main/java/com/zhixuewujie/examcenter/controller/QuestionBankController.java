package com.zhixuewujie.examcenter.controller;

import com.zhixuewujie.examcenter.common.Result;
import com.zhixuewujie.examcenter.service.IQuestionBankService;
import com.zhixuewujie.examcenter.util.JwtUtil;
import com.zhixuewujie.examcenter.vo.QuestionBankVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 题库控制器
 * 
 * 功能：
 * - 获取我的题库列表
 * - 获取题库详情
 * - 题库统计信息
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@RestController
@RequestMapping("/api/question-banks")
public class QuestionBankController {

    @Autowired
    private IQuestionBankService questionBankService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取我的题库列表
     * GET /api/question-banks
     */
    @GetMapping
    public Result<List<QuestionBankVO>> getQuestionBanks(HttpServletRequest request) {
        try {
            Long userId = getUserId(request);
            List<QuestionBankVO> banks = questionBankService.getQuestionBanks(userId);
            return Result.success(banks);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取题库详情
     * GET /api/question-banks/{id}
     */
    @GetMapping("/{id}")
    public Result<QuestionBankVO> getQuestionBankDetail(@PathVariable Long id) {
        try {
            QuestionBankVO bank = questionBankService.getQuestionBankDetail(id);
            return Result.success(bank);
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

