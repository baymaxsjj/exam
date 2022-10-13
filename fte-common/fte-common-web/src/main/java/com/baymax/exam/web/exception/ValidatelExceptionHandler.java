package com.baymax.exam.web.exception;

import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/9 15:47
 * @description：通用全局异常捕获
 * @modified By：
 * @version:
 */
@RestControllerAdvice
public class ValidatelExceptionHandler {
    /**
     * 方法参数校验
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Result.failed(ResultCode.PARAM_ERROR,e.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * 单个参数校验
     * @param ex
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public Result handleBindGetException(ConstraintViolationException ex){
        return Result.failed(ResultCode.PARAM_ERROR,ex.getMessage());
    }
}
