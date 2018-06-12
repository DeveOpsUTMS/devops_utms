package com.valuelabs.poc.devops_utms.resource.appdynamics;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class MetricReport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SerializedName("metric-datas")
	private Metrics metrics;

	public Metrics getMetrics() {
		return metrics;
	}

	public void setMetrics(Metrics metrics) {
		this.metrics = metrics;
	}

}
