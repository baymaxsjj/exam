package com.baymax.exam.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baymax.exam.center.model.ExamInfo;
import com.baymax.exam.center.mapper.ExamInfoMapper;
import com.baymax.exam.center.service.IExamInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 考试信息 服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-10-28
 */
@Service
public class ExamInfoServiceImpl extends ServiceImpl<ExamInfoMapper, ExamInfo> implements IExamInfoService {
    @Autowired
    ExamInfoMapper examInfoMapper;

    /**
     * 得到学生考试信息
     *
     * @param page 页面
     * @param ew   电子战
     * @return {@link IPage}<{@link ExamInfo}>
     */
    @Override
    public IPage<ExamInfo> getSutExamInfo(IPage<ExamInfo> page,Wrapper ew) {
        return examInfoMapper.getSutExamInfo(page,ew);
    }
}
