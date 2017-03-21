package com.ct.soa.web.framework.model;

import java.util.List;

/**
 * 分页结果模型类
 * 
 * @author liuqs
 *
 */
public class PageResultSet {
	private int iDisplayStart = 0;// 起始行
	private int iDisplayLength = 15;// 分页大小

	private Integer iRecordsTotal;
	private Integer iTotalDisplayRecords;// 搜索过滤后的总行数

	private Object aaData;// 结果集

	/**
	 * 
	 * @param offset
	 * @param pageSize
	 * @param total
	 * @param data
	 */
	public PageResultSet(PageParam pp, Object data) {
		this.iDisplayStart = pp.offset();
		this.iDisplayLength = pp.size();
		this.aaData = data;

		if (pp.getCount() != 0) {
			iRecordsTotal = pp.getCount();
			iTotalDisplayRecords = pp.getCount();
		}
	}

	/**
	 * 
	 * @param offset
	 * @param pageSize
	 * @param total
	 * @param data
	 */
	public PageResultSet(int offset, int pageSize, int total, List<?> data) {
		this.iDisplayStart = offset;
		this.iDisplayLength = pageSize;
		this.aaData = data;

		if (total != 0) {
			iRecordsTotal = total;
			iTotalDisplayRecords = total;
		}
	}

	public int getiDisplayStart() {
		return iDisplayStart;
	}

	public void setiDisplayStart(int iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	public int getiDisplayLength() {
		return iDisplayLength;
	}

	public void setiDisplayLength(int iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}

	public Integer getiRecordsTotal() {
		return iRecordsTotal;
	}

	public void setiRecordsTotal(Integer iRecordsTotal) {
		this.iRecordsTotal = iRecordsTotal;
	}

	public Integer getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setiTotalDisplayRecords(Integer iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	public Object getAaData() {
		return aaData;
	}

	public void setAaData(Object aaData) {
		this.aaData = aaData;
	}
}
