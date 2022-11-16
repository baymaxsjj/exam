package com.baymax.exam.center.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/10 20:35
 * @description：
 * @modified By：
 * @version:
 */ //题目提取规则
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParseQuestionRules {

    @Schema(description = "分割规则")
    private String divisionRule;

    @Schema(description = "题目规则")
    private String questionRule;

    @Schema(description = "答案规则")
    private String answerRule;

    @Schema(description = "答案分割")
    private String answerSplit;

    @Schema(description = "选择规则")
    private String optionRule;
}
