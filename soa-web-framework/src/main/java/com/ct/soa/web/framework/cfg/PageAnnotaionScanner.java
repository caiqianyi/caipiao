package com.ct.soa.web.framework.cfg;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

import com.ct.soa.web.framework.interceptor.MybatisPageInterceptor;

@Configuration
@AutoConfigureAfter(SqlSessionFactory.class)
public class PageAnnotaionScanner implements InitializingBean{
	@Value(value = "${page.annotation.enable:true}")
	private boolean enable;

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (enable) {
			sqlSessionFactory.getConfiguration().addInterceptor(new MybatisPageInterceptor());
		}
		
	}
}
