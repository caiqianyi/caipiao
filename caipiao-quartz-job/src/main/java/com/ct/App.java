package com.ct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ct.soa.web.framework.datasource.DynamicDataSourceRegister;

@EnableTransactionManagement
@Configuration  
@EnableAutoConfiguration
@ComponentScan  
@Import({DynamicDataSourceRegister.class}) //�����Դ����
@SpringBootApplication
public class App extends SpringBootServletInitializer{
	 public static void main(String[] args) {
       SpringApplication.run(App.class, args);
   }
	 
	 @Override
   protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
       // TODO Auto-generated method stub
       return builder.sources(App.class);
   }
	 	
}
