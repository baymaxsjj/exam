package com.baymax.exam.center.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

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
public class ExamAnswerLog implements Serializable {

    private static final long serialVersionUID = 1L;

      private Integer id;

    @Schema(description = "考试id")
    private Integer examId;

    @Schema(description = "学生id")
    private Integer sutdentId;

    @Schema(description = "状态：0:开始、2：交卷")
    private Byte status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
