package com.valuelabs.poc.devops_utms.resource.appdynamics;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class MetricValues implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SerializedName("metric-value")
	private MetricValue metricValue;

	public MetricValue getMetricValue() {
		return metricValue;
	}

	public void setMetricValue(MetricValue metricValue) {
		this.metricValue = metricValue;
	}


}
