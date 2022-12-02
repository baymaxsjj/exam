package com.baymax.exam.message;

import com.baymax.exam.message.enums.MessageCodeEnum;
import com.baymax.exam.message.enums.MessageTypeEnum;
import com.baymax.exam.message.model.MessageInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/21 10:28
 * @description：
 * @modified By：
 * @version:
 */
@Data
public class MessageResult<T> {
    @Schema(description = "消息信息")
    @Valid
    private MessageInfo info;

    @Schema(description = "状态码")
    private MessageCodeEnum code;

    @Schema(description = "数据")
    private T data;

    /**
     * 消息
     *
     * @param messageInfo 消息信息
     * @return {@link MessageResult}
     */
    public static MessageResult message(MessageInfo messageInfo){
        return result(null,messageInfo,null);
    }

    /**
     * 数据
     *
     * @param code 代码
     * @param date 日期
     * @return {@link MessageResult}<{@link T}>
     */
    public static <T> MessageResult<T> data(MessageCodeEnum code,T date){
        return result(code,null,date);
    }

    private static <T> MessageResult<T> result(MessageCodeEnum code,MessageInfo info,T data) {
        MessageResult<T> result = new MessageResult<>();
        result.setData(data);
        result.setInfo(info);
        result.setCode(code);
        return result;
    }

}
