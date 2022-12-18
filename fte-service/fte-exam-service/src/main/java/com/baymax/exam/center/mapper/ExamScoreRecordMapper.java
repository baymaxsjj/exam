package com.baymax.exam.center.mapper;

import com.baymax.exam.center.model.ExamAnswerResult;
import com.baymax.exam.center.model.ExamScoreRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 考试得分 Mapper 接口
 * </p>
 *
 * @author baymax
 * @since 2022-12-11
 */
@Mapper
public interface ExamScoreRecordMapper extends BaseMapper<ExamScoreRecord> {
    void batchUpdateByList(List<ExamScoreRecord> scoreRecordList);
}
