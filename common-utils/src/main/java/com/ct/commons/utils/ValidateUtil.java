package com.ct.commons.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则校验类
 * @author xhl
 *
 */
public class ValidateUtil {
	/**
	 * 验证只能输入字母或者数字
	 * @param email
	 * @return
	 */
	public static boolean checkNumberOrChar(String email) {
		boolean flag = false;
		try {
			String check = "[0-9A-Za-z]*";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 验证只能输入数字
	 * @param email
	 * @return
	 */
	public static boolean checkNumber(String email) {
		boolean flag = false;
		try {
			String check = "[0-9]*";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 校验微信号
	 * @param weChat
	 * @return
	 */
	public static boolean checkWechat(String weChat){
		
		boolean flag = false;
		try {
			String check = "[0-9a-zA-z-_]+$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(weChat);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	/**
	 * 校验手机号
	 * @param tel
	 * @return false true
	 */
	public static boolean checkTel(String tel){
		
		boolean flag = false;
		try {
			String check = "^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(tel);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	//校验金额
	public static boolean isBigDecimal(String str) {  
        java.util.regex.Matcher match =null;  
        if(checkNumber(str)==true){  
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[0-9]*");  
            match = pattern.matcher(str.trim());  
        }else{  
            if(str.trim().indexOf(".")==-1){  
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^[+-]?[0-9]*");  
                match = pattern.matcher(str.trim());  
            }else{  
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^[+-]?[0-9]+(\\.\\d{1,100}){1}");  
                match = pattern.matcher(str.trim());                  
            }  
        }  
        return match.matches();
	}
	public static void main(String[] args){
		System.out.println(checkTel("17012345678"));
	}
}
