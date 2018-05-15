package com.valuelabs.poc.devops_utms.resource;

import java.io.Serializable;

public class DayWiseList implements Serializable {
	
	private String day;
	
	private int no;
	
	public DayWiseList(){
		
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	@Override
	public String toString() {
		return "DayWiseList [day=" + day + ", no=" + no + "]";
	}
	
}
