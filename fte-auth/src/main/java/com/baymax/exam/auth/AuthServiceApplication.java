package com.baymax.exam.auth;

import com.baymax.exam.user.feign.UserClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/10 18:30
 * @description：认证服务器
 * @modified By：
 * @version:
 */
@EnableFeignClients(basePackageClasses = UserClient.class)
@SpringBootApplication
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class,args);
    }
}
