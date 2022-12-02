package com.baymax.exam.center.enums;

import com.baymax.exam.center.model.ParseQuestionRules;
import lombok.Getter;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/12 11:11
 * @description：默认题目规则
 * @modified By：
 * @version:
 */
public enum DefaultQuestionRuleEnum {
//    rule.setDivisionRule("\\n\\d{1,3}\\s*[\\.、：:]");
//        rule.setQuestionRule("");
//        rule.setAnswerRule("答案：\\s*([\\s\\S]*)");
//        rule.setAnswerSplit("；");
//        rule.setOptionRule("\\n\\s*[A-Z]\\s*[、]\\s*");
    CHAOXING("学习通",new ParseQuestionRules("\\n\\d{1,3}\\s*[\\.、：:]","","答案：\\s*([\\s\\S]*)","；","\\n\\s*[A-Z]\\s*[、.]\\s*"));
    @Getter
    private String name;
    @Getter
    private ParseQuestionRules rule;
    DefaultQuestionRuleEnum(String name, ParseQuestionRules rule){
        this.name=name;
        this.rule=rule;
    }
}
