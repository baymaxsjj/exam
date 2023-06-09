package com.baymax.exam.center.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baymax.exam.base.BaseEntity;
import com.baymax.exam.center.enums.ExamAnswerLogEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Null;

/**
 * <p>
 * 考试作答日志
 * </p>
 *
 * @author baymax
 * @since 2022-11-04
 */
@Getter
@Setter
@TableName("ed_exam_answer_log")
@Schema(name = "ExamAnswerLog", description = "考试作答日志")
public class ExamAnswerLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

      private Integer id;

    @Schema(description = "考试id")
    private Integer examId;

    @Schema(description = "学生id")
    private Integer studentId;
    @Schema(description = "班级id")
    private Integer classId;

    @Null(message = "状态不能为考")
    @Schema(description = "状态：0:开始、2：交卷")
    private ExamAnswerLogEnum status;

    @Schema(description = "状态信息")
    private String info;
}
