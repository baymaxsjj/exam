package com.baymax.exam.center.vo;

import com.baymax.exam.center.enums.QuestionTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
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

    @Size(min = 0,max = 5,message = "题目难度应在1~5")
    @Schema(defaultValue = "题目难度")
    private Byte difficulty;

    @Schema(defaultValue = "题目标签")
    private Set<Integer> tags;
    @NotEmpty(message = "题目数量不能为空")
    @Schema(defaultValue = "题目数量")
    private Integer totalNumber;
    @Schema(defaultValue = "题目总分吧")
    private Integer totalScore;

}
