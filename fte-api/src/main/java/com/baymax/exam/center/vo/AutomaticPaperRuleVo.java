package com.baymax.exam.center.vo;

import com.baymax.exam.center.enums.QuestionTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ：Baymax
 * @date ：Created in 2023/3/10 18:32
 * @description：自动组卷参数
 * @modified By：
 * @version:
 */
@Data
public class AutomaticPaperRuleVo {

    @Schema(defaultValue = "题型")
    private List<QuestionTypeEnum> questionType;
    @Schema(defaultValue = "题目分布")
    private Map<QuestionTypeEnum,Float> percentage;

    @Max(value = 5,message = "题目难度应在0~5")
    @Schema(defaultValue = "题目难度")
    private Integer difficulty=3;

    @Schema(defaultValue = "题目标签")
    private Set<Integer> tags;
    @NotNull(message = "题目数量不能为空")
    @Schema(defaultValue = "题目数量")
    private Integer totalNumber;
    @Schema(defaultValue = "题目总分吧")
    private Integer totalScore;

}
