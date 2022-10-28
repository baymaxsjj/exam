package com.baymax.exam.center.service.impl;

import com.baymax.exam.center.model.ExamInfo;
import com.baymax.exam.center.mapper.ExamInfoMapper;
import com.baymax.exam.center.service.IExamInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
