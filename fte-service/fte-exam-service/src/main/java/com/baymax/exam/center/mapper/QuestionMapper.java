package com.baymax.exam.center.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baymax.exam.center.model.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baymax.exam.center.vo.QuestionInfoVo;
import com.baymax.exam.user.vo.CourseInfoVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 题目信息 Mapper 接口
 * </p>
 *
 * @author baymax
 * @since 2022-10-18
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {
    IPage<QuestionInfoVo> questionInfoList(IPage<QuestionInfoVo> page, QueryWrapper<QuestionInfoVo> ew);
}
