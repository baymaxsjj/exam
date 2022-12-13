package com.baymax.exam.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baymax.exam.center.model.ExamAnswerResult;
import com.baymax.exam.center.mapper.ExamAnswerResultMapper;
import com.baymax.exam.center.service.IExamAnswerResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 考试作答结果 服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-11-04
 */
@Service
public class ExamAnswerResultServiceImpl extends ServiceImpl<ExamAnswerResultMapper, ExamAnswerResult> implements IExamAnswerResultService {
    public List<ExamAnswerResult> getAnswerResultList(int examInfoId,int userId){
        LambdaQueryWrapper<ExamAnswerResult> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ExamAnswerResult::getExamInfoId,examInfoId);
        queryWrapper.eq(ExamAnswerResult::getUserId,userId);
        return list(queryWrapper);
    }
}
