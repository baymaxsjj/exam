package com.baymax.exam.center.service;

import com.baymax.exam.center.model.Exam;
import com.baymax.exam.center.model.ExamQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baymax.exam.center.model.Question;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baymax
 * @since 2022-10-26
 */
public interface IExamQuestionService extends IService<ExamQuestion> {
    /**
     * 获取试卷题目信息
     *
     * @param id id
     * @return {@link Question}
     */
    Question getQuestionByExamId(Integer id);
}
