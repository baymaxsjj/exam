package com.baymax.exam.center.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baymax.exam.center.enums.QuestionResultTypeEnum;
import com.baymax.exam.center.enums.ReviewTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 考试得分
 * </p>
 *
 * @author baymax
 * @since 2022-12-11
 */
@Getter
@Setter
@TableName("ed_exam_score_record")
@Schema(name = "ExamScoreRecord", description = "考试得分")
public class ExamScoreRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "id不能为空")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "用户id")
    private Integer userId;
    @Schema(description = "班级id")
    private Integer classId;
    @Schema(description = "考试信息id")
    private Integer examInfoId;

    @Schema(description = "题目id")
    private Integer questionId;
    @NotNull(message = "得分不能为空")
    @Schema(description = "得分")
    private Float score;

    @Schema(description = "评语")
    private String comment;
    @Schema(description = "结果类型：对、错、半错")
    private QuestionResultTypeEnum resultType;
    @Schema(description = "评阅类型")
    private ReviewTypeEnum reviewType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
