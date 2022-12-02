package com.baymax.exam.web.config;

import com.baymax.exam.web.interceptor.FeignInterceptor;
import com.baymax.exam.web.interceptor.UserTokenInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/30 19:03
 * @description：
 * @modified By：
 * @version:
 */
@Slf4j
public class BaseWebConfig implements WebMvcConfigurer{

    @Resource
    private UserTokenInterceptor tokenInterceptor ;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**");
    }
}
