package com.ct.caipiao.core.cal.cjdlt;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ct.caipiao.core.cal.AbstractCatProcessor;
import com.ct.caipiao.core.cal.ILotteryPlayService;
import com.ct.caipiao.core.cal.utils.Combination;
import com.ct.commons.exception.I18nMessageException;
import com.ct.commons.utils.Assert;
@Service("cjdltPtPlayProcessor")
public class CjdltPTPlayProcessor extends AbstractCjdltPlayProcessor implements ILotteryPlayService {

	@Override
	public long calNumber(String lottery) {
		lottery = verify(lottery);
		
		String[] s1 = lottery.split("\\"+RB_SPLIT_SYMBOL);
		String[] reds = s1[0].split(AbstractCatProcessor.NUM_SPLIT_SYMBOL);//胆码
		String[] blues = s1[1].split(AbstractCatProcessor.NUM_SPLIT_SYMBOL);//后区
		
		long re = Combination.combination(reds.length,reds.length - SINGLE_RED_NUMBER);
		long blue = Combination.combination(blues.length, blues.length - SINGLE_BLUE_NUMBER);
		return re * blue;
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
		Assert.notEmpty(lottery, 11001, lottery+"号码格式不正确");
		
		String[] s1 = lottery.split("\\"+RB_SPLIT_SYMBOL);
		if(s1.length != 2){
			throw new I18nMessageException(11001, lottery+"号码格式不正确");
		}
		
		
		String reds[] = s1[0].split(AbstractCatProcessor.NUM_SPLIT_SYMBOL);//前区
		
		if(!(reds.length > 0 && reds.length <= MAX_RED_NUMBER)){
			throw new I18nMessageException(11009, lottery+"前区个数不正确");
		}
		Set<String> redSet = new HashSet<String>();
		for(String red : reds){//检查格式
			verifyRed(red);
			redSet.add(red);
		}
		
		String[] blues = s1[1].split(AbstractCatProcessor.NUM_SPLIT_SYMBOL);//后区
		if(!(blues.length > 0 && blues.length <= MAX_BLUE_NUMBER)){
			throw new I18nMessageException(11002, lottery+"后区个数不正确");
		}
		
		for(String blue : blues){//检查格式
			verifyBlue(blue);
		}
		
		AbstractCatProcessor.sort(reds);
		
		AbstractCatProcessor.sort(blues);
		
		return AbstractCatProcessor.toString(reds) + RB_SPLIT_SYMBOL + AbstractCatProcessor.toString(blues); 
	}

}
