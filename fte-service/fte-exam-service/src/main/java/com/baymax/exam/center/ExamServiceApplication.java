package com.baymax.exam.center;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/17 19:05
 * @description：
 * @modified By：
 * @version:
 */
//表示开启异步化
@EnableAsync
@EnableFeignClients(basePackages="com.baymax.exam.*")
@SpringBootApplication(scanBasePackages = "com.baymax.exam")
//扫描mapper
@MapperScan(basePackages = "com.baymax.exam.center.mapper")
public class ExamServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExamServiceApplication.class,args);
    }
}
