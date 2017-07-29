package com.ct;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableAutoConfiguration
@ComponentScan
// 多数据源管理
@SpringBootApplication
@EnableRabbit
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder builder) {
		System.out.println("以war包方式启动...");
		return builder.sources(this.getClass());
	}

}
