package com.baymax.exam.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Data;
import lombok.Getter;

public enum LoginTypeEnum {
    QQ("qq"),
    GITEE("gitee"),
    GITHUB("github");
    @Getter
    @EnumValue
    String type;
    LoginTypeEnum(String name){
        this.type=name;
    }
}
