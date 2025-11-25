package com.zhixuewujie.examcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhixuewujie.examcenter.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 题目Mapper
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

    /**
     * 随机获取题目
     */
    List<Question> selectRandomQuestions(@Param("bankId") Long bankId, 
                                         @Param("type") String type, 
                                         @Param("count") Integer count);

}

