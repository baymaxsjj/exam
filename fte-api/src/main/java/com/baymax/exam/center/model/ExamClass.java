package com.baymax.exam.center.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author baymax
 * @since 2022-10-28
 */
@Getter
@Setter
@TableName("ee_exam_class")
@Schema(name = "ExamClass", description = "")
public class ExamClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "考试班级")
      private Integer classId;

    @Schema(description = "考试信息")
      private Integer examInfoId;
}
