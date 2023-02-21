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
    private List<ExamAnswerResult> getAnswerResultList(int examInfoId,Integer userId){
        LambdaQueryWrapper<ExamAnswerResult> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ExamAnswerResult::getExamInfoId,examInfoId);
        if(userId!=null){
            queryWrapper.eq(ExamAnswerResult::getUserId,userId);
        }
        return list(queryWrapper);
    }
    public List<ExamAnswerResult> getAnswerResultListByExamId(int examInfoId){
        return getAnswerResultList(examInfoId,null);
    }
    public List<ExamAnswerResult> getAnswerResultListByUserId(int examInfoId,int userId){
        return getAnswerResultList(examInfoId,userId);
    }
}
