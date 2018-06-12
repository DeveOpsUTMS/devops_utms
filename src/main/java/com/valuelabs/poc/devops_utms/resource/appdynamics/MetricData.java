package com.valuelabs.poc.devops_utms.resource.appdynamics;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class MetricData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int metricId;
	private String metricPath;
	private String metricName;
	
	@SerializedName("metricValues")
	private MetricValues metricValues;

	public int getMetricId() {
		return metricId;
	}

	public void setMetricId(int metricId) {
		this.metricId = metricId;
	}

	public String getMetricPath() {
		return metricPath;
	}

	public void setMetricPath(String metricPath) {
		this.metricPath = metricPath;
	}

	public String getMetricName() {
		return metricName;
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	public MetricValues getMetricValues() {
		return metricValues;
	}

	public void setMetricValues(MetricValues metricValues) {
		this.metricValues = metricValues;
	}
	
	
	

}
