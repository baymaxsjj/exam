package com.baymax.exam.center.vo;

import com.baymax.exam.center.model.ExamAnswerResult;
import com.baymax.exam.center.model.ExamScoreRecord;
import lombok.Data;

import java.util.List;

/**
 * @author ：Baymax
 * @date ：Created in 2022/12/9 15:48
 * @description：考试作答情况
 * @modified By：
 * @version:
 */
@Data
public class ExamAnswerInfoVo {
    private ExamScoreRecord scoreRecord;
    private List<ExamAnswerResult> answerResult;
}
