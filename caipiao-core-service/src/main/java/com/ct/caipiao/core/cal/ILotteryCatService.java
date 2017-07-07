package com.ct.caipiao.core.cal;

public interface ILotteryCatService {

	/**
	 * 计算注数
	 * @param lottery
	 * @return
	 */
	long calNumber(String play,String lottery);
	
	/**
	 * 计算金额
	 * @param lottery 号码
	 * @param muilt 倍数
	 * @return
	 */
	long cal(String play,String lottery,int muilt);
	
	/**
	 * 根据号码判断玩法类型
	 * @param lottery
	 * @return
	 */
	String checkPlayType(String lottery);
}
