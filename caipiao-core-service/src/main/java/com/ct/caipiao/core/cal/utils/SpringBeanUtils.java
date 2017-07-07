package com.ct.caipiao.core.cal.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringBeanUtils implements ApplicationContextAware {

	private static ApplicationContext context = null;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		context = applicationContext;
	}
	
	public static ApplicationContext getApplicationContext(){
		return context;
	}

	public synchronized static Object getBean(String beanName) {
		return context.getBean(beanName);
	}
}