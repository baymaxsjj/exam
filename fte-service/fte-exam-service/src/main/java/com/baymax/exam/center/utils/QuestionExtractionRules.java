package com.baymax.exam.center.utils;

import lombok.Data;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/10 20:35
 * @description：
 * @modified By：
 * @version:
 */ //题目提取规则
@Data
public
class QuestionExtractionRules {
    /**
     * 分割规则
     */
    private String divisionRule;
    /**
     * 题目规则
     */
    private String questionRule;
    /**
     * 题目规则
     */
    private String answerRule;

    /**
     * 答案分割
     */
    private String answerSplit;
    /**
     * 选择规则
     */
    private String optionRule;
}
