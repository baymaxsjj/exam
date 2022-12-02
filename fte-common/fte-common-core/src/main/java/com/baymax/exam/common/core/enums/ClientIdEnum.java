package com.baymax.exam.common.core.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baymax.exam.common.core.base.IBaseEnum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/23 18:55
 * @description：客户端id
 * @modified By：
 * @version:
 */
@Slf4j
public enum ClientIdEnum implements IBaseEnum {
    ADMIN_CLIENT_ID("admin-app"),
    PORTAL_CLIENT_ID("exam-app");
    @Getter
    @EnumValue
    @JsonValue
    private String value;
    ClientIdEnum(String clientId){
        this.value=clientId;
    }
    @Override
    public String getLabel() {
        return null;
    }
    @JsonCreator
    public static ClientIdEnum get(String clientId) {
        return IBaseEnum.getEnumByValue(clientId,ClientIdEnum.class);
    }
}
