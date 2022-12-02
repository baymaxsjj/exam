package com.baymax.exam.message.config;

import com.baymax.exam.message.interceptor.MyWebSocketInterceptor;
import com.baymax.exam.web.interceptor.UserTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/12 20:20
 * @description：web配置类
 * @modified By：
 * @version:
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private UserTokenInterceptor tokenInterceptor ;


    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**");
    }
}
