package com.baymax.exam.test;

import com.alibaba.fastjson2.JSONObject;
import com.baymax.exam.center.utils.ParseQuestionText;
import com.baymax.exam.center.model.ParseQuestionRules;
import com.baymax.exam.center.vo.QuestionInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/10 17:56
 * @description：提取题目测试
 * @modified By：
 * @version:
 */
@Slf4j
public class ParseQuestionTextTest {

    @Test
    public void parse(){
        ParseQuestionRules rule = new ParseQuestionRules();
        rule.setDivisionRule("\\n\\d{1,3}\\s*[\\.、：:]");
        rule.setQuestionRule("");
        rule.setAnswerRule("答案：\\s*([\\s\\S]*)");
        rule.setAnswerSplit("；");
        rule.setOptionRule("\\n\\s*[A-Z]\\s*[、]\\s*");
        List<QuestionInfoVo> parse = ParseQuestionText.parse(getQuestionStr(), rule);
        log.info(JSONObject.toJSONString(parse));
    }
    public String getQuestionStr(){
        return
                "1.下列说法中正确的是( )。\n" +
                        "A.20 世纪 50 年代提出了软件工程概念\n" +
                        "B.20 世纪 60 年代提出了软件工程概念\n" +
                        "C.20 世纪 70 年代出现了客户机/服务器技术\n" +
                        "D.20 世纪 80 年代软件工程学科达到成熟\n" +
                        "答案： B\n" +
                        "2.软件危机的主要原因是( )。\n" +
                        "A.软件工具落后\n" +
                        "B.软件生产能力不足\n" +
                        "C.对软件的认识不够\n" +
                        "D.软件本身的特点及开发方法\n" +
                        "答案： D";
    }
}
