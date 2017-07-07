package com.ct.caipiao.core.cal._11x5;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ct.caipiao.core.cal.AbstractCatProcessor;
import com.ct.caipiao.core.cal.ILotteryPlayService;
import com.ct.caipiao.core.cal.utils.Combination;
import com.ct.commons.exception.I18nMessageException;
import com.ct.commons.utils.Assert;

public class _11x5RXDTPlayProcessor implements ILotteryPlayService{

	public static Map<String,Integer[]> setting = new HashMap<String,Integer[]>();
	static {
		setting.put("rx2", new Integer[]{1,1,2,10,2});
		setting.put("rx3", new Integer[]{1,2,2,10,3});
		setting.put("rx4", new Integer[]{1,3,1,10,4});
		setting.put("rx5", new Integer[]{1,4,2,10,5});
		setting.put("rx6", new Integer[]{1,5,2,10,6});
		setting.put("rx7", new Integer[]{1,6,2,10,7});
		
		setting.put("zuq2", new Integer[]{1,1,2,10,2});
		setting.put("zuq3", new Integer[]{1,2,2,10,3});
	}
	
	private int minDanNum,maxDanNum,minTuoNum,maxTuoNum,minDTNum;
	
	public _11x5RXDTPlayProcessor(String rxPlay) {
		// TODO Auto-generated constructor stub
		this.minDanNum = setting.get(rxPlay)[0];
		this.maxDanNum = setting.get(rxPlay)[1];
		this.minTuoNum = setting.get(rxPlay)[2];
		this.maxTuoNum = setting.get(rxPlay)[3];
		this.minDTNum = setting.get(rxPlay)[4];
	}

	@Override
	public long calNumber(String lottery) {
		lottery = verify(lottery);
		String[] s1 = lottery.split("\\#");
		String[] dans = s1[0].split(AbstractCatProcessor.NUM_SPLIT_SYMBOL);// 胆码
		String[] tuos = s1[1].split(AbstractCatProcessor.NUM_SPLIT_SYMBOL);// 拖码

		long re = Combination.combination(tuos.length, minDTNum - dans.length);
		return re;
	}

	@Override
	public long cal(String lottery, int muilt) {
		return calNumber(lottery) * muilt * money();
	}

	@Override
	public long money() {
		return 2;
	}

	@Override
	public String verify(String lottery) {
		Assert.notEmpty(lottery, 13001, lottery+"号码格式不正确");
		
		String[] s1 = lottery.split("\\#");
		if (s1.length != 2) {
			throw new I18nMessageException(11001, lottery + "号码格式不正确");
		}

		String[] dans = s1[0].split(AbstractCatProcessor.NUM_SPLIT_SYMBOL);// 胆码

		if (!(dans.length >= minDanNum && dans.length <= maxDanNum)) {
			throw new I18nMessageException(14002, lottery + "胆码个数不正确");
		}
		Set<String> danSet = new HashSet<String>();
		for (String dan : dans) {// 检查格式
			Assert.isNumber(dan, 13005, lottery+"号码个数不正确");
			Assert.hasLength(dan, 2, 13005, lottery+"号码个数不正确");
			int lot = Integer.parseInt(dan);
			if(!(lot > 0 && lot < 12)){
				throw new I18nMessageException(13005,lottery+"号码个数不正确");
			}
			danSet.add(dan);
		}

		if (danSet.size() != dans.length) {
			throw new I18nMessageException(11003, lottery + "胆码重复");
		}

		String[] tuos = s1[1].split(AbstractCatProcessor.NUM_SPLIT_SYMBOL);// 拖码
		if (!(tuos.length >= minTuoNum && tuos.length <= maxTuoNum)) {
			throw new I18nMessageException(11002, lottery + "拖码个数不正确");
		}
		Set<String> tuoSet = new HashSet<String>();
		for (String tuo : tuos) {// 检查格式
			Assert.isNumber(tuo, 13005, lottery+"号码个数不正确");
			Assert.hasLength(tuo, 2, 13005, lottery+"号码个数不正确");
			int lot = Integer.parseInt(tuo);
			if(!(lot > 0 && lot < 12)){
				throw new I18nMessageException(13005,lottery+"号码个数不正确");
			}
			tuoSet.add(tuo);
		}

		if (tuoSet.size() != tuos.length) {
			throw new I18nMessageException(11004, lottery + "拖码重复");
		}

		for (String dan : danSet) {//
			for (String tuo : tuoSet) {
				if (dan.equals(tuo)) {
					throw new I18nMessageException(11007, lottery + "胆码拖码重复");
				}
			}
		}

		int c = dans.length + tuos.length;
		if (c < minDTNum) {
			throw new I18nMessageException(11008, lottery + "胆码+拖码数不能少于"+minDTNum+"个");
		}
		
		AbstractCatProcessor.sort(dans);
		AbstractCatProcessor.sort(tuos);
		
		return AbstractCatProcessor.toString(dans)+"#"+AbstractCatProcessor.toString(tuos); 
	}
	
	public static void main(String[] args) {
		System.out.println(new _11x5RXDTPlayProcessor("zuq2").cal("01#02,03,04,05,06,07,08,09", 1));
	}

}
