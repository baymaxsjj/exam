package com.baymax.exam.center.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baymax.exam.common.core.base.IBaseEnum;
import lombok.Getter;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/18 15:16
 * @description：题目类型
 * @modified By：
 * @version:
 */
public enum QuestionTypeEnum implements IBaseEnum<Integer> {
    SIGNAL_CHOICE(0,"单项选择题",2,26),
    MULTIPLE_CHOICE(1,"多项选择题",2,26),
    JUDGMENTAL(2,"判断题",2,2),
    COMPLETION(3,"填空题",1,-1),
    SUBJECTIVE(4,"主观题",1,1);

    @Getter
    @EnumValue //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    private Integer value;

    @Getter
    // @JsonValue //  表示对枚举序列化时返回此字段
    private String label;
    //选择最小个数
    @Getter
    private int itemMin;
    //选项最多个数，<0不限制
    @Getter
    private int itemMax;

    QuestionTypeEnum(Integer value, String label,int itemMin,int itmeMax) {
        this.value = value;
        this.label = label;
        this.itemMin=itemMin;
        this.itemMax=itmeMax;
    }
}
