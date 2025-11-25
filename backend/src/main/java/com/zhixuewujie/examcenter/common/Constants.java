package com.zhixuewujie.examcenter.common;

/**
 * 系统常量定义
 *
 * @author 智学无界
 * @since 2025-11-24
 */
public class Constants {

    /**
     * 题目类型
     */
    public static class QuestionType {
        /** 判断题 */
        public static final String JUDGE = "judge";
        /** 单选题 */
        public static final String SINGLE = "single";
        /** 多选题 */
        public static final String MULTIPLE = "multiple";
    }

    /**
     * 练习模式
     */
    public static class PracticeMode {
        /** 答题模式：选择选项后立即判断对错，显示答案和解析 */
        public static final String ANSWER = "ANSWER";
        /** 背题模式：进入页面时直接展示答案和解析 */
        public static final String RECITE = "RECITE";
    }

    /**
     * 来源类型
     */
    public static class SourceType {
        /** 练习 */
        public static final String PRACTICE = "PRACTICE";
        /** 考试 */
        public static final String EXAM = "EXAM";
    }

    /**
     * 考试状态
     */
    public static class ExamStatus {
        /** 进行中 */
        public static final String ONGOING = "ONGOING";
        /** 已完成 */
        public static final String FINISHED = "FINISHED";
    }

    /**
     * 是否正确
     */
    public static class IsCorrect {
        /** 正确 */
        public static final int YES = 1;
        /** 错误 */
        public static final int NO = 0;
    }

    /**
     * 是否及格
     */
    public static class IsPassed {
        /** 及格 */
        public static final int YES = 1;
        /** 不及格 */
        public static final int NO = 0;
    }

    /**
     * 用户状态
     */
    public static class UserStatus {
        /** 正常 */
        public static final int NORMAL = 0;
        /** 禁用 */
        public static final int DISABLED = 1;
    }

}


