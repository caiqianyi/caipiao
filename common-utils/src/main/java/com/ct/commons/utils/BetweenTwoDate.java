package com.ct.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BetweenTwoDate {

	/**
	 * 
	 *@author wkx
	 *@Description:返回两个日期之间的相差时间如  2014-9~2014-11={20149,201410,201411}
	 *@param date1
	 *@param date2
	 *@return
	 *@date 2016年12月19日
	 */
	public static String[] getMonthBetw2Date(Long date1,Long date2){
		Calendar c1=Calendar.getInstance();
        Calendar c2=Calendar.getInstance();
        
        c1.setTimeInMillis(date1);
        c2.setTimeInMillis(date2);
        int i =c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
        int count = 0;
        
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH)+1;
        int month2 = c2.get(Calendar.MONTH)+1;
        
        
        if(i>=0){
            count =  i*12+c2.get(Calendar.MONTH)-c1.get(Calendar.MONTH);
        }else{
        	return null;
        }
        String[] dates = new String[count+1];
        for (int j = 0; j < count+1; j++) {
        	int m = month1 + j;
        	int y = year1 + (m-1)/12;//年份
        	dates[j] = y+""+(m%12==0?12:m%12);
		}
        return dates;
	}
	
	/**
	 * 
	 *@author wkx
	 *@Description:返回两个日期之间的相差天 如  2014-9~2014-11={20149,201410,201411}
	 *@param date1  秒数
	 *@param date2  秒数
	 *@return
	 * @throws ParseException 
	 *@date 2016年12月19日
	 */
	public static Map<String,String> getMonthBetw2Date2(String d1,String d2) throws ParseException{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
	        Date date1 = df.parse(d1);  
	        Date date2 = df.parse(d2);  
	        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	        Map<String,String> dateList = new HashMap<String,String>();
	        try {  
	          int s = (int) ((date2.getTime() - date1.getTime())/ (24 * 60 * 60 * 1000));  
	          if(s>0){  
	            for(int i = 0;i<=s;i++){  
	              long todayDate = date1.getTime() + i * 24 * 60 * 60 * 1000;  
	              Date tmDate = new Date(todayDate);  
	              /** 
	               * yyyy-MM-dd E :2012-09-01 星期三 
	               */  
	              dateList.put(sf.format(tmDate),sf.format(tmDate));
	            }  
	          }else if(s==0){
	        	  dateList.put(d1,d1);
	          }
	        } catch (Exception e) {  
	          System.out.println("格式错误");  
	        } 
        return dateList;
	}
	
	
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		
		String date1 = "2014-01-10";
		String date2 = "2015-11-10";
		
	}
}
