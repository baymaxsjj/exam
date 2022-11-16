package com.baymax.exam.center.enums;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/13 15:36
 * @description：考试作答日志
 * @modified By：
 * @version:
 */
public enum ExamAnswerLogEnum {
    START(0,"开始考试"),
    SUBMIT(1,"提交"),
    COPY(2,"复制"),
    PASTE(3,"粘贴"),
    PAUSE(4,"暂停");
    private String action;
    private Integer value;
    ExamAnswerLogEnum(Integer value,String action){
        this.action=action;
        this.value=value;
    }
}
