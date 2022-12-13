package com.baymax.exam.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baymax.exam.center.model.ExamScoreRecord;
import com.baymax.exam.center.mapper.ExamScoreRecordMapper;
import com.baymax.exam.center.service.IExamScoreRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 考试得分 服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-12-11
 */
@Service
public class ExamScoreRecordServiceImpl extends ServiceImpl<ExamScoreRecordMapper, ExamScoreRecord> implements IExamScoreRecordService {

    public List<ExamScoreRecord> getScoreList(int examInfoId,int userId){
        LambdaQueryWrapper<ExamScoreRecord> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ExamScoreRecord::getExamInfoId,examInfoId);
        queryWrapper.eq(ExamScoreRecord::getUserId,userId);
        return list(queryWrapper);
    }
}
