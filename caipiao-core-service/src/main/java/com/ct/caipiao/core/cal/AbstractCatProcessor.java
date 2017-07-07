package com.ct.caipiao.core.cal;

import java.util.Arrays;
import java.util.Collections;

public abstract class AbstractCatProcessor {
	
	public static final String NUM_SPLIT_SYMBOL = ",";
	
	public static void sort(String[] strs){
		Collections.sort(Arrays.asList(strs));
	}
	
	public static String toString(String[] strs){
		String ss = Arrays.toString(strs).replaceAll(" ", "");
		return ss.substring(1,ss.length()-1);
	}
}
