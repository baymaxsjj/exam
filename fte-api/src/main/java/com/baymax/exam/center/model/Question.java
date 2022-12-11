package com.baymax.exam.center.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.baymax.exam.base.BaseEntity;
import com.baymax.exam.center.enums.QuestionTypeEnum;
import com.baymax.exam.center.enums.QuestionVisibleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * <p>
 * 题目信息
 * </p>
 *
 * @author baymax
 * @since 2022-10-17
 */
@Getter
@Setter
@TableName("eq_question")
@Schema(name = "Question", description = "题目信息")
public class Question extends BaseEntity {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @NotBlank(message = "题目名称不能空~")
    @Schema(description = "题目名称")
    private String content;

    @NotNull(message = "题目名称不能空~")
    @Schema(description = "题目类型：0：单选、1：多选、2：判断、3：填空、4：主观")
    private QuestionTypeEnum type;

    @Schema(description = "题目解析")
    private String analysis;

    @Range(min = 0,max = 5,message = "题目难度应在0~5之间")
    @Schema(description = "题目难度")
    private Integer difficulty;

    @Range(min = 1,max = 100,message = "题目分值应在1~100分之间")
    @Schema(description = "题目分值")
    private Float score;

    @Schema(description = "是否公开：0:自己、1：课程、2：公开")
    private QuestionVisibleEnum isPublic;

    @Schema(description = "课程id")
    private Integer courseId;

    @Schema(description = "题目标签id")
    private Integer tagId;

    @Schema(description = "教师id")
    private Integer teacherId;

}
