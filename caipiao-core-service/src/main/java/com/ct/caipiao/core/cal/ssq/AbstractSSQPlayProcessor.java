package com.ct.caipiao.core.cal.ssq;

import com.ct.commons.exception.I18nMessageException;
import com.ct.commons.utils.Assert;

public abstract class AbstractSSQPlayProcessor{
	
	public static final int SINGLE_RED_NUMBER = 6;//单数用球数
	
	public static final int SINGLE_BLUE_NUMBER = 1;//单数用球数
	
	public static final int MAX_BLUE_NUMBER = 16;//最大蓝球数
	
	public static final int MAX_RED_NUMBER = 33;//最大红球数
	
	public static final String RB_SPLIT_SYMBOL = "\\#";
	
	public static final String SINGLE_SPLIT_SYMBOL="\\|";
	
	public static final String DT_SPLIT_SYMBOL = "\\:";
	
	public void verifyRed(String lottery){
		Assert.isNumber(lottery, 11005, lottery+"红球格式不对");
		Assert.hasLength(lottery, 2, 11005, lottery+"红球格式不对");
		int lot = Integer.parseInt(lottery);
		if(!(lot > 0 && lot < 34)){
			throw new I18nMessageException(11005,lottery+"红球格式不对");
		}
	}
	
	public void verifyBlue(String lottery){
		Assert.isNumber(lottery, 11006, lottery+"篮球格式不对");
		Assert.hasLength(lottery, 2, 11006, lottery+"篮球格式不对");
		int lot = Integer.parseInt(lottery);
		
		if(!(lot > 0 && lot < 17)){
			throw new I18nMessageException(11006,lottery+"篮球格式不对");
		}
	}
}
