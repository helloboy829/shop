package com.qiguliuxing.dts.wx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 小程序服务启动类
 * 
 * @author CHENBO
 * @QQ:623659388
 */
@SpringBootApplication(scanBasePackages = { "com.qiguliuxing.dts.db", "com.qiguliuxing.dts.core",
		"com.qiguliuxing.dts.wx" })
@MapperScan({ "com.qiguliuxing.dts.db.dao", "com.qiguliuxing.dts.db.dao.ex" })
@EnableTransactionManagement
@EnableScheduling
public class Application {
	// 小程序后台服务启动类
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}