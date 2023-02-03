package com.baymax.exam.message.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author Baymax
 * @date 2022/11/21
 * code:
 *      模块号
 *      通知权限
 *          0：模块
 *          1：系统
 *          2:系统/模块
 *      显示模式
 *          0 富文本
 *          1 卡片通知
 *          2 图片
 *          3 视频
 *          4 链接
 *
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MessageTypeEnum {
    NONE(0,"无"),
    SYSTEM_NOTIFICATION_MESSAGE(2100,"系统通知消息"),
    SYSTEM_ACTIVITIES_MESSAGE(2101,"系统活动消息"),
    COURSE_EXAM_MESSAGE(3211,"考试通知"),
    COURSE_HOMEWORK_MESSAGE(3212,"作业通知"),
    COURSE_USER_MESSAGE(3003,"用户消息"),
    COURSE_PICTURE_MESSAGE(3024,"图片消息"),
    COURSE_LINK_MESSAGE(3045,"链接消息"),
    COURSE_VIDEO_MESSAGE(3036,"视频消息");
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
