package com.valuelabs.poc.devops_utms.resource.appdynamics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Metrics implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SerializedName("metric-data")
	private List<MetricData> metricList = new ArrayList<>();

	public List<MetricData> getMetricList() {
		return metricList;
	}

	public void setMetricList(List<MetricData> metricList) {
		this.metricList = metricList;
	}

}
