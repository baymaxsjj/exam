package com.baymax.exam.center.model;

import com.baomidou.mybatisplus.annotation.*;

import com.baymax.exam.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 题目选择表
 * </p>
 *
 * @author baymax
 * @since 2022-10-17
 */
@Getter
@Setter
@TableName("eq_question_item")
@Schema(name = "QuestionItem", description = "题目选择表")
public class QuestionItem extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
      private Integer id;
    @Schema(description = "选择内容")
    private String content;

    @Schema(description = "题目id")
    private Integer questionId;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @Schema(description = "是否正确：null:不正确、！null:正确")
    private String correct;
}
