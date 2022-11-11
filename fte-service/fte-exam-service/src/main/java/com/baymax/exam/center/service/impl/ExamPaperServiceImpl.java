package com.baymax.exam.center.service.impl;

import com.baymax.exam.center.model.ExamPaper;
import com.baymax.exam.center.mapper.ExamMapper;
import com.baymax.exam.center.service.IExamPaperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 考试试卷信息表 服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-10-26
 */
@Service
public class ExamPaperServiceImpl extends ServiceImpl<ExamMapper, ExamPaper> implements IExamPaperService {

}
