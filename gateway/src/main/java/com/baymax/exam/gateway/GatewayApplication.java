package com.baymax.exam.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/4 22:04
 * @description：网关服务
 * @modified By：
 * @version:
 */
//开启swagger
@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
