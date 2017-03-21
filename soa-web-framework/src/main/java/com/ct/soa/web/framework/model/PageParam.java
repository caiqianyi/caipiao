package com.ct.soa.web.framework.model;


public class PageParam {
	private int offset = 0;// 起始行
	private int size = 10;// 分页大小
	 
	private Object params; //slq需要的参数对象
	
	private int count;// 记录总条数
	private boolean useCount = true;//是否查询总数
	
	public PageParam(int offset, int size, Object params) {
		this.offset =offset;
		this.size= size;
		this.params = params;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Object getParams() {
		return params;
	}

	public void setParams(Object params) {
		this.params = params;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int offset() {
		return offset;
	}

	public int size() {
		return size;
	}

	public boolean isUseCount() {
		return useCount;
	}

	public void setUseCount(boolean useCount) {
		this.useCount = useCount;
	}
}
