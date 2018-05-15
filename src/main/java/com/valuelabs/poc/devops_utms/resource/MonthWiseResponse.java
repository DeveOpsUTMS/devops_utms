package com.valuelabs.poc.devops_utms.resource;

import java.io.Serializable;
import java.util.List;

public class MonthWiseResponse implements Serializable {
	
	private List<Month> months;
	
	public MonthWiseResponse() {
		
	}

	public List<Month> getMonths() {
		return months;
	}

	public void setMonths(List<Month> months) {
		this.months = months;
	}

	@Override
	public String toString() {
		return "MonthWiseResponse [months=" + months + "]";
	}
	
}
