package com.valuelabs.poc.devops_utms.resource;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


public class ExecutionStart {

	private Long unixtime;
	public Long getUnixtime() {
		return unixtime;
	}
	public void setUnixtime(Long unixtime) {
		this.unixtime = unixtime;
	}
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date date;
	
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
