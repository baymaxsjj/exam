package com.baymax.exam.common.core.exception;

/**
 * @author ：Baymax
 * @date ：Created in 2023/1/31 17:04
 * @description：
 * @modified By：
 * @version:
 */
public class ResultException extends Exception{
    public ResultException(Object date){
        super("微服务调用异常："+date.toString());
    }
}
