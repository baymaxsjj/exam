package com.baymax.exam.message;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/17 19:05
 * @description：
 * @modified By：
 * @version:
 */
@SpringBootApplication(scanBasePackages = "com.baymax.exam")
//扫描mapper
@MapperScan(basePackages = "com.baymax.exam.message.mapper")
public class MessageServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageServiceApplication.class,args);
    }
}
