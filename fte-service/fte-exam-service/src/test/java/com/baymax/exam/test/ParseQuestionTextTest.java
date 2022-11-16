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
                "25、串行口中断标志 RI/TI 由 ____ 置位,____ 清零。\n" +
                "答案： 系统； 软件；\n" +
                "26、单片机应用程序一般存放在( ) 中。\n" +
                "A、 RAM\n" +
                "B、 ROM\n" +
                "C、 寄存器\n" +
                "D、 CPU\n" +
                "答案： B\n" +
                "27、程序状态字寄存器 PSW 中的 AC=1,表示( )。\n" +
                "A、 计算结果有进位\n" +
                "B、 计算结果有溢出\n" +
                "C、 累加器 A 中的数据有奇数个 1\n" +
                "D、 计算结果低 4 位向高位进位\n" +
                "答案： D\n"
                +"32、MCS-51 外扩 ROM、RAM 和 I/O 口时,它的地址总线是( )\n" +
                        "A、 P0 、P1\n" +
                        "B、 P0、P2\n" +
                        "C、 P2、P1\n" +
                        "D、 P0、P3\n" +
                        "答案： B";
    }
}
