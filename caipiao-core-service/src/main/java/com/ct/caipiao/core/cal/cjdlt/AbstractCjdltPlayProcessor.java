package com.ct.caipiao.core.cal.cjdlt;

import com.ct.commons.exception.I18nMessageException;
import com.ct.commons.utils.Assert;

public abstract class AbstractCjdltPlayProcessor{
	
	public static final int SINGLE_RED_NUMBER = 5;//单数用球数
	
	public static final int SINGLE_BLUE_NUMBER = 2;//单数用球数
	
	public static final int MAX_BLUE_NUMBER = 12;//最大蓝球数
	
	public static final int MAX_RED_NUMBER = 35;//最大红球数
	
	public static final String RB_SPLIT_SYMBOL = "#";
	
	public static final String SINGLE_SPLIT_SYMBOL="|";
	
	public static final String DT_SPLIT_SYMBOL = ":";
	
	public void verifyRed(String lottery){
		Assert.isNumber(lottery, 11005, lottery+"前区格式不对");
		Assert.hasLength(lottery, 2, 11005, lottery+"前区格式不对");
		int lot = Integer.parseInt(lottery);
		if(!(lot > 0 && lot < 34)){
			throw new I18nMessageException(11005,lottery+"前区格式不对");
		}
	}
	
	public void verifyBlue(String lottery){
		Assert.isNumber(lottery, 11006, lottery+"后区格式不对");
		Assert.hasLength(lottery, 2, 11006, lottery+"后区格式不对");
		int lot = Integer.parseInt(lottery);
		
		if(!(lot > 0 && lot < 17)){
			throw new I18nMessageException(11006,lottery+"后区格式不对");
		}
	}
}
