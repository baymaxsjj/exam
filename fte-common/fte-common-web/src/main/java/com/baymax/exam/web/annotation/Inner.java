package com.baymax.exam.web.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/30 18:43
 * @description：内部接口注解
 * @modified By：
 * @version:
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Inner {

    /**
     * 是否AOP统一处理
     *
     * @return false, true
     */
    boolean value() default true;
}