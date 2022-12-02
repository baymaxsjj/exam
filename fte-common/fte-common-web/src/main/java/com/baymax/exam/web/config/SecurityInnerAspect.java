package com.baymax.exam.web.config;

import cn.hutool.core.util.StrUtil;
import com.baymax.exam.common.core.base.SecurityConstants;
import com.baymax.exam.web.annotation.Inner;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

/**
 * @author ：Baymax
 * @date ：Created in 2022/12/1 9:08
 * @description：
 * @modified By：
 * @version:
 */
@Slf4j
@Aspect
@Component
public record SecurityInnerAspect(HttpServletRequest request) implements Ordered {

    @SneakyThrows
    @Around("@within(inner) || @annotation(inner)")
    public Object around(ProceedingJoinPoint point, Inner inner) {
        // 实际注入的inner实体由表达式后一个注解决定，即是方法上的@Inner注解实体，若方法上无@Inner注解，则获取类上的
        if (inner == null) {
            Class<?> clazz = point.getTarget().getClass();
            inner = AnnotationUtils.findAnnotation(clazz, Inner.class);
        }
        String header = request.getHeader(SecurityConstants.FROM);
        if (inner.value() && !StrUtil.equals(SecurityConstants.FROM_IN, header)) {
            log.warn("访问接口 {} 没有权限", point.getSignature().getName());
            throw new AccessDeniedException("Access is denied");
        }
        return point.proceed();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

}
