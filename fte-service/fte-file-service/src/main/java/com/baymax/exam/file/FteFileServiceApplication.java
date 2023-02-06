package com.baymax.exam.file;

import cn.xuyanwu.spring.file.storage.EnableFileStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@EnableFileStorage
@SpringBootApplication
public class FteFileServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FteFileServiceApplication.class, args);
	}

}
