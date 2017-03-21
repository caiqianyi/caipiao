package com.ct.soa.core.exception;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ct.commons.beans.PropertyConfigurer;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
	private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	private Properties props = null;
	
	private PropertyConfigurer propertyConfigurer;
	
    @ExceptionHandler//处理所有异常
    public I18nErrorMessage exceptionHandler(Exception e, HttpServletResponse response) {
    	I18nErrorMessage message = new I18nErrorMessage();
    	I18nMessageException i18ne = null;
    	if(!(e instanceof I18nMessageException))
    		i18ne = new I18nMessageException(e);
    	else
    		i18ne = ((I18nMessageException)e);
		String key = i18ne.getModule().getValue() + "."+i18ne.getCode();
		String msg = props.getProperty(key);
		message.setErrcode(i18ne.getCode());
    	message.setErrmsg(msg);
    	String info = i18ne.getInfo() == null ? "" : i18ne.getInfo();
    	
    	StackTraceElement ste = e.getStackTrace()[0];
    	
    	String err = ste.getClassName() +"."+ ste.getMethodName() + "(line:"+ste.getLineNumber() +") ~ "
    			+ "{\"module\":\""+i18ne.getModule()+"@"+i18ne.getModule().getValue()+"\","
    					+ "\"errcode\":\""+i18ne.getCode()+"\",\"errmsg\":\""+message.getErrmsg() 
    					+"\",\"message\":\""+info+"\"}";
    	
    	if(i18ne.getPrevException() != null)
    		logger.error(err,i18ne.getPrevException());
    	else
    		logger.error(err);

    	return message;
    }
    @Resource
	public void setPropertyConfigurer(PropertyConfigurer propertyConfigurer) {
		this.propertyConfigurer = propertyConfigurer;
		this.props = this.propertyConfigurer.lazyLoadUniqueProperties("classpath*:i18n_messages.properties");
	}
    
}