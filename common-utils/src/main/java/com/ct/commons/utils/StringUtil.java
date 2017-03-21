package com.ct.commons.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	public static boolean isNumeric(String str){ 
	   Pattern pattern = Pattern.compile("[0-9]*"); 
	   Matcher isNum = pattern.matcher(str);
	   if( !isNum.matches() ){
	       return false; 
	   } 
	   return true; 
	}
	
	public static void main(String[] args) {
		System.out.println("   just do it!".replaceAll(" ", ""));
	}

}
