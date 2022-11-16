package com.baymax.exam.center.vo;

import com.baymax.exam.center.enums.DefaultQuestionRuleEnum;
import com.baymax.exam.center.model.ParseQuestionRules;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/12 10:57
 * @description：批量题目
 * @modified By：
 * @version:
 */
@Data
public class ParseQuestionVo {
    @NotBlank
    @Schema(description = "问题文本")
    private String questionsText;

    @Schema(description = "自定义规则,优先级>默认规则")

    private ParseQuestionRules customRule;

    @Schema(description = "默认规则")
    private DefaultQuestionRuleEnum defaultRule;
}
