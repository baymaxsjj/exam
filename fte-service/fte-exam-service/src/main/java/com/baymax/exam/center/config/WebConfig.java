package com.baymax.exam.center.config;

import com.baymax.exam.center.interceptor.ExamCenterInterceptor;
import com.baymax.exam.web.config.BaseWebConfig;
import com.baymax.exam.web.interceptor.UserTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

import static com.baymax.exam.center.interceptor.ExamCenterInterceptor.EXAM_INFO_KEY;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/12 20:20
 * @description：web配置类
 * @modified By：
 * @version:
 */
@Configuration
public class WebConfig extends BaseWebConfig {

    //在此处，将拦截器注册为一个 Bean
    @Resource
    private ExamCenterInterceptor examCenterInterceptor ;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(examCenterInterceptor).addPathPatterns("/exam-center/**");
    }
}
