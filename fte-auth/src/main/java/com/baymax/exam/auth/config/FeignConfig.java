package com.baymax.exam.auth.config;

import com.baymax.exam.auth.interceptor.FeignInterceptor;
import okhttp3.ConnectionPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author ：Baymax
 * @date ：Created in 2022/12/1 9:37
 * @description：配置openfeign
 * @modified By：
 * @version:
 */
@Configuration
public class FeignConfig {
    @Bean
    public FeignInterceptor customFeignInterceptor(){
        return new FeignInterceptor();
    }
    @Bean
    public okhttp3.OkHttpClient okHttpClient(){
        return new okhttp3.OkHttpClient.Builder()
                //设置连接超时
                .connectTimeout(60, TimeUnit.SECONDS)
                //设置读超时
                .readTimeout(60, TimeUnit.SECONDS)
                //设置写超时
                .writeTimeout(120,TimeUnit.SECONDS)
                //是否自动重连
                .retryOnConnectionFailure(true)
                .connectionPool(new ConnectionPool())
                //构建OkHttpClient对象
                .build();
    }
}
