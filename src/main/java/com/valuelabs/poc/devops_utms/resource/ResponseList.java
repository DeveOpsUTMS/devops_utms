package com.valuelabs.poc.devops_utms.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseList {

    private String year;
    private String month;
    private List<String> weeks;
    private List<String> dates;
    private Map<String, Object> keyMap = new HashMap<>();
    
    public ResponseList(){}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public List<String> getWeeks() {
		return weeks;
	}

	public void setWeeks(List<String> weeks) {
		this.weeks = weeks;
	}

	public List<String> getDates() {
		return dates;
	}

	public void setDates(List<String> dates) {
		this.dates = dates;
	}

	public Map<String, Object> getKeyMap() {
		return keyMap;
	}

	public void setKeyMap(Map<String, Object> keyMap) {
		this.keyMap = keyMap;
	}

	@Override
	public String toString() {
		return "ResponseList [year=" + year + ", month=" + month + ", weeks=" + weeks + ", dates=" + dates + ", keyMap="
				+ keyMap + "]";
	}
}
