package com.baymax.exam.center.service.impl;

import com.baymax.exam.center.model.ExamQuestion;
import com.baymax.exam.center.mapper.ExamQuestionMapper;
import com.baymax.exam.center.model.Question;
import com.baymax.exam.center.service.IExamQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-10-26
 */
@Service
public class ExamQuestionServiceImpl extends ServiceImpl<ExamQuestionMapper, ExamQuestion> implements IExamQuestionService {
    @Autowired
    ExamQuestionMapper examQuestionMapper;
    /**
     * 获取试卷题目信息
     *
     * @param id id
     * @return {@link Question}
     */
    @Override
    public List<Question> getQuestionByExamId(Integer id) {
        return examQuestionMapper.getQuestion(id);
    }
}
