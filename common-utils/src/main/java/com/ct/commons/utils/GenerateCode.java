package com.ct.commons.utils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;


public class GenerateCode {

	/**
	 * 随机码位数
	 * @param inputnum
	 * @return
	 */
    public static long gen(int inputnum) {
    	int num=inputnum+2;
        String current = String.valueOf((java.util.UUID.randomUUID().getLeastSignificantBits())).substring(2,num);

        while (current.startsWith("0") || isFourSame(current)) {
            current = String.valueOf((java.util.UUID.randomUUID().getLeastSignificantBits())).substring(2, num);
        }
        return Long.parseLong(current);
    }

    public static boolean isFourSame(String input) {
        Map<Integer, Integer> current = new HashMap<Integer, Integer>();
        for (int i = 0; i < input.length(); i++) {
            int inputInt = Integer.parseInt(String.valueOf(input.indexOf(i)));
            if (null == current.get(inputInt)) {
                current.put(inputInt, 1);
            } else {
                int pre = current.get(inputInt);
                if (pre >= 2) {
                    return false;
                } else {
                    current.put(inputInt, pre + 1);
                }
            }
        }
        return true;
    }
    
    //年+5位数 例如：201500001、201500002、201500003.。。
    public static long getYearSeq(int seq){
    	String str = new SimpleDateFormat("yyyy")
		.format(new java.util.Date());
		long value = Long.parseLong((str)) * 100000;
		value=value+seq;
		return value;
    }
    
    //生成购买道具订单号，生成规则示例：20160622+类型(5位)+玩家id(9位，不够前面补0)++8位随机数
    public static synchronized String genToolOrderNo(){
    	String today = new SimpleDateFormat("yyyyMMdd")
				.format(new java.util.Date());
    	return "TONPAY_"+today+getUUID().substring(0, 17);
    }    
    
    public static String getUUID(){
    	return UUID.randomUUID().toString().replace("-","");
    }
    /**
     * 生成随机数
     * @return
     */
    public static int getRandom(int max){
    	Random random = new Random();
    	return random.nextInt(max);
    }
    public static void main(String[] a){
    	System.out.println(genToolOrderNo());
    }

}
