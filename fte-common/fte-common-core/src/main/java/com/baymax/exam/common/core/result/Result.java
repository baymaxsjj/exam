package com.baymax.exam.common.core.result;

import cn.hutool.json.JSONUtil;
import com.baymax.exam.common.core.exception.ResultException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 结果
 * 统一响应结构体
 *
 * @author haoxr
 * @date 2022/1/30
 **/
@Slf4j
@Data
public class Result<T> implements Serializable {

    private String code;

    private T data;

    private String msg;
    //正常：啥也不提示
    public static <T> Result<T> success() {
        return success(null);
    }
    //正常：有数据
    public static <T> Result<T> success(T data) {
        return result(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMsg(), data);
    }
    @JsonIgnore
    public  T getResultDate() throws ResultException {
        log.info("微服务返回数据：{}",JSONUtil.toJsonStr(this));
        if (!code.equals(ResultCode.SUCCESS.getCode())){
             throw new ResultException(this);
        }
        log.info("微服务实际数据：{}",data);
        return data;
    }
    //正常，有数据和消息
    public static <T> Result<T> success(String msg,T data) {
        return result(ResultCode.SUCCESS_MSG.getCode(),msg,data);
    }
    // 正常：有消息
    public static Result msgSuccess(String msg){
        return result(ResultCode.SUCCESS_MSG.getCode(),msg,null);
    }
    //异常：有消息
    public static Result msgInfo(String msg){
        return result(ResultCode.MESSAGE_INFO.getCode(),msg,null);
    }
    public static Result msgWaring(String msg){
        return result(ResultCode.MESSAGE_WARING.getCode(),msg,null);
    }
    public static Result msgError(String msg){
        return result(ResultCode.MESSAGE_ERROR.getCode(),msg,null);
    }

    public static <T> Result<T> failed() {
        return result(ResultCode.SYSTEM_EXECUTION_ERROR.getCode(), ResultCode.SYSTEM_EXECUTION_ERROR.getMsg(), null);
    }

    public static <T> Result<T> failed(String msg) {
        return result(ResultCode.SYSTEM_EXECUTION_ERROR.getCode(), msg, null);
    }

    public static <T> Result<T> judge(boolean status) {
        if (status) {
            return success();
        } else {
            return failed();
        }
    }

    public static <T> Result<T> failed(IResultCode resultCode) {
        return result(resultCode.getCode(), resultCode.getMsg(), null);
    }

    public static <T> Result<T> failed(IResultCode resultCode, String msg) {
        return result(resultCode.getCode(), msg, null);
    }

    private static <T> Result<T> result(IResultCode resultCode, T data) {
        return result(resultCode.getCode(), resultCode.getMsg(), data);
    }

    private static <T> Result<T> result(String code, String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    public static boolean isSuccess(Result<?> result) {
        return result != null && ResultCode.SUCCESS.getCode().equals(result.getCode());
    }
}
