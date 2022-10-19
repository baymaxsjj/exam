package com.baymax.exam.center.service.impl;

import com.baymax.exam.center.model.Question;
import com.baymax.exam.center.mapper.QuestionMapper;
import com.baymax.exam.center.service.IQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baymax.exam.center.vo.QuestionInfoVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 题目信息 服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-10-18
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    /**
     * 更新题目
     *
     * @param questionInfo 问题信息
     * @return boolean
     */
    @Override
    public boolean updateQuestion(QuestionInfoVo questionInfo) {
//        if(questionInfo)
        return false;
    }
}
