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
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 考试试卷信息表
 * </p>
 *
 * @author baymax
 * @since 2022-10-26
 */
@Getter
@Setter
@TableName("ee_exam")
@Schema(name = "Exam", description = "考试试卷信息表")
public class Exam extends BaseEntity {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

      @NotBlank(message = "标题不能为空")
      @Length(min = 2,max = 255,message = "试卷标题应在2~100个字符")
    @Schema(description = "试卷标题")
    private String title;

    @Length(min = 2,max = 255,message = "试卷介绍应在2~100个字符")
    @Schema(description = "试卷介绍")
    private String introduce;

    @NotNull(message = "课程id不能为空")
    @Schema(description = "课程id")
    private Integer courseId;

    @Schema(description = "教师id")
    private Integer teacherId;
}
