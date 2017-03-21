package com.ct.commons.utils;

/**
 * 定义系统中需要的常量
 * @author xhl
 *
 */
public class Constant {
	
	public static final String SESSION_USER_CODE="usercode"; //用户登录账号
	
	public static final String SESSION_USER_NAME="username"; //用户姓名
	
	public static final String SESSION_USER_ID="userid";     //用户id
	 
	public static final String SESSION_USER_ROLE="userrole";  //用户角色
	
	public static final String SESSION_USER="user";  //登录用户信息
	
	public static final String UPDATE_PSW_ERR="prepswErr";    //原始密码输入有误
	
	public static final String AGENT_DEFAULT_PASSWORD="12345";  //代理商初始密码
	
	public static final String SYS_VERIFYCODE="verifycode";    //验证码
	
	public static final String SYS_VERIFYCODE_FORM="0123456789";  //验证码组成
	
	public static final String COOKIE_ACCOUNT="username";//登录页面取出的账户
	
	public static final int QUERY_NUM=5000;//每次查询的数量
	
	public static final String WD_UPDATEPASS_URL = "http://vap.gw.weidian.com/h5/wpartner/ResetPassword/1.0";//微店重置密码接口
	public static final String WD_ENCRYPT_KEY = "1234567xianlai90paohuzi123$9^0~!";//微店重置密码接口key
	
	public static final String PAY_WAY = "('WX','ZFB')";
}
