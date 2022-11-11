package com.baymax.exam.center.exceptions;

import com.baymax.exam.common.core.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/9 13:18
 * @description：全局异常处理
 * @modified By：
 * @version:
 */
//@Slf4j
@RestControllerAdvice
public class ExamExceptionHandler {
    //指定异常执行方法
    @ExceptionHandler(value = ExamOnLineException.class)
    @ResponseBody
    public Result error(ExamOnLineException e){
        return Result.msgError(e.getMessage());
    }
}
