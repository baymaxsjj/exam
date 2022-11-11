package com.baymax.exam.center.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 考试得分
 * </p>
 *
 * @author baymax
 * @since 2022-11-04
 */
@Getter
@Setter
@TableName("ed_exam_record")
@Schema(name = "ExamRecord", description = "考试得分")
public class ExamRecord implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "考试信息id")
    private Integer examInfoId;

    @Schema(description = "题目id")
    private Integer questionId;

    @Schema(description = "得分：null未作答")
    private Byte score;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
