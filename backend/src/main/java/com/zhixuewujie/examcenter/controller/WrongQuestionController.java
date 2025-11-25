package com.zhixuewujie.examcenter.controller;

import org.springframework.web.bind.annotation.*;

/**
 * 错题与易错题控制器
 * 
 * 功能：
 * - 我的错题（任何错误都记录，按题目ID去重展示）
 * - 易错题（错误次数 ≥ 2 次的题目）
 * - 错题重做
 * - 错题统计
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@RestController
@RequestMapping("/api/wrong-questions")
public class WrongQuestionController {

    /**
     * 获取我的错题列表（按题目ID去重）
     * GET /api/wrong-questions/my-wrong?bankId={bankId}
     * 
     * 逻辑：
     * 1. 查询该用户在指定题库中所有答错的题目
     * 2. 按题目ID去重
     * 3. 按最近错误时间倒序排列
     * 4. 返回题目列表及错误次数
     */
    // TODO: 实现我的错题查询

    /**
     * 获取易错题列表（错误次数 ≥ 2 次）
     * GET /api/wrong-questions/difficult?bankId={bankId}
     * 
     * 逻辑：
     * 1. 统计该用户对每道题的错误次数
     * 2. 筛选出错误次数 ≥ 2 次的题目
     * 3. 按错误次数降序排列
     * 4. 返回题目列表及统计信息
     */
    // TODO: 实现易错题查询

    /**
     * 获取错题统计
     * GET /api/wrong-questions/statistics?bankId={bankId}
     * 
     * 返回：
     * - 错题总数（去重后）
     * - 易错题总数（错误次数 ≥ 2）
     * - 最常错的题型
     * - 错误率趋势
     */
    // TODO: 实现错题统计

    /**
     * 清空我的错题（标记为已掌握）
     * DELETE /api/wrong-questions/clear?bankId={bankId}
     * 
     * 说明：并非真正删除记录，而是标记为"已掌握"，不再在错题列表中显示
     */
    // TODO: 实现清空错题

    /**
     * 移除单个错题（标记为已掌握）
     * DELETE /api/wrong-questions/{questionId}
     */
    // TODO: 实现移除单个错题

}


