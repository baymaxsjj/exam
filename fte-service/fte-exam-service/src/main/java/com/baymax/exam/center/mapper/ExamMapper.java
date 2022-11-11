package com.baymax.exam.center.mapper;

import com.baymax.exam.center.model.ExamPaper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baymax.exam.center.vo.ExamPaperVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 考试试卷信息表 Mapper 接口
 * </p>
 *
 * @author baymax
 * @since 2022-10-26
 */
@Mapper
public interface ExamMapper extends BaseMapper<ExamPaper> {
    ExamPaperVo getExamInfo(Integer examId);
}
