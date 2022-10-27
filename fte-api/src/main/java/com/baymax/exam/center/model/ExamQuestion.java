package com.baymax.exam.center.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baymax.exam.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author baymax
 * @since 2022-10-26
 */
@Getter
@Setter
@TableName("ee_exam_question")
@Schema(name = "ExamQuestion", description = "")
public class ExamQuestion extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "题目id不能为空")
    @Schema(description = "题目id")
    private Integer questionId;

    @NotNull(message = "试卷id不能为空")
    @Schema(description = "试卷id")
    private Integer examId;

}
