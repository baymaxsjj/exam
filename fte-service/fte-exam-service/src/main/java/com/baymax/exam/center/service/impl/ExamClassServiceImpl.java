package com.baymax.exam.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baymax.exam.center.model.ExamClass;
import com.baymax.exam.center.mapper.ExamClassMapper;
import com.baymax.exam.center.service.IExamClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-10-28
 */
@Service
public class ExamClassServiceImpl extends ServiceImpl<ExamClassMapper, ExamClass> implements IExamClassService {
    @Autowired
    ExamClassMapper examClassMapper;
    /**
     * 删除考试班级
     *
     * @param classId    班级id
     * @param examInfoId 考试信息id
     * @return {@link Integer}
     */
    @Override
    public Integer delExamClass(int classId, int examInfoId) {
        LambdaQueryWrapper<ExamClass> queryWrapper=new LambdaQueryWrapper<>();
        Map<SFunction<ExamClass, ?>, Object> queryMap= Map.of(ExamClass::getClassId,classId,ExamClass::getExamInfoId,examInfoId);
        queryWrapper.allEq(queryMap);
        return examClassMapper.delete(queryWrapper);
    }

    public List<ExamClass> getExamClassIds(int examInfoId){
        LambdaQueryWrapper<ExamClass> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ExamClass::getExamInfoId,examInfoId);
        return list(queryWrapper);
    }
}
