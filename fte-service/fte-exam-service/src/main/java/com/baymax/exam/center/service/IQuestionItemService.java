package com.baymax.exam.center.service;

import com.baymax.exam.center.model.QuestionItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 题目选择表 服务类
 * </p>
 *
 * @author baymax
 * @since 2022-10-18
 */
public interface IQuestionItemService extends IService<QuestionItem> {
    /**
     * 获取题目项
     *
     * @param questionId 问题id
     * @return {@link List}<{@link QuestionItem}>
     */
    List<QuestionItem> getQuestionItems(int questionId);
}
