package com.baymax.exam.center.vo;

import com.baymax.exam.center.model.ExamAnswerResult;
import com.baymax.exam.center.model.ExamScoreRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author ：Baymax
 * @date ：Created in 2022/12/12 18:24
 * @description：
 * @modified By：
 * @version:
 */
@Data
public class AnswerQuestionResultVo {
    @Schema(description = "题目信息")
    private QuestionInfoVo questionInfo;
    @Schema(description = "作答信息")
    private List<ExamAnswerResult> answerResult;
    @Schema(description = "批阅结果")
    private ExamScoreRecord scoreRecord;
}
