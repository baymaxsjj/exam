package com.baymax.exam.center.service;

import com.baymax.exam.center.model.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baymax.exam.center.vo.QuestionInfoVo;

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
     * 更新题目
     *
     * @param questionInfo 问题信息
     * @return boolean
     */
    boolean updateQuestion(QuestionInfoVo questionInfo);
}
