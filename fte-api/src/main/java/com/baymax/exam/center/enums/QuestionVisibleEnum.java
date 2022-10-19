package com.baymax.exam.center.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baymax.exam.common.core.base.IBaseEnum;
import lombok.Getter;

public enum QuestionVisibleEnum implements IBaseEnum {
    self(0,"自己"),
    course(1,"课程"),
    overt(2,"公开");

    @Getter
    @EnumValue //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    private Integer value;

    @Getter
    // @JsonValue //  表示对枚举序列化时返回此字段
    private String label;

    QuestionVisibleEnum(Integer value,String label){
        this.value=value;
        this.label=label;
    }


}
