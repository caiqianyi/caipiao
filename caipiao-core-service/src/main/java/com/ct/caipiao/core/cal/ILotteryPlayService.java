package com.ct.caipiao.core.cal;

public interface ILotteryPlayService {
	/**
	 * 计算注数
	 * @param lottery
	 * @return
	 */
	long calNumber(String lottery);
	
	/**
	 * 计算金额
	 * @param lottery 号码
	 * @param muilt 倍数
	 * @return
	 */
	long cal(String lottery,int muilt);
	
	/**
	 * 单注价格
	 * @return
	 */
	long money();
	
	/**
	 * 校验号码格式是否正确
	 * @param lottery
	 * @return
	 */
	String verify(String lottery);
	
}
