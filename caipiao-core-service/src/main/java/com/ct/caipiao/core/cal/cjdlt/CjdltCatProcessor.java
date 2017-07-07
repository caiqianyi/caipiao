package com.ct.caipiao.core.cal.cjdlt;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ct.caipiao.core.cal.AbstractCatProcessor;
import com.ct.caipiao.core.cal.ILotteryCatService;
import com.ct.caipiao.core.cal.ILotteryPlayService;
import com.ct.commons.utils.Assert;

@Service("cjdltCatService")
public class CjdltCatProcessor extends AbstractCatProcessor implements ILotteryCatService {
	
	@Override
	public long calNumber(String play, String lottery) {
		Assert.notEmpty(lottery, 11001, lottery+"号码格式不正确");
		
		long count = 0l;
		String[] s1 = lottery.split(AbstractCjdltPlayProcessor.SINGLE_SPLIT_SYMBOL);
		for(String lot : s1){
			ILotteryPlayService playService = checkPlayType(lot).equals("dt") ? new CjdltDTPlayProcessor() : new CjdltPTPlayProcessor();
			count += playService.calNumber(lot);
		}
		return count;
	}

	@Override
	public long cal(String play, String lottery, int muilt) {
		Assert.notEmpty(lottery, 11001, lottery+"号码格式不正确");
		
		long money = 0l;
		String[] s1 = lottery.split(AbstractCjdltPlayProcessor.SINGLE_SPLIT_SYMBOL);
		ILotteryPlayService playService = getPlayService(play);
		
		for(String lot : s1){
			ILotteryPlayService service = playService;
			if(service == null){
				service = checkPlayType(lot).equals("dt") ? new CjdltDTPlayProcessor() : new CjdltPTPlayProcessor();
			}
			money += service.cal(lot,muilt);
		}
		
		return money;
	}
	
	private ILotteryPlayService getPlayService(String play){
		if(StringUtils.isNotBlank(play)){
			switch (play) {
			case "dt":
				return new CjdltDTPlayProcessor();
			case "pt":
				return new CjdltDTPlayProcessor();
			}
		}
		return null;
	}
	
	@Override
	public String checkPlayType(String lottery) {
		Assert.notEmpty(lottery, 11001, lottery+"号码格式不正确");
		
		String lots[] = lottery.split(AbstractCjdltPlayProcessor.SINGLE_SPLIT_SYMBOL);
		String playtype = null;
		for(String lot : lots){
			String pt = null;
			if(lot.split(AbstractCjdltPlayProcessor.DT_SPLIT_SYMBOL).length>1){
				pt = "dt";
			}else{
				pt = "pt";
			}
			if(playtype != null && !playtype.equals(pt)){
				return "hunhe";
			}
			playtype = pt;
		}
		return playtype;
	}

	public static void main(String[] args) {
		CjdltCatProcessor processor = new CjdltCatProcessor();
		System.out.println(processor.cal(null, "01,02,03,04,05,06#01,02|01,02,03,04:06,07,08#01,02", 1));
	}
}
