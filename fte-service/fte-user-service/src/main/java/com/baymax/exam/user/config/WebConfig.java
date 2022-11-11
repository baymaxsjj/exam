package com.baymax.exam.user.config;

import com.baymax.exam.web.interceptor.UserTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

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
