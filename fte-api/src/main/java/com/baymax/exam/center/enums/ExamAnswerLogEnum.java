package com.baymax.exam.center.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Value;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/13 15:36
 * @description：考试作答日志
 * @modified By：
 * @version:
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ExamAnswerLogEnum {
    START(11,"开始答题"),
    SUBMIT(12,"提交试卷"),
    COPY(51,"复制行为"),
    PASTE(52,"粘贴行为"),
    PAUSE(53,"后台行为");

    @Getter
    private String action;
    @Getter
    @EnumValue
    private Integer value;
    ExamAnswerLogEnum(Integer value,String action){
        this.action=action;
        this.value=value;
    }
}
