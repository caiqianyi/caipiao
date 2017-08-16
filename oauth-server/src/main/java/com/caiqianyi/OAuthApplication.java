package com.caiqianyi;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

import com.ct.soa.web.framework.datasource.DynamicDataSourceRegister;

@SpringBootApplication
@Configuration  
@EnableAutoConfiguration
@ComponentScan
@Import({DynamicDataSourceRegister.class}) //多数据源管理
@EnableRabbit	
@EnableAuthorizationServer
public class OAuthApplication{
	
	 public static void main(String[] args) {
         SpringApplication.run(OAuthApplication.class, args);
     }
	 
}