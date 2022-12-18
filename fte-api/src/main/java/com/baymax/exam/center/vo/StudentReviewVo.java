package com.baymax.exam.center.vo;

import com.baymax.exam.center.enums.ExamAnswerLogEnum;
import com.baymax.exam.center.enums.QuestionTypeEnum;
import com.baymax.exam.user.model.UserAuthInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author ：Baymax
 * @date ：Created in 2022/12/12 10:45
 * @description：
 * @modified By：
 * @version:
 */
@Data
public class StudentReviewVo {
    @Schema(description = "学生信息")
    private UserAuthInfo userAuthInfo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "提交时间")
    private LocalDateTime submitTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    @Schema(description = "正确个数")
    private Integer correctNumber=0;
    @Schema(description = "已批阅个数")
    private Integer reviewCount=0;
    @Schema(description = "批阅总数")
    private Integer reviewTotal=0;
    @Schema(description = "得分")
    private Float score=0f;
    @Schema(description = "审阅类型")
    private ExamAnswerLogEnum answerStatus;
    @Schema(description = "作答结果")
    private Map<QuestionTypeEnum, List<AnswerQuestionResultVo>> answerResults;
}
