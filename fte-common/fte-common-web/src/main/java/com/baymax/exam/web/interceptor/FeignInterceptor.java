package com.baymax.exam.web.interceptor;

import com.baymax.exam.common.core.base.SecurityConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ：Baymax
 * @date ：Created in 2022/12/1 9:30
 * @description：Feign拦截器
 * @modified By：
 * @version:
 */
@Slf4j
@Component
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 新增一个header
        requestTemplate.header(SecurityConstants.FROM,SecurityConstants.FROM_IN);
        log.info("feign拦截器生效了!");
    }
}
