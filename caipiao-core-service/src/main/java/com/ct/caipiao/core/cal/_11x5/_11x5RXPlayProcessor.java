package com.ct.caipiao.core.cal._11x5;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ct.caipiao.core.cal.AbstractCatProcessor;
import com.ct.caipiao.core.cal.ILotteryPlayService;
import com.ct.caipiao.core.cal.cjdlt.AbstractCjdltPlayProcessor;
import com.ct.caipiao.core.cal.utils.Combination;
import com.ct.commons.exception.I18nMessageException;
import com.ct.commons.utils.Assert;

public class _11x5RXPlayProcessor extends AbstractCjdltPlayProcessor implements ILotteryPlayService{

	public static Map<String,Integer[]> setting = new HashMap<String,Integer[]>();
	static {
		setting.put("zq1", new Integer[]{1,6});
		
		setting.put("rx2", new Integer[]{2,8});
		setting.put("rx3", new Integer[]{3,9});
		setting.put("rx4", new Integer[]{4,9});
		setting.put("rx5", new Integer[]{5,10});
		setting.put("rx6", new Integer[]{6,10});
		setting.put("rx7", new Integer[]{7,10});
		setting.put("rx8", new Integer[]{8,9});
		
		setting.put("zuq2", new Integer[]{2,8});
		setting.put("zuq3", new Integer[]{3,9});
		
		
		setting.put("zhiq2", new Integer[]{2,2});
		setting.put("zhiq3", new Integer[]{3,3});
	}
	private int minNumber = 2;
	private int maxNumber = 8;
	
	public _11x5RXPlayProcessor(String rxPlay) {
		// TODO Auto-generated constructor stub
		this.minNumber = setting.get(rxPlay)[0];
		this.maxNumber = setting.get(rxPlay)[1];
	}

	@Override
	public long calNumber(String lottery) {
		lottery = verify(lottery);
		String[] s1 = lottery.split(AbstractCatProcessor.NUM_SPLIT_SYMBOL);
		
		long re = Combination.combination(s1.length,s1.length - minNumber);
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
		
		String lots[] = lottery.split(AbstractCatProcessor.NUM_SPLIT_SYMBOL);//前区
		
		if(!(lots.length >= minNumber && lots.length <= maxNumber)){
			throw new I18nMessageException(13009, lottery+"号码个数不正确");
		}
		
		Set<String> redSet = new HashSet<String>();
		for(String ss : lots){//检查格式
			Assert.isNumber(ss, 13005, lottery+"号码个数不正确");
			Assert.hasLength(ss, 2, 13005, lottery+"号码个数不正确");
			int lot = Integer.parseInt(ss);
			if(!(lot > 0 && lot < 12)){
				throw new I18nMessageException(13005,lottery+"号码个数不正确");
			}
			redSet.add(ss);
		}
		
		if(lots.length != redSet.size()){
			throw new I18nMessageException(13006,lottery+"号码重复不正确");
		}
		
		AbstractCatProcessor.sort(lots);
		
		return AbstractCatProcessor.toString(lots); 
	}
	
	public static void main(String[] args) {
		System.out.println(new _11x5RXPlayProcessor("zhiq3").cal("01,02,03", 1));
	}

}
