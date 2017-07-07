package com.ct.pay.alipay;

import com.ct.common.config.PropertiesManager;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.4
 *修改日期：2016-03-08
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
	private static String prop = "/pay/alipay.properties";
	
	public static String getPartner(){
		// 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
		return PropertiesManager.smartloadProperties(prop).getProperty("partner");
	}
	
	public static String getSellerId(){
		// 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
		return PropertiesManager.smartloadProperties(prop).getProperty("partner");
	}

	public static String getKey(){
		// MD5密钥，安全检验码，由数字和字母组成的32位字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
		return PropertiesManager.smartloadProperties(prop).getProperty("key");
	}
	
	public static String getNotifyUrl(){
		// 支付宝--服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
		return PropertiesManager.smartloadProperties(prop).getProperty("notify_url");
	}
	
	public static String getReturnUrl(){
		// 支付宝--页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
		return PropertiesManager.smartloadProperties(prop).getProperty("return_url");
	}
	
	public static String getShowUrl(){
		// 支付宝--收银台页面上，商品展示的超链接，必填
		return PropertiesManager.smartloadProperties(prop).getProperty("show_url");
	}
	
	public static String getSignType(){
		// 签名方式
		//private static static String sign_type = "MD5";
		return PropertiesManager.smartloadProperties(prop).getProperty("sign_type");
	}
	
	public static String getLogPath(){
		// 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。
		return PropertiesManager.smartloadProperties(prop).getProperty("log_path");
	}
	
	public static String getInputCharset(){
		// 字符编码格式 目前支持utf-8
		return PropertiesManager.smartloadProperties(prop).getProperty("input_charset");
	}
	
	public static String getPaymentType(){
		// 支付类型 ，无需修改
		return PropertiesManager.smartloadProperties(prop).getProperty("payment_type");
	}
	
	public static String getService(){
		// 调用的接口名，无需修改
		return PropertiesManager.smartloadProperties(prop).getProperty("service");
	}

	public static String getInfoConfig(String key){
		return PropertiesManager.smartloadProperties(prop).getProperty(key);
	}

}

