package com.baymax.exam.center.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baymax.exam.center.model.ExamInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 考试信息 Mapper 接口
 * </p>
 *
 * @author baymax
 * @since 2022-10-28
 */
@Mapper
public interface ExamInfoMapper extends BaseMapper<ExamInfo> {
    IPage<ExamInfo> getSutExamInfo(IPage<ExamInfo> page, Wrapper ew);
}
