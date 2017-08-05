package com.ct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableConfigServer
@EnableEurekaServer
public class App{
	
	 public static void main(String[] args) {
		 System.out.println("以jar包方式启动...");
         SpringApplication.run(App.class, args);
     }
	 
}
 