package com.baymax.exam.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baymax.exam.center.model.ExamQuestion;
import com.baymax.exam.center.mapper.ExamQuestionMapper;
import com.baymax.exam.center.model.Question;
import com.baymax.exam.center.service.IExamQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 得到考试问题
     *
     * @param examId     考试id
     * @param questionId 问题id
     * @return {@link ExamQuestion}
     */
    @Override
    public ExamQuestion getExamQuestion(int examId, int questionId) {
        LambdaQueryWrapper<ExamQuestion> queryWrapper=new LambdaQueryWrapper<>();
        Map<SFunction<ExamQuestion, ?>,Object> queryMap=new HashMap();
        queryMap.put(ExamQuestion::getExamId,examId);
        queryMap.put(ExamQuestion::getQuestionId,questionId);
        queryWrapper.allEq(queryMap);
        return examQuestionMapper.selectOne(queryWrapper);
    }
}
