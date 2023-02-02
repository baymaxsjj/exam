package com.baymax.exam.center.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
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
    @ExcelProperty("提交时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "提交时间")
    private LocalDateTime submitTime;
    @ExcelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    @ExcelProperty("正确个数")
    @Schema(description = "正确个数")
    private Integer correctNumber=0;
    @ExcelProperty("已批阅个数")
    @Schema(description = "已批阅个数")
    private Integer reviewCount=0;
    @ExcelProperty("批阅总数")
    @Schema(description = "批阅总数")
    private Integer reviewTotal=0;
    @ExcelProperty("课程班级名称")
    @Schema(description = "课程班级名称")
    private String courseClassName;
    @ExcelProperty("得分")
    @Schema(description = "得分")
    private Float score=0f;
    @ExcelProperty(value = "审阅类型")
    @Schema(description = "审阅类型")
    private ExamAnswerLogEnum answerStatus;
    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    @Schema(description = "作答结果")
    private Map<QuestionTypeEnum, List<AnswerQuestionResultVo>> answerResults;
}
