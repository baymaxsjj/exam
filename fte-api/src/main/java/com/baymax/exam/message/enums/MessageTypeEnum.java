package com.baymax.exam.message.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author Baymax
 * @date 2022/11/21
 */
public enum MessageTypeEnum {
    NONE(0,"无"),
    SYSTEM_NOTIFICATION_MESSAGE(201,"系统通知消息"),
    SYSTEM_ACTIVITIES_MESSAGE(202,"系统活动消息"),
    COURSE_EXAM_MESSAGE(301,"课程考试通知");
    @Getter
    @EnumValue
    private Integer value;
    @Getter
    private String info;
    private MessageTypeEnum(Integer value,String info){
        this.info=info;
        this.value=value;
    }
}
