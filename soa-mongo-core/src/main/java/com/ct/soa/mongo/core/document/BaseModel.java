package com.ct.soa.mongo.core.document;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;

public class BaseModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5945202000974135733L;
	@Id
	private Long id;
	private Date time;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
