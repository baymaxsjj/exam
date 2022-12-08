package com.baymax.exam.center.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

public enum ReviewTypeEnum {
    NONE(10,"未批阅"),
    ROBOT(20,"机器批阅"),
    TEACHER(30,"老师批阅");
    @Getter
    @EnumValue
    int value;
    @Getter
    String label;
    ReviewTypeEnum(int value,String label){
        this.value=value;
        this.label=label;
    }
}
