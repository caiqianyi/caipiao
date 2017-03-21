package com.ct.caipiao.core.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import com.ct.soa.mongo.core.document.BaseModel;


@Document(collection = "lottery_nums")
public class LotteryNums extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7598797671928622614L;
	
	private String lottery;
	private String cat;
	private Long openTimeStamp;
	private String qihao;
	public String getLottery() {
		return lottery;
	}
	public void setLottery(String lottery) {
		this.lottery = lottery;
	}
	public Long getOpenTimeStamp() {
		return openTimeStamp;
	}
	public void setOpenTimeStamp(Long openTimeStamp) {
		this.openTimeStamp = openTimeStamp;
	}
	public String getQihao() {
		return qihao;
	}
	public void setQihao(String qihao) {
		this.qihao = qihao;
	}
	public String getCat() {
		return cat;
	}
	public void setCat(String cat) {
		this.cat = cat;
	}
}
