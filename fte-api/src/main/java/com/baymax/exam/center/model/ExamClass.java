package com.baymax.exam.center.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baymax.exam.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * <p>
 * 考试班级
 * </p>
 *
 * @author baymax
 * @since 2022-10-26
 */
@Getter
@Setter
@TableName("ee_exam_class")
@Schema(name = "ExamClass", description = "考试班级")
public class ExamClass extends BaseEntity {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

      @NotNull(message = "班级id不能为空")
    @Schema(description = "班级id")
    private Integer classId;

    @NotNull(message = "试卷id不能为空")
    @Schema(description = "试卷id")
    private Integer examId;

    @Schema(description = "题目乱序")
    private Boolean questionDisorder;

    @Schema(description = "选项乱序")
    private Boolean optionDisorder;

    @Schema(description = "结束可见")
    private Boolean endVisible;

    @Future(message = "开始时间不合法")
    @NotNull(message = "开始时间不能为空")
    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Future(message = "结束时间不合法")
    @NotNull(message = "结束时间不能为空")
    @Schema(description = "结束时间")
    private LocalDateTime endTime;

}
