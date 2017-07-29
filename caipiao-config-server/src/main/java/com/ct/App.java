package com.ct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;


@Configuration
@EnableAutoConfiguration
@RestController
@EnableConfigServer
public class App{
	
	 public static void main(String[] args) {
		 System.out.println("以jar包方式启动...");
         SpringApplication.run(App.class, args);
     }
	 
}
 