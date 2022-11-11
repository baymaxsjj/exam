package com.baymax.exam.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baymax.exam.center.model.QuestionItem;
import com.baymax.exam.center.mapper.QuestionItemMapper;
import com.baymax.exam.center.service.IQuestionItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 题目选择表 服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-10-18
 */
@Service
public class QuestionItemServiceImpl extends ServiceImpl<QuestionItemMapper, QuestionItem> implements IQuestionItemService {

    /**
     * 获取题目项
     *
     * @param questionId 问题id
     * @return {@link List}<{@link QuestionItem}>
     */
    @Override
    public List<QuestionItem> getQuestionItems(int questionId) {
        return list(new LambdaQueryWrapper<QuestionItem>().eq(QuestionItem::getQuestionId,questionId));
    }
}
