package com.zhixuewujie.examcenter.common;

/**
 * 模拟考试配置常量
 * 
 * 人工智能训练师（三级/高级）考试规则：
 * - 判断题：40题 × 0.5分 = 20分
 * - 单选题：140题 × 0.5分 = 70分
 * - 多选题：10题 × 1分 = 10分
 * - 总分：100分
 * - 及格分：60分
 * - 考试时长：90分钟
 *
 * @author 智学无界
 * @since 2025-11-24
 */
public class ExamConfig {

    /**
     * 判断题数量
     */
    public static final int JUDGE_COUNT = 40;

    /**
     * 判断题每题分数
     */
    public static final double JUDGE_SCORE = 0.5;

    /**
     * 单选题数量
     */
    public static final int SINGLE_COUNT = 140;

    /**
     * 单选题每题分数
     */
    public static final double SINGLE_SCORE = 0.5;

    /**
     * 多选题数量
     */
    public static final int MULTIPLE_COUNT = 10;

    /**
     * 多选题每题分数
     */
    public static final double MULTIPLE_SCORE = 1.0;

    /**
     * 总分
     */
    public static final double TOTAL_SCORE = 100.0;

    /**
     * 及格分
     */
    public static final double PASS_SCORE = 60.0;

    /**
     * 考试时长（分钟）
     */
    public static final int DURATION = 90;

    /**
     * 易错题最小错误次数
     */
    public static final int DIFFICULT_MIN_WRONG_COUNT = 2;

    /**
     * 计算题目分数
     * 
     * @param questionType 题目类型：judge-判断题，single-单选题，multiple-多选题
     * @return 题目分数
     */
    public static double getQuestionScore(String questionType) {
        switch (questionType) {
            case "judge":
                return JUDGE_SCORE;
            case "single":
                return SINGLE_SCORE;
            case "multiple":
                return MULTIPLE_SCORE;
            default:
                return 0.0;
        }
    }

    /**
     * 计算总分
     * 
     * @param judgeCorrect 判断题正确数
     * @param singleCorrect 单选题正确数
     * @param multipleCorrect 多选题正确数
     * @return 总分
     */
    public static double calculateScore(int judgeCorrect, int singleCorrect, int multipleCorrect) {
        return judgeCorrect * JUDGE_SCORE + singleCorrect * SINGLE_SCORE + multipleCorrect * MULTIPLE_SCORE;
    }

    /**
     * 判断是否及格
     * 
     * @param score 得分
     * @return true-及格，false-不及格
     */
    public static boolean isPassed(double score) {
        return score >= PASS_SCORE;
    }

}


