package com.zhixuewujie.examcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhixuewujie.examcenter.entity.UserAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户答题记录Mapper
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@Mapper
public interface UserAnswerMapper extends BaseMapper<UserAnswer> {

    /**
     * 获取我的错题（按题目ID去重）
     */
    List<Map<String, Object>> selectWrongQuestions(@Param("userId") Long userId, 
                                                   @Param("bankId") Long bankId);

    /**
     * 获取易错题（错误次数 >= 2）
     */
    List<Map<String, Object>> selectDifficultQuestions(@Param("userId") Long userId, 
                                                        @Param("bankId") Long bankId);

}

