package com.baymax.exam.center.vo;

import com.baymax.exam.center.enums.QuestionTypeEnum;
import com.baymax.exam.center.model.Question;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/12 12:14
 * @description：
 * @modified By：
 * @version:
 */
@Data
public class BatchQuestion {
    @NotNull(message = "课程id不能为空")
    @Schema(description = "课程id")
    private Integer courseId;
    @Schema(description = "导入标签")
    private Integer tagId;
    @NotEmpty(message = "题目列表不能为空")
    @Schema(description = "题目列表")
    private List<QuestionInfoVo> questionInfos;
    @Schema(description = "题目配置")
    private Set<Question> questionConfig;
}
