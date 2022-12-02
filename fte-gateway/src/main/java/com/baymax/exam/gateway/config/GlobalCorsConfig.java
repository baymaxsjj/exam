package com.baymax.exam.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/7 10:27
 * @description：前端跨域
 * @modified By：
 * @version:
 */

@Configuration
public class GlobalCorsConfig {
//    @Bean
    public CorsWebFilter  corsFilter() {
        //1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //1) 允许的域,不要写*，否则cookie就无法使用了
        config.addAllowedOriginPattern("*");
        //2) 是否发送Cookie信息
        config.setAllowCredentials(true);
        //3) 允许的请求方式GET POST等
        config.addAllowedMethod("*");
        // 4）允许的头信息
        config.addAllowedHeader("*");
        config.setMaxAge(3600L);

        //2.添加映射路径，我们拦截一切请求
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(configSource);
    }
}
