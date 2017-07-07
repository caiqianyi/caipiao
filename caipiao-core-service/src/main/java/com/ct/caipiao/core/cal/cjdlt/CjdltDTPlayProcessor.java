package com.ct.caipiao.core.cal.cjdlt;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ct.caipiao.core.cal.AbstractCatProcessor;
import com.ct.caipiao.core.cal.ILotteryPlayService;
import com.ct.caipiao.core.cal.utils.Combination;
import com.ct.commons.exception.I18nMessageException;
import com.ct.commons.utils.Assert;

@Service("cjdltDtPlayProcessor")
public class CjdltDTPlayProcessor extends AbstractCjdltPlayProcessor implements
		ILotteryPlayService {

	public static final int MAX_DAN_NUMBER = 4;// 最大胆码数

	public static final int MAX_TUO_NUMBER = 28;// 最大拖码数

	public static final int MIN_DT_NUMBER = 6;

	@Override
	public long calNumber(String lottery) {
		lottery = verify(lottery);

		String[] s1 = lottery.split("\\"+DT_SPLIT_SYMBOL);
		String[] s2 = s1[1].split("\\"+RB_SPLIT_SYMBOL);
		String[] dans = s1[0].split(AbstractCatProcessor.NUM_SPLIT_SYMBOL);// 胆码
		String[] tuos = s2[0].split(AbstractCatProcessor.NUM_SPLIT_SYMBOL);// 拖码
		String[] blues = s2[1].split(AbstractCatProcessor.NUM_SPLIT_SYMBOL);// 后区

		long re = Combination.combination(tuos.length, SINGLE_RED_NUMBER - dans.length);
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
		// 01,02,03:04,05,06#01,02,03,04

		Assert.notEmpty(lottery, 11001, lottery + "号码格式不正确");

		String[] s1 = lottery.split("\\"+DT_SPLIT_SYMBOL);
		if (s1.length != 2) {
			throw new I18nMessageException(11001, lottery + "号码格式不正确");
		}

		String[] s2 = s1[1].split("\\"+RB_SPLIT_SYMBOL);
		if (s2.length != 2) {
			throw new I18nMessageException(11001, lottery + "号码格式不正确");
		}

		String[] dans = s1[0].split(AbstractCatProcessor.NUM_SPLIT_SYMBOL);// 胆码

		if (!(dans.length > 0 && dans.length <= MAX_DAN_NUMBER)) {
			throw new I18nMessageException(11002, lottery + "胆码个数不正确");
		}
		Set<String> danSet = new HashSet<String>();
		for (String dan : dans) {// 检查格式
			verifyRed(dan);
			danSet.add(dan);
		}

		if (danSet.size() != dans.length) {
			throw new I18nMessageException(11003, lottery + "胆码重复");
		}

		String[] tuos = s2[0].split(AbstractCatProcessor.NUM_SPLIT_SYMBOL);// 拖码
		if (!(tuos.length > 1 && tuos.length < MAX_TUO_NUMBER)) {
			throw new I18nMessageException(11002, lottery + "拖码个数不正确");
		}
		Set<String> tuoSet = new HashSet<String>();
		for (String tuo : tuos) {// 检查格式
			verifyRed(tuo);
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
		if (c < MIN_DT_NUMBER) {
			throw new I18nMessageException(11008, lottery + "胆码+拖码数不能少于6个");
		}

		String[] blues = s2[1].split(AbstractCatProcessor.NUM_SPLIT_SYMBOL);// 篮球
		if (!(blues.length > 0 && blues.length <= MAX_BLUE_NUMBER)) {
			throw new I18nMessageException(11002, lottery + "后区个数不正确");
		}

		for (String blue : blues) {// 检查格式
			verifyBlue(blue);
		}

		AbstractCatProcessor.sort(dans);

		AbstractCatProcessor.sort(tuos);

		AbstractCatProcessor.sort(blues);

		return AbstractCatProcessor.toString(dans) + DT_SPLIT_SYMBOL
				+ AbstractCatProcessor.toString(tuos) + RB_SPLIT_SYMBOL
				+ AbstractCatProcessor.toString(blues);
	}
}
