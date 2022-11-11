package com.baymax.exam.center.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baymax.exam.center.model.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baymax.exam.center.vo.QuestionInfoVo;
import com.baymax.exam.user.vo.CourseInfoVo;

import java.util.List;

/**
 * <p>
 * 题目信息 服务类
 * </p>
 *
 * @author baymax
 * @since 2022-10-18
 */
public interface IQuestionService extends IService<Question> {

    /**
     * 添加题目
     *
     * @param questionInfo 问题信息
     * @return boolean
     */
    String addQuestion(QuestionInfoVo questionInfo);

    /**
     * 问题信息
     *
     * @param questionId 问题id
     * @return {@link QuestionInfoVo}
     */
    QuestionInfoVo questionInfo(Integer questionId);

    List<QuestionInfoVo> examQuestionInfo(Integer examId);
}
