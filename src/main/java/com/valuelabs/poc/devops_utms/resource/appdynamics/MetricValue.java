package com.valuelabs.poc.devops_utms.resource.appdynamics;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class MetricValue implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SerializedName("value")
	private int percentageCpuUsed;
	@SerializedName("current")
	private int currentCpuPercentage;
	
	private int min;
	private int max;
	
	
	public int getPercentageCpuUsed() {
		return percentageCpuUsed;
	}
	public void setPercentageCpuUsed(int percentageCpuUsed) {
		this.percentageCpuUsed = percentageCpuUsed;
	}
	public int getCurrentCpuPercentage() {
		return currentCpuPercentage;
	}
	public void setCurrentCpuPercentage(int currentCpuPercentage) {
		this.currentCpuPercentage = currentCpuPercentage;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	
	
	

}
