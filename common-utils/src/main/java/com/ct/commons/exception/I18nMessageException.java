package com.ct.commons.exception;

import org.springframework.util.Assert;

/**
 * service层，controller层 统一消息异常类
 * @author cqy
 *
 */
public class I18nMessageException extends RuntimeException {

	private static final long serialVersionUID = -8537903223002821107L;

	private Integer code;
	private MsgModule module;
	private String info;
	private Exception prevException;
	
	
	public I18nMessageException(Integer code) {
		this(MsgModule.GLOBAL,code);
	}
	
	public I18nMessageException(MsgModule module,Integer code) {
		Assert.notNull(module);
		Assert.notNull(code);
		
		this.code = code;
		this.module = module;
	}
	
	
	public I18nMessageException(Integer code,String info) {
		this(MsgModule.GLOBAL,code,info);
	}
	
	public I18nMessageException(MsgModule module,Integer code,String info) {
		this(module, code);
		Assert.notNull(info);
		this.info = info;
	}
	
	public I18nMessageException(Integer code,String info,Exception prevException) {
		this(MsgModule.GLOBAL,code,info,prevException);
	}
	
	public I18nMessageException(MsgModule module,Integer code,String info,Exception prevException) {
		this(module, code , info);
		this.prevException = prevException;
	}
	
	public I18nMessageException(MsgModule module,Integer code,Exception prevException) {
		this(module, code , prevException.getMessage() , prevException);
	}
	
	public I18nMessageException(Exception prevException) {
		this(MsgModule.GLOBAL,500,prevException);
	}
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public MsgModule getModule() {
		return module;
	}
	public void setModule(MsgModule module) {
		this.module = module;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Exception getPrevException() {
		return prevException;
	}
	public void setPrevException(Exception prevException) {
		this.prevException = prevException;
	}
}
