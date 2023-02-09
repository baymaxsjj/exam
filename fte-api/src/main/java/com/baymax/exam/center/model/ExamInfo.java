package com.baymax.exam.center.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.List;

import com.baymax.exam.base.BaseEntity;
import com.baymax.exam.user.model.Classes;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
@TableName("ee_exam_info")
@Schema(name = "ExamInfo", description = "考试信息")
public class ExamInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "考试标题不能为空")
    @Schema(description = "考试标题")
    private String title;

    @NotNull(message = "试卷id不能为空")
    @Schema(description = "试卷id")
    private Integer examId;

    @Schema(description = "老师id")
    private Integer teacherId;

    @NotNull(message = "课程id不能为空")
    @Schema(description = "课程id")
    private Integer courseId;

    @Schema(description = "题目乱序")
    private Boolean questionDisorder;

    @Schema(description = "选项乱序")
    private Boolean optionDisorder;

    @Schema(description = "结束可见")
    private Boolean endVisible;
    @Schema(description = "开启监控")
    private Boolean isMonitor;

    @Schema(description = "允许复制粘贴")
    private Boolean isCopyPaste;

    @Future(message = "提交时间不合法")
    @Schema(description = "提交时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitTime;

    @NotNull(message = "开始时间不能为空")
    @Schema(description = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Future(message = "结束时间不合法")
    @NotNull(message = "结束时间不能为空")
    @Schema(description = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
