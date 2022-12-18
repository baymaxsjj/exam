package com.baymax.exam.center.mapper;

import com.baymax.exam.center.model.ExamAnswerResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 考试作答结果 Mapper 接口
 * </p>
 *
 * @author baymax
 * @since 2022-11-04
 */
@Mapper
public interface ExamAnswerResultMapper extends BaseMapper<ExamAnswerResult> {
}
