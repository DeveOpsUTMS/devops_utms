package com.valuelabs.poc.devops_utms.resource;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Month implements Serializable {
	
	private String monthName;
	//private List<String> dates;
	private Map<String, Object> datesList;
	
	
public Month(){
	
}


public String getMonthName() {
	return monthName;
}


public void setMonthName(String monthName) {
	this.monthName = monthName;
}


public Map<String, Object> getDatesList() {
	return datesList;
}


public void setDatesList(Map<String, Object> datesList) {
	this.datesList = datesList;
}


@Override
public String toString() {
	return "Month [monthName=" + monthName + ", datesList=" + datesList + "]";
}







}
