package com.baymax.exam.web.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baymax.exam.common.core.base.SecurityConstants;
import com.baymax.exam.web.utils.UserAuthUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
        //1、使用RequestContextHolder拿到刚进来的请求数据
        if(UserAuthUtil.getUser()!=null){
            requestTemplate.header(SecurityConstants.USER_TOKEN_HEADER, "{\"id\":"+UserAuthUtil.getUserId()+"}");
        }
        // 新增一个header
        requestTemplate.header(SecurityConstants.FROM, SecurityConstants.FROM_IN);
        log.info("feign拦截器{}", requestTemplate.headers());
    }
}
