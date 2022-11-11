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
    @Schema(description = "选项内容\n" +
            "选择性题目：为选项\n" +
            "填空题/客观题：null\n" +
            "文件题：为文件类型\n" +
            "代码题：为语言类型\n")
    private String content;

    @Schema(description = "题目id")
    private Integer questionId;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @Schema(description = "选项答案：\n" +
            "选择性题目：非null就是正确答案\n" +
            "填空题/客观题：为正确答案\n" +
            "文件题：为文件答案\n" +
            "代码题：为代码执行结果")
    private String answer;
}
